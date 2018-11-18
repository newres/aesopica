# aesopica

A Clojure library designed to help create Semantic Web, and in particular Linked Data/RDF based applications. 
It allows the user to create Linked Data using idiomatic Clojure datastructures, and translate them to various RDF formats.

## Example Usage


```clojure
(ns example
   (:require [aesopica.core :as aes]
             [aesopica.converter :as conv]))

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
     [:invitation2 :has-invited :fox]
     [:invitation2 :has-food :crumbled-food]
     [:invitation2 :serves-using :narrow-mouthed-jug]
     [:fox :can-eat-food-served-using :shallow-plate]
     [:fox :can-not-eat-food-served-using :narrow-mouthed-jug]
     [:stork :can-eat-food-served-using :narrow-mouthed-jug]
     [:stork :can-not-eat-food-served-using :shallow-plate]}})
  
(conv/write-dataset-graph-turtle (conv/convert-to-dataset-graph fox-and-stork-edn))
```
## Features

### String, integer long and custom Datatypes Supported

```clojure
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
     [:dinner1 :has-date {:value "2002-05-30T18:00:00" :type :xsd/dateTime}]}})
```
### Quads/Named Graphs


```clojure(def fox-and-stork-reif-edn
  {::aes/context
   {nil "http://www.newresalhaider.com/ontologies/aesop/foxstork/"
    :rdf "http://www.w3.org/1999/02/22-rdf-syntax-ns#"}
   ::aes/facts
   #{[:fox :rdf/type :animal :dinner1]
     [:stork :rdf/type :animal :dinner1]
     [:fox :gives-invitation :invitation1 :dinner1]
     [:invitation1 :has-invited :stork :dinner1]
     [:invitation1 :has-food :soup :dinner1]
     [:invitation1 :serves-using :shallow-plate :dinner1]
     [:stork :gives-invitation :invitation2 :dinner2]
     [:invitation2 :has-invited :fox :dinner2]
     [:invitation2 :has-food :crumbled-food :dinner2]
     [:invitation2 :serves-using :narrow-mouthed-jug :dinner2]
     [:fox :can-eat-food-served-using :shallow-plate]
     [:fox :can-not-eat-food-served-using :narrow-mouthed-jug]
     [:stork :can-eat-food-served-using :narrow-mouthed-jug]
     [:stork :can-not-eat-food-served-using :shallow-plate]}})
```

### Conversion to common formats such as Turtle, N-Quads

See the aesopica.converter and tests for examples.

## License

Copyright © 2018 Newres Al Haider

Distributed under the Eclipse Public License (see the LICENSE file). 
