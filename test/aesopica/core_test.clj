(ns aesopica.core-test
  (:require [clojure.test :refer :all]
            [aesopica.core :as aes :refer :all]
            [clojure.spec.alpha :as s]))

(comment "The fox invited the stork to dinner.
          At the dinner soup was served from a shallow plate, that the fox could eat but the hungry stork could not even taste.
          In turn the stork invited the fox to a dinner.
          Dinner was served in a narrow mouthed jug filled with crumbled food.
          This time the fox could not reach the food, while the stork ate.")

(def fox-and-stork-rdf
  "@base <http://www.newresalhaider.com/ontologies/aesop/foxstork> .
  @prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .

  <#fox> rdf:type <#animal>.
  <#stork> rdf:type <#animal>.
  <#fox> <#gives-invitation> <#invitation1>.
  <#invitation1> <#has-invited> <#stork>.
  <#invitation1> <#has-food> <#soup>.
  <#invitation1> <#serves-using> <#shallow-plate>.
  <#stork> <#gives-invitation> <#invitation2>.
  <#invitation2> <#has-invited> <#fox>.
  <#invitation2> <#has-food> <#crumbled-food>.
  <#invitation2> <#serves-using> <#narrow-mouthed-jug>.
  <#fox> <#can-eat-food-served-using> <#shallow-plate>.
  <#fox> <#can-not-eat-food-served-using> <#narrow-mouthed-jug>.
  <#stork> <#can-eat-food-served-using> <#narrow-mouthed-jug>.
  <#stork> <#can-not-eat-food-served-using> <#shallow-plate>.")

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
     [:stork :can-not-eat-food-served-suing :shallow-plate]}})

(deftest valid-empty-context-test
  (let [context {}]
    (is (s/valid? ::aes/context context))))

(deftest valid-context-fox-stork-test
  (let [context (::aes/context fox-and-stork-edn)]
    (is (s/valid? ::aes/context context))))

(deftest valid-facts-fox-stork-test
  (let [facts (::aes/facts fox-and-stork-edn)]
    (is (s/valid? ::aes/facts facts))))

(deftest valid-kb-fox-stork-test
  (let [kg fox-and-stork-edn]
    (is (s/valid? ::aes/knowledge-graph kg))))


(deftest contextualize-base-test
  (let [
        context {nil "http://www.newresalhaider.com/ontologies/aesop/foxstork/"}
        kw :fox]
    (is (= "http://www.newresalhaider.com/ontologies/aesop/foxstork/fox" (contextualize context kw)))
    )
  )

(deftest contextualize-keyword-test
  (let [
        context {:foxstork "http://www.newresalhaider.com/ontologies/aesop/foxstork/"}
        kw :foxstork/fox]
    (is (= "http://www.newresalhaider.com/ontologies/aesop/foxstork/fox" (contextualize context kw)))
    )
  )

(deftest convert-to-resource-test
  (let [
        context {:foxstork "http://www.newresalhaider.com/ontologies/aesop/foxstork/"}
        kw :foxstork/fox]
    (is (=

         (str (contextualize context kw))
         (.getURI (convert-to-resource context kw))))
    )
  )

(deftest convert-to-statement-test
  (let [
        context {:foxstork "http://www.newresalhaider.com/ontologies/aesop/foxstork/"}
        triple [:foxstork/fox :foxstork/gives-invitation :foxstork/invitation1]
]
    (is (= (str "[" (clojure.string/join ", " (map contextualize (repeat context) triple)) "]") (.toString (convert-to-statement context triple))))
    )
  )

;; (deftest convert-to-statement-no-context-test
;;   (let [fact [:fox :gives-invitation :invitation1]]
;;     (println (convert-to-statement nil fact))
;;     )
;;   )

(deftest edn-to-turtle-empty-translate-test
  (is (= "" (convert-to-rdf {}))))

;; (deftest edn-to-turtle-translate-test
;;   (testing "Wheter an EDN representation can be correctly translated to RDF.")
;;   (is (= fox-and-stork-rdf (convert-to-rdf fox-and-stork-edn))))
