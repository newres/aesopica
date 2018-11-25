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
   (org.apache.jena.rdf.model AnonId)
   (org.apache.jena.rdf.model.impl StatementImpl)
   (org.apache.jena.rdf.model.impl ResourceImpl)
   (org.apache.jena.graph NodeFactory)
   (org.apache.jena.sparql.core Quad)
   (org.apache.jena.query DatasetFactory)
   (org.apache.jena.sparql.core DatasetGraphFactory)
   (org.apache.jena.sparql.core.mem DatasetGraphInMemory)
   (org.apache.jena.datatypes BaseDatatype))
  (:require
   [aesopica.core :as core]
   [clojure.spec.alpha :as s]))

(s/def ::triple-lang #{Lang/JSONLD Lang/TURTLE Lang/RDFXML})
(s/def ::quad-lang #{Lang/NQUADS Lang/TRIG})
(s/def ::lang (s/or ::triple-land ::triple-lang ::quad-lang ::quad-lang))

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

(defn convert-to-blank-node [context symbol]
  (new ResourceImpl (new AnonId (str symbol))))

(defmulti convert-to-subject (fn [context element] (cond (s/valid? ::core/blank-node element) ::core/blank-node :else :default)))

(defmethod convert-to-subject ::core/blank-node [context element]
  (convert-to-blank-node context element))

(defmethod convert-to-subject :default [context element]
  (convert-to-resource context element))

(defn convert-to-object [context element]
  (cond (keyword? element) (convert-to-resource context element)
        (s/valid? ::core/blank-node element) (convert-to-blank-node context element)
        :else (convert-to-literal context element)))

(defn convert-to-statement [context triple]
  (let [subject-resource (convert-to-subject context (nth triple 0))
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
  "Takes and EDN representation of a knowledge graph and converts it to a DataSetGraph.
  This format is chosen by default in order to quads/multiple graphs could be represented."
  [edn]
  (let [facts (::core/facts edn)
        context (::core/context edn)
        quads (map convert-to-quad (repeat context) facts)]
    (let [dataset-graph (DatasetGraphFactory/create)]
      (doseq [quad quads]
        (.add dataset-graph quad))
      dataset-graph)))

(defn write-dataset-graph
  "Write a dataset graph in the specified language as a string."
  [writable lang]
  (let [syntax lang
        out (java.io.StringWriter.)]
    (RDFDataMgr/write out writable syntax)
    (.toString out)))

(defn write-dataset-graph-trig
  "Writes a dataset-graph to a TRIG string."
  [writable]
  ;; There seems to be an issue with the default TriGWriter so a lower level function method is used.
  (let [syntax Lang/TRIG
        out (java.io.StringWriter.)]
    (.write (new org.apache.jena.riot.writer.TriGWriterBlocks) out writable nil nil org.apache.jena.sparql.util.Context/emptyContext)
    (.toString out)))

(defn write-dataset-graph-turtle
  "Writes a dataset-graph to a TURTLE string. Note that this format is not designed for named graphs/quads. In this implementation, we do a union of the default and named graphs, but this is lossy when there are named graphs/quads are present. For a loss-less format, use a format designed for named-graphs: such as N-Quads or TriG."
  [writable]
  (write-dataset-graph (new org.apache.jena.graph.compose.Union (.getDefaultGraph writable) (.getUnionGraph writable)) Lang/TURTLE))

(defn write-dataset-graph-nquads
  "Writes a dataset-graph to an NQUADS string."
  [writable]
  (write-dataset-graph writable Lang/NQUADS))

(defn read-dataset-graph
  "Reads a dataset from a given format string."
  [dataset-string format]
  (let [syntax format
        in (java.io.StringReader. dataset-string)
        dataset-graph (DatasetGraphFactory/create)]
    (.parse (.lang (.source (RDFParser/create) in) syntax) dataset-graph)
    dataset-graph))

(defn read-dataset-graph-turtle
  "Reads a dataset from a TURTLE format string."
  [dataset-string]
  (read-dataset-graph dataset-string Lang/TURTLE))

(defn read-dataset-graph-trig
  "Reads a dataset from a TRIG format string."
  [dataset-string]
  (read-dataset-graph dataset-string Lang/TRIG))

(defn read-dataset-graph-nquads
  "Reads a dataset from an NQUADS format string."
  [dataset-string]
  (read-dataset-graph dataset-string Lang/NQUADS))
