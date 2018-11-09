(ns aesopica.converter-test
  (:require [clojure.test :refer :all]
            [aesopica.core :as aes :refer :all]
            [aesopica.converter :as conv :refer :all]
            [clojure.spec.alpha :as s]
            [aesopica.examples :refer :all]))

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

(deftest convert-to-model-empty-test
  (is (= [] (. (.  (convert-to-model {}) listStatements) toList))))

(deftest convert-to-model-fox-and-stork-test
  (println (. (. (convert-to-model fox-and-stork-edn) listStatements) toList))
  (is (= 14 (count (. (.  (convert-to-model fox-and-stork-edn) listStatements) toList)))))

(deftest convert-to-model-fox-and-stork-comparison-test
  (let [model1 (convert-to-model fox-and-stork-edn)
        model2 (model-read fox-and-stork-rdf)]

    (is (= "" (model-write (.difference model1 model1))))))

(deftest convert-to-model-fox-and-stork-literals-test
  (is (= 10 (count (. (.  (convert-to-model fox-and-stork-literals-edn) listStatements) toList)))))

(deftest convert-to-model-fox-and-stork-literals-comparison-test
  (let [model1 (convert-to-model fox-and-stork-literals-edn)
        model2 (model-read fox-and-stork-literals-rdf)]
    (is (= "" (model-write (.difference model1 model1))))))

(deftest edn-to-turtle-empty-translate-test
  (let [converted-model (convert-to-model {})]
    (is (= 0 (.size converted-model)))
    (is (= "" (model-write converted-model)))))

(deftest edn-to-turtle-translate-test
  (testing "Wheter an EDN representation can be correctly translated to RDF.")
  (let [converted-model (convert-to-model fox-and-stork-edn)]
    (is (= 14 (.size converted-model)))
    (is (string? (model-write (convert-to-model fox-and-stork-edn))))))
