(ns aesopica.converter-test
  (:require [clojure.test :refer :all]
            [aesopica.core :as aes :refer :all]
            [aesopica.converter :as conv :refer :all]
            [clojure.spec.alpha :as s]
            [aesopica.examples :refer :all]
            [clojure.set :as set]))

(deftest convert-to-resource-test
  (let [context {:foxstork "http://www.newresalhaider.com/ontologies/aesop/foxstork/"}
        kw :foxstork/fox]
    (is (=

         (str (contextualize context kw))
         (.getURI (convert-to-resource context kw))))))

(deftest convert-to-literal-string-test
  (let [string "a string"
        literal (convert-to-literal {} string)
        datatype-uri "http://www.w3.org/2001/XMLSchema#string"]
    (is (= string (.getString literal)))
    (is (= datatype-uri (.getDatatypeURI literal)))))

(deftest convert-to-literal-long-test
  (let [long 3
        literal (convert-to-literal {} long)
        datatype-uri "http://www.w3.org/2001/XMLSchema#long"]
    (is (= (str long) (.getString literal)))
    (is (= datatype-uri (.getDatatypeURI literal)))))

(deftest convert-to-literal-boolean-test
  (let [boolean true
        literal (convert-to-literal {} boolean)
        datatype-uri "http://www.w3.org/2001/XMLSchema#boolean"]
    (is (= (str boolean) (.getString literal)))
    (is (= datatype-uri (.getDatatypeURI literal)))))

(deftest convert-to-object-test
  (let [element "test"
        context {}]
    (is (= element (.getString (convert-to-object context element))))))

(deftest convert-to-statement-test
  (let [context {:foxstork "http://www.newresalhaider.com/ontologies/aesop/foxstork/"}
        triple [:foxstork/fox :foxstork/gives-invitation :foxstork/invitation1]]
    (is (= (str "[" (clojure.string/join ", " (map contextualize (repeat context) triple)) "]") (.toString (convert-to-statement context triple))))))

(deftest convert-to-statement-quad-test
  (let [context {:foxstork "http://www.newresalhaider.com/ontologies/aesop/foxstork/"}
        quad [:foxstork/fox :foxstork/gives-invitation :foxstork/invitation1 :foxstork/dinner1]]
    (is (= (str "[" (clojure.string/join ", " (map contextualize (repeat context) (take 3 quad))) "]") (.toString (convert-to-statement context quad))))))

(deftest convert-to-statement-literal-long-test
  (let [context {:foxstork "http://www.newresalhaider.com/ontologies/aesop/foxstork/"}
        triple [:foxstork/fox :foxstork/age 6]]
    (is (= "[http://www.newresalhaider.com/ontologies/aesop/foxstork/fox, http://www.newresalhaider.com/ontologies/aesop/foxstork/age, \"6\"^^http://www.w3.org/2001/XMLSchema#long]" (.toString (convert-to-statement context triple))))))

(deftest convert-to-statement-literal-double-test
  (let [context {:foxstork "http://www.newresalhaider.com/ontologies/aesop/foxstork/"}
        triple [:foxstork/fox :foxstork/weight-in-kg 8.3]]
    (is (= "[http://www.newresalhaider.com/ontologies/aesop/foxstork/fox, http://www.newresalhaider.com/ontologies/aesop/foxstork/weight-in-kg, \"8.3\"^^http://www.w3.org/2001/XMLSchema#double]" (.toString (convert-to-statement context triple))))))

(deftest convert-to-statement-literal-string-test
  (let [context {:foxstork "http://www.newresalhaider.com/ontologies/aesop/foxstork/"}
        triple [:foxstork/fox :foxstork/name "Mr. Fox"]]
    (is (= "[http://www.newresalhaider.com/ontologies/aesop/foxstork/fox, http://www.newresalhaider.com/ontologies/aesop/foxstork/name, \"Mr. Fox\"]" (.toString (convert-to-statement context triple))))))

(deftest convert-to-statement-literal-boolean-test
  (let [context {:foxstork "http://www.newresalhaider.com/ontologies/aesop/foxstork/"}
        triple [:foxstork/fox :foxstork/isCunning true]]
    (is (= "[http://www.newresalhaider.com/ontologies/aesop/foxstork/fox, http://www.newresalhaider.com/ontologies/aesop/foxstork/isCunning, \"true\"^^http://www.w3.org/2001/XMLSchema#boolean]" (.toString (convert-to-statement context triple))))))

(deftest convert-to-statement-custom-datatype-test
  (let [context {:foxstork "http://www.newresalhaider.com/ontologies/aesop/foxstork/"
                 :xsd "http://www.w3.org/2001/XMLSchema#"}
        triple [:foxstork/dinner :foxstork/planned-on-date {:value "2002-09-24" :type :xsd/date}]]
    (is (= "[http://www.newresalhaider.com/ontologies/aesop/foxstork/dinner, http://www.newresalhaider.com/ontologies/aesop/foxstork/planned-on-date, \"2002-09-24\"^^http://www.w3.org/2001/XMLSchema#date]" (.toString (convert-to-statement context triple))))))

(deftest convert-to-quad-triple-test
  (let [context {:foxstork "http://www.newresalhaider.com/ontologies/aesop/foxstork/"}
        triple [:foxstork/fox :foxstork/name "Mr. Fox"]]
    (is (= "[_ http://www.newresalhaider.com/ontologies/aesop/foxstork/fox http://www.newresalhaider.com/ontologies/aesop/foxstork/name \"Mr. Fox\"]" (.toString (convert-to-quad context triple))))))

(deftest convert-to-quad-quad-test
  (let [context {:foxstork "http://www.newresalhaider.com/ontologies/aesop/foxstork/"}
        quad [:foxstork/fox :foxstork/gives-invitation :foxstork/invitation1 :foxstork/dinner1]]
    (is (=

         "[http://www.newresalhaider.com/ontologies/aesop/foxstork/dinner1 http://www.newresalhaider.com/ontologies/aesop/foxstork/fox http://www.newresalhaider.com/ontologies/aesop/foxstork/gives-invitation http://www.newresalhaider.com/ontologies/aesop/foxstork/invitation1]"
         (.toString (convert-to-quad context quad))))))

(deftest convert-to-dataset-graph-empty-test
  (is (= true (.isEmpty  (convert-to-dataset-graph {})))))

(deftest convert-to-dataset-graph-fox-and-stork-test
  (is (= 14 (count (iterator-seq (.find (convert-to-dataset-graph fox-and-stork-edn)))))))

(defn diff [dataset-graph1 dataset-graph2]
  (let [dataset-graph1-set (set (clojure.string/split (write-dataset-graph-quad dataset-graph1) #"\n"))
        dataset-graph2-set (set (clojure.string/split (write-dataset-graph-quad dataset-graph2) #"\n"))
        diff (clojure.data/diff dataset-graph1-set dataset-graph2-set)]
    diff))

(deftest convert-to-dataset-graph-fox-and-stork-comparison-test
  (let [dataset-graph1 (convert-to-dataset-graph fox-and-stork-edn)
        dataset-graph2 (read-dataset-graph fox-and-stork-rdf)
        diff-result (diff dataset-graph1 dataset-graph2)]
    (is (= nil (first diff-result)))
    (is (= nil (second diff-result)))
    (is (= 14 (count (last diff-result))))))

(deftest convert-to-dataset-graph-fox-and-stork-literals-test
  (is (= 10 (count (iterator-seq (.find  (convert-to-dataset-graph fox-and-stork-literals-edn)))))))

(deftest convert-to-dataset-graph-fox-and-stork-literals-comparison-test
  (let [dataset-graph1 (convert-to-dataset-graph fox-and-stork-literals-edn)
        dataset-graph2 (read-dataset-graph fox-and-stork-literals-rdf)
        diff-result (diff dataset-graph1 dataset-graph2)]
    (is (= 3 (count (first diff-result))))
    (is (= 2 (count (second diff-result))))
    (is (= 7 (count (last diff-result))))))

(deftest convert-to-dataset-graph-fox-and-stork-reif-test
  (is (= 14 (count (iterator-seq (.find  (convert-to-dataset-graph fox-and-stork-reif-edn)))))))

(deftest convert-to-dataset-graph-fox-and-stork-reif-comparison-test
  (let [dataset-graph1 (convert-to-dataset-graph fox-and-stork-reif-edn)
        dataset-graph2 (read-dataset-graph-quad fox-and-stork-reif-nquad)
        diff-result (diff dataset-graph1 dataset-graph2)]
    (is (= 0 (count (first diff-result))))
    (is (= 0 (count (second diff-result))))
    (is (= 14 (count (last diff-result))))))

(deftest edn-to-turtle-empty-translate-test
  (let [converted-dataset-graph (convert-to-dataset-graph {})]
    (is (= 0 (count (iterator-seq (.find  converted-dataset-graph)))))
    (is (= "" (write-dataset-graph-quad converted-dataset-graph)))))

(deftest edn-to-turtle-translate-test
  (testing "Wheter an EDN representation can be correctly translated to RDF.")
  (let [converted-dataset-graph (convert-to-dataset-graph fox-and-stork-edn)]
    (is (= 14 (count (iterator-seq (.find  converted-dataset-graph)))))
    (is (string? (write-dataset-graph-quad (convert-to-dataset-graph fox-and-stork-edn))))))
