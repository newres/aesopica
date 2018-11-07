(ns aesopica.converter
  (:import
   (org.apache.jena.riot Lang)
   (org.apache.jena.riot RDFDataMgr)
   (org.apache.jena.riot RDFParser)
   (org.apache.jena.riot RDFParserBuilder)
   (org.apache.jena.rdf.model)
   (org.apache.jena.rdf.model ResourceFactory)
   (org.apache.jena.rdf.model ModelFactory)
   (org.apache.jena.rdf.model Statement)
   (org.apache.jena.rdf.model.impl StatementImpl)
   (org.apache.jena.datatypes BaseDatatype))
  (:require
   [aesopica.core :as core]
   [clojure.spec.alpha :as s]))

(defmulti convert-to-literal (fn [context literal] (cond (map? literal) :custom-type :else :default)))

(defmethod convert-to-literal :custom-type [context literal-map]
  (ResourceFactory/createTypedLiteral (:value literal-map) (new BaseDatatype (core/contextualize context (:type literal-map)))))

(defmethod convert-to-literal :default [context literal]
  (ResourceFactory/createTypedLiteral literal))

(defn convert-to-resource [context kw]
  (ResourceFactory/createResource (core/contextualize context kw)))

(defn convert-to-property [context kw]
  (ResourceFactory/createProperty (core/contextualize context kw)))

(defn convert-to-object [context element]
  (if (keyword? element)
    (convert-to-resource context element)
    (convert-to-literal context element)))

(defn convert-to-statement [context triple]
  (let [subject-resource (convert-to-resource context (nth triple 0))
        predicate-property (convert-to-property context (nth triple 1))
        object-resource (convert-to-object context (nth triple 2))]
    (ResourceFactory/createStatement subject-resource predicate-property object-resource)))

(defn convert-to-model
  "Takes and EDN representation of a knowledge graph and converts it to rdf."
  [edn]
  (let [facts (::core/facts edn)
        context (::core/context edn)
        statements (map convert-to-statement (repeat context) facts)]
    (. (ModelFactory/createDefaultModel) add statements)))

(defn model-write
  "Writes the model to a string."
  [model]
  (let [syntax (Lang/TURTLE)
        out (java.io.StringWriter.)]
    (RDFDataMgr/write out model syntax)
    (.toString out)))

(defn model-read
  "Reads a model from a string."
  [model-string]
  (let [syntax (Lang/TURTLE)
        in (java.io.StringReader. model-string)
        model (ModelFactory/createDefaultModel)]
    (.parse (.lang (.source (RDFParser/create) in) syntax) model)
    model))

