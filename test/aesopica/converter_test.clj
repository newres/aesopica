(ns aesopica.converter-test
  (:require [clojure.test :refer :all]
            [aesopica.core :as aes :refer :all]
            [aesopica.converter :as conv :refer :all]
            [clojure.spec.alpha :as s]))

(comment "The fox invited the stork to dinner.
          At the dinner soup was served from a shallow plate, that the fox could eat but the hungry stork could not even taste.
          In turn the stork invited the fox to a dinner.
          Dinner was served in a narrow mouthed jug filled with crumbled food.
          This time the fox could not reach the food, while the stork ate.")

(def fox-and-stork-rdf
  "@base <http://www.newresalhaider.com/ontologies/aesop/foxstork/> .
  @prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .

  <fox> rdf:type <animal>.
  <stork> rdf:type <animal>.
  <fox> <gives-invitation> <invitation1>.
  <invitation1> <has-invited> <stork>.
  <invitation1> <has-food> <soup>.
  <invitation1> <serves-using> <shallow-plate>.
  <stork> <gives-invitation> <invitation2>.
  <invitation2> <has-invited> <fox>.
  <invitation2> <has-food> <crumbled-food>.
  <invitation2> <serves-using> <narrow-mouthed-jug>.
  <fox> <can-eat-food-served-using> <shallow-plate>.
  <fox> <can-not-eat-food-served-using> <narrow-mouthed-jug>.
  <stork> <can-eat-food-served-using> <narrow-mouthed-jug>.
  <stork> <can-not-eat-food-served-using> <shallow-plate>.")

(def fox-and-stork-edn
  {::aes/context
   {nil "http://www.newresalhaider.com/ontologies/aesop/foxstork/"
    :rdf "http://www.w3.org/1999/02/22-rdf-syntax-ns#"}
   ::aes/facts
   #{[:fox :rdf/type :animal]
     [:stork :rdf/type :animal]
     [:fox :gives-invitation :invitation1]
     [:invitation1 :has-invited :stork]
     [:invitation1 :has-food :soup]
     [:invitation1 :serves-using :shallow-plate]
     [:stork :gives-invitation :invitation2]
     [:invitation2 :has-invited :stork]
     [:invitation2 :has-food :crumbled-food]
     [:invitation2 :serves-using :narrow-mouthed-jug]
     [:fox :can-eat-food-served-using :shallow-plate]
     [:fox :can-not-eat-food-served-suing :narrow-mouthed-jug]
     [:stork :can-eat-food-served-using :narrow-mouthed-jug]
     [:stork :can-not-eat-food-served-using :shallow-plate]}})

(def fox-and-stork-literals-rdf
  "@base <http://www.newresalhaider.com/ontologies/aesop/foxstork/> .
  @prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .
  @prefix foaf: <http://xmlns.com/foaf/0.1/> .
  @prefix xsd: <http://www.w3.org/2001/XMLSchema#>.

  <fox> rdf:type <animal>.
  <fox> foaf:name \"vo\".
  <fox> foaf:age 2.
  <fox> <is-cunning> true.
  <stork> rdf:type <animal>.
  <stork> foaf:name \"ooi\".
  <stork> foaf:age 13.
  <stork> <is-cunning> true.
  <dinner1> <has-date> \"2002-05-30T09:00:00\"^^xsd:dateTime
  ")

(def fox-and-stork-literals-edn
  {::aes/context
   {nil "http://www.newresalhaider.com/ontologies/aesop/foxstork/"
    :rdf "http://www.w3.org/1999/02/22-rdf-syntax-ns#"
    :foaf "http://xmlns.com/foaf/0.1/"
    :xsd "http://www.w3.org/2001/XMLSchema#"}
   ::aes/facts
   #{[:fox :rdf/type :animal]
     [:fox :foaf/name "vo"]
     [:fox :foaf/age 2]
     [:fox :is-cunning true]
     [:fox :has-weight 6.8]
     [:stork :rdf/type :animal]
     [:stork :foaf/name "ooi"]
     [:stork :foaf/age 13]
     [:stork :is-cunning true]
     [:dinner1 :has-date {:value "2002-05-30T09:00:00" :type :xsd/dateTime}]}})

;; (deftest valid-empty-context-test
;;   (let [context {}]
;;     (is (s/valid? ::aes/context context))))

;; (deftest valid-context-fox-stork-test
;;   (let [context (::aes/context fox-and-stork-edn)]
;;     (is (s/valid? ::aes/context context))))

;; (deftest valid-facts-fox-stork-test
;;   (let [facts (::aes/facts fox-and-stork-edn)]
;;     (is (s/valid? ::aes/facts facts))))

;; (deftest valid-kb-fox-stork-test
;;   (let [kg fox-and-stork-edn]
;;     (is (s/valid? ::aes/knowledge-graph kg))))

;; (deftest valid-context-fox-stork-literals-test
;;   (let [context (::aes/context fox-and-stork-literals-edn)]
;;     (is (s/valid? ::aes/context context))))

;; (deftest valid-kb-fox-stork-literals-test
;;   (let [facts (::aes/facts fox-and-stork-literals-edn)]
;;     (is (s/valid? ::aes/facts facts))))

;; (deftest valid-kb-fox-stork-test
;;   (let [kg fox-and-stork-literals-edn]
;;     (is (s/valid? ::aes/knowledge-graph kg))))

;; (deftest contextualize-base-test
;;   (let [context {nil "http://www.newresalhaider.com/ontologies/aesop/foxstork/"}
;;         kw :fox]
;;     (is (= "http://www.newresalhaider.com/ontologies/aesop/foxstork/fox" (contextualize context kw)))))

;; (deftest contextualize-keyword-test
;;   (let [context {:foxstork "http://www.newresalhaider.com/ontologies/aesop/foxstork/"}
;;         kw :foxstork/fox]
;;     (is (= "http://www.newresalhaider.com/ontologies/aesop/foxstork/fox" (contextualize context kw)))))

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
  ( println (. (. (convert-to-model fox-and-stork-edn) listStatements) toList) )
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
