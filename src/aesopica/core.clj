(ns aesopica.core
  (:import
   (org.apache.jena.rdf.model)
   (org.apache.jena.rdf.model ResourceFactory)
   (org.apache.jena.rdf.model Statement)
   (org.apache.jena.rdf.model.impl StatementImpl))
  (:require [clojure.spec.alpha :as s]))

(s/def ::context (s/map-of (s/or :keyword keyword? :nil nil?) string?))
(s/def ::triple (s/coll-of keyword? :kind vector? :count 3))
(s/def ::facts (s/coll-of ::triple :kind set?))
(s/def ::knowledge-graph (s/keys :req [::context ::facts]))

(defn keyword-to-iri
  [keyword]
  (str keyword))

(defn contextualize [context kw]
  (let [ns (namespace kw)
        prefix (if (= ns "") (get context nil)
                   (get context (keyword ns)))]
    (str prefix (name kw))))

(defn convert-to-resource [context kw]
  (ResourceFactory/createResource (contextualize context kw)))

(defn convert-to-property [context kw]
  (ResourceFactory/createProperty (contextualize context kw)))

(defn convert-to-statement [context triple]
  (let [subject-resource (convert-to-resource context (nth triple 0))
        predicate-property (convert-to-property context (nth triple 1))
        object-resource (convert-to-resource context (nth triple 2))]
    (ResourceFactory/createStatement subject-resource predicate-property object-resource)))

;; (defn create-statement [sub pre obj]
;;   (new StatementImpl sub pre obj ))


;; (defn iri?
;;   "Checks whether the given value is an IRI."
;;   [x]
;;   (.)(.create IRIFactory)
;;   )

(defn convert-to-rdf
  "Takes and EDN representation of a knowledge graph and coverts it to rdf."
  [edn]
  (if (= {} edn) ""
      "@base <http://www.newresalhaider.com/ontologies/aesop/foxstork> .
  @prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .

  <#fox> a <#animal>.
  <#stork> a <#animal>.
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
  <#stork> <#can-not-eat-food-served-using> <#shallow-plate>."))

(defn foo
  "I don't do a whole lot."
  [x]
  (println x "Hello, World!"))
