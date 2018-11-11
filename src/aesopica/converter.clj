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
   (org.apache.jena.graph NodeFactory)
   (org.apache.jena.sparql.core Quad) (org.apache.jena.sparql.core DatasetGraphFactory)
   (org.apache.jena.sparql.core.mem DatasetGraphInMemory)
   (org.apache.jena.datatypes BaseDatatype))
  (:require
   [aesopica.core :as core]
   [clojure.spec.alpha :as s]))

(s/def graph (s/or :uri string? :nil nil))
(s/def statement #(instance? Statement %))
(s/def graph-statement (s/cat :graph ::graph :statement ::statement))

(defmulti convert-to-literal (fn [context literal] (cond (map? literal) :custom-type :else :default)))

(defmethod convert-to-literal :custom-type [context literal-map]
  (ResourceFactory/createTypedLiteral (:value literal-map) (new BaseDatatype (core/contextualize context (:type literal-map)))))

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

(defmulti convert-to-quad (fn [context fact] (cond (s/valid? ::core/quad fact) :quad :else :triple)))

(defmethod convert-to-quad :quad [context quad]
  (let [triple (.asTriple (convert-to-statement context (take 3 quad)))]
    (new Quad (NodeFactory/createURI (core/contextualize context (last quad))) triple)))

(defmethod convert-to-quad :triple [context triple]
  (new Quad nil (.asTriple (convert-to-statement context triple))))

(defn convert-to-dataset-graph
  "Takes and EDN representation of a knowledge graph and converts it to a DataSetGraph."
  [edn]
  (let [facts (::core/facts edn)
        context (::core/context edn)
        quads (map convert-to-quad (repeat context) facts)]
    (let [dataset-graph (DatasetGraphFactory/create)]
      (doseq [quad quads]
        (.add dataset-graph quad))
      dataset-graph)))

(defn write-dataset-graph
  "Writes a dataset-graph."
  [writeable]
  (let [syntax (Lang/TURTLE)
        out (java.io.StringWriter.)]
    (RDFDataMgr/write out writeable syntax)
    (.toString out)))

(defn write-dataset-graph-quad
  "Writes a dataset-graph."
  [writeable]
  (let [syntax (Lang/NQUADS)
        out (java.io.StringWriter.)]
    (RDFDataMgr/write out writeable syntax)
    (.toString out)))

(defn read-dataset-graph
  "Reads a dataset from a string."
  [dataset-string]
  (let [syntax (Lang/TURTLE)
        in (java.io.StringReader. dataset-string)
        dataset-graph (DatasetGraphFactory/create)]
    (.parse (.lang (.source (RDFParser/create) in) syntax) dataset-graph)
    dataset-graph))

(defn read-dataset-graph-quad
  "Reads a dataset from a string."
  [dataset-string]
  (let [syntax (Lang/NQUADS)
        in (java.io.StringReader. dataset-string)
        dataset-graph (DatasetGraphFactory/create)]
    (.parse (.lang (.source (RDFParser/create) in) syntax) dataset-graph)
    dataset-graph))
