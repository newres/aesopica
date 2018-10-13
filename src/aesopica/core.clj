(ns aesopica.core
  (:import
   (org.apache.jena.riot Lang)
   (org.apache.jena.riot RDFDataMgr)
   (org.apache.jena.rdf.model)
   (org.apache.jena.rdf.model ResourceFactory)
   (org.apache.jena.rdf.model ModelFactory)
   (org.apache.jena.rdf.model Statement)
   (org.apache.jena.rdf.model.impl StatementImpl)
   (org.apache.jena.datatypes BaseDatatype)
   )
  (:require [clojure.spec.alpha :as s]))

(s/def ::context (s/map-of (s/or :keyword keyword? :nil nil?) string?))
(s/def ::custom-literal (s/keys :req-un [::value ::type]))
(s/def ::object (s/or :keyword keyword? :string string? :number number? :boolean boolean? :custom-literal ::custom-literal))
(s/def ::triple (s/cat :subject keyword? :predicate keyword? :object ::object))
(s/def ::facts (s/coll-of ::triple :kind set?))
(s/def ::knowledge-graph (s/keys :req [::context ::facts]))

(defn contextualize [context kw]
  (let [ns (namespace kw)
        prefix (if (= ns "") (get context nil)
                   (get context (keyword ns)))]
    (str prefix (name kw))))

(defmulti convert-to-literal (fn [context literal] (cond (map? literal) :custom-type :else :default )))

(defmethod convert-to-literal :custom-type [context literal-map]
  (ResourceFactory/createTypedLiteral (:value literal-map) (new BaseDatatype (contextualize context (:type literal-map))))
  )

(defmethod convert-to-literal :default [context literal]
  (ResourceFactory/createTypedLiteral literal)
  )
;; (defn convert-to-literal [literal]
;;   (ResourceFactory/createTypedLiteral literal)
;;   )

(defn convert-to-resource [context kw]
  (ResourceFactory/createResource (contextualize context kw)))

(defn convert-to-property [context kw]
  (ResourceFactory/createProperty (contextualize context kw)))

(defn convert-to-object [context element]
  (if (keyword? element)
    (convert-to-resource context element)
    (convert-to-literal context element)
    )
  )

(defn convert-to-statement [context triple]
  (let [subject-resource (convert-to-resource context (nth triple 0))
        predicate-property (convert-to-property context (nth triple 1))
        object-resource (convert-to-object context (nth triple 2))]
    (ResourceFactory/createStatement subject-resource predicate-property object-resource)))

(defn convert-to-model
  "Takes and EDN representation of a knowledge graph and coverts it to rdf."
  [edn]
  (let [facts (::facts edn)
        context (::context edn)
        statements (map convert-to-statement (repeat context) facts)]
    (. (ModelFactory/createDefaultModel) add statements)))

(defn model-write
  "Writes the model to a string."
  [model]
  (let [syntax (Lang/TURTLE)
        out (java.io.StringWriter.)]
    (RDFDataMgr/write out model syntax)
    (.toString out)))

