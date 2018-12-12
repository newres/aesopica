(ns aesopica.examples
  (:require
   [aesopica.core :as aes :refer :all]))

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
     [:invitation2 :has-invited :fox]
     [:invitation2 :has-food :crumbled-food]
     [:invitation2 :serves-using :narrow-mouthed-jug]
     [:fox :can-eat-food-served-using :shallow-plate]
     [:fox :can-not-eat-food-served-using :narrow-mouthed-jug]
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
  <dinner1> <has-date> \"2002-05-30T18:00:00\"^^xsd:dateTime
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
     [:dinner1 :has-date {:value "2002-05-30T18:00:00" :type :xsd/dateTime}]}})

(def fox-and-stork-reif-edn
  {::aes/context
   {nil "http://www.newresalhaider.com/ontologies/aesop/foxstork/"
    :rdf "http://www.w3.org/1999/02/22-rdf-syntax-ns#"}
   ::aes/facts
   #{[:fox :rdf/type :animal]
     [:stork :rdf/type :animal]
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

(def fox-and-stork-reif-nquad

  "<http://www.newresalhaider.com/ontologies/aesop/foxstork/fox> <http://www.newresalhaider.com/ontologies/aesop/foxstork/can-eat-food-served-using> <http://www.newresalhaider.com/ontologies/aesop/foxstork/shallow-plate> .
<http://www.newresalhaider.com/ontologies/aesop/foxstork/fox> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.newresalhaider.com/ontologies/aesop/foxstork/animal> .
<http://www.newresalhaider.com/ontologies/aesop/foxstork/fox> <http://www.newresalhaider.com/ontologies/aesop/foxstork/can-not-eat-food-served-using> <http://www.newresalhaider.com/ontologies/aesop/foxstork/narrow-mouthed-jug> .
<http://www.newresalhaider.com/ontologies/aesop/foxstork/stork> <http://www.newresalhaider.com/ontologies/aesop/foxstork/can-not-eat-food-served-using> <http://www.newresalhaider.com/ontologies/aesop/foxstork/shallow-plate> .
<http://www.newresalhaider.com/ontologies/aesop/foxstork/stork> <http://www.newresalhaider.com/ontologies/aesop/foxstork/can-eat-food-served-using> <http://www.newresalhaider.com/ontologies/aesop/foxstork/narrow-mouthed-jug> .
<http://www.newresalhaider.com/ontologies/aesop/foxstork/stork> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.newresalhaider.com/ontologies/aesop/foxstork/animal> .
<http://www.newresalhaider.com/ontologies/aesop/foxstork/invitation2> <http://www.newresalhaider.com/ontologies/aesop/foxstork/serves-using> <http://www.newresalhaider.com/ontologies/aesop/foxstork/narrow-mouthed-jug> <http://www.newresalhaider.com/ontologies/aesop/foxstork/dinner2> .
<http://www.newresalhaider.com/ontologies/aesop/foxstork/invitation2> <http://www.newresalhaider.com/ontologies/aesop/foxstork/has-food> <http://www.newresalhaider.com/ontologies/aesop/foxstork/crumbled-food> <http://www.newresalhaider.com/ontologies/aesop/foxstork/dinner2> .
<http://www.newresalhaider.com/ontologies/aesop/foxstork/invitation2> <http://www.newresalhaider.com/ontologies/aesop/foxstork/has-invited> <http://www.newresalhaider.com/ontologies/aesop/foxstork/fox> <http://www.newresalhaider.com/ontologies/aesop/foxstork/dinner2> .
<http://www.newresalhaider.com/ontologies/aesop/foxstork/stork> <http://www.newresalhaider.com/ontologies/aesop/foxstork/gives-invitation> <http://www.newresalhaider.com/ontologies/aesop/foxstork/invitation2> <http://www.newresalhaider.com/ontologies/aesop/foxstork/dinner2> .
<http://www.newresalhaider.com/ontologies/aesop/foxstork/fox> <http://www.newresalhaider.com/ontologies/aesop/foxstork/gives-invitation> <http://www.newresalhaider.com/ontologies/aesop/foxstork/invitation1> <http://www.newresalhaider.com/ontologies/aesop/foxstork/dinner1> .
<http://www.newresalhaider.com/ontologies/aesop/foxstork/invitation1> <http://www.newresalhaider.com/ontologies/aesop/foxstork/has-food> <http://www.newresalhaider.com/ontologies/aesop/foxstork/soup> <http://www.newresalhaider.com/ontologies/aesop/foxstork/dinner1> .
<http://www.newresalhaider.com/ontologies/aesop/foxstork/invitation1> <http://www.newresalhaider.com/ontologies/aesop/foxstork/serves-using> <http://www.newresalhaider.com/ontologies/aesop/foxstork/shallow-plate> <http://www.newresalhaider.com/ontologies/aesop/foxstork/dinner1> .
<http://www.newresalhaider.com/ontologies/aesop/foxstork/invitation1> <http://www.newresalhaider.com/ontologies/aesop/foxstork/has-invited> <http://www.newresalhaider.com/ontologies/aesop/foxstork/stork> <http://www.newresalhaider.com/ontologies/aesop/foxstork/dinner1> .")

(def fox-and-stork-named-graph-edn
  {::aes/context
   {nil "http://www.newresalhaider.com/ontologies/aesop/foxstork/"
    :rdf "http://www.w3.org/1999/02/22-rdf-syntax-ns#"
    :time "http://www.w3.org/2006/time#"}
   ::aes/facts
   #{[:fox :rdf/type :animal]
     [:stork :rdf/type :animal]
     [:fox :gives-invitation :invitation1 :dinner1]
     [:invitation1 :has-invited :stork :dinner1]
     [:invitation1 :has-food :soup :dinner1]
     [:invitation1 :serves-using :shallow-plate :dinner1]
     [:stork :gives-invitation :invitation2 :dinner2]
     [:invitation2 :has-invited :fox :dinner2]
     [:invitation2 :has-food :crumbled-food :dinner2]
     [:invitation2 :serves-using :narrow-mouthed-jug :dinner2]
     [:invitation1 :serves-using :narrow-mouthed-jug :dinner2]
     [:dinner1 :time/before :dinner2]}})

(def fox-and-stork-named-graph-trig

  "{ <http://www.newresalhaider.com/ontologies/aesop/foxstork/fox>
          <http://www.w3.org/1999/02/22-rdf-syntax-ns#type>  <http://www.newresalhaider.com/ontologies/aesop/foxstork/animal> .
  <http://www.newresalhaider.com/ontologies/aesop/foxstork/dinner1>
          <http://www.w3.org/2006/time#before>  <http://www.newresalhaider.com/ontologies/aesop/foxstork/dinner2> .
  <http://www.newresalhaider.com/ontologies/aesop/foxstork/stork>
          <http://www.w3.org/1999/02/22-rdf-syntax-ns#type>  <http://www.newresalhaider.com/ontologies/aesop/foxstork/animal> .
}

<http://www.newresalhaider.com/ontologies/aesop/foxstork/dinner2> {
    <http://www.newresalhaider.com/ontologies/aesop/foxstork/invitation2>
            <http://www.newresalhaider.com/ontologies/aesop/foxstork/serves-using>  <http://www.newresalhaider.com/ontologies/aesop/foxstork/narrow-mouthed-jug> ;
            <http://www.newresalhaider.com/ontologies/aesop/foxstork/has-food>  <http://www.newresalhaider.com/ontologies/aesop/foxstork/crumbled-food> ;
            <http://www.newresalhaider.com/ontologies/aesop/foxstork/has-invited>  <http://www.newresalhaider.com/ontologies/aesop/foxstork/fox> .
    <http://www.newresalhaider.com/ontologies/aesop/foxstork/stork>
            <http://www.newresalhaider.com/ontologies/aesop/foxstork/gives-invitation>  <http://www.newresalhaider.com/ontologies/aesop/foxstork/invitation2> .
    <http://www.newresalhaider.com/ontologies/aesop/foxstork/invitation1>
            <http://www.newresalhaider.com/ontologies/aesop/foxstork/serves-using>  <http://www.newresalhaider.com/ontologies/aesop/foxstork/narrow-mouthed-jug> .
}

<http://www.newresalhaider.com/ontologies/aesop/foxstork/dinner1> {
    <http://www.newresalhaider.com/ontologies/aesop/foxstork/fox>
            <http://www.newresalhaider.com/ontologies/aesop/foxstork/gives-invitation>  <http://www.newresalhaider.com/ontologies/aesop/foxstork/invitation1> .
    <http://www.newresalhaider.com/ontologies/aesop/foxstork/invitation1>
            <http://www.newresalhaider.com/ontologies/aesop/foxstork/has-food>  <http://www.newresalhaider.com/ontologies/aesop/foxstork/soup> ;
            <http://www.newresalhaider.com/ontologies/aesop/foxstork/serves-using>  <http://www.newresalhaider.com/ontologies/aesop/foxstork/shallow-plate> ;
            <http://www.newresalhaider.com/ontologies/aesop/foxstork/has-invited>  <http://www.newresalhaider.com/ontologies/aesop/foxstork/stork> .
}")

(def fox-and-stork-named-graph-nquads
  "
<http://www.newresalhaider.com/ontologies/aesop/foxstork/fox> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.newresalhaider.com/ontologies/aesop/foxstork/animal> .
<http://www.newresalhaider.com/ontologies/aesop/foxstork/dinner1> <http://www.w3.org/2006/time#before> <http://www.newresalhaider.com/ontologies/aesop/foxstork/dinner2> .
<http://www.newresalhaider.com/ontologies/aesop/foxstork/stork> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.newresalhaider.com/ontologies/aesop/foxstork/animal> .
<http://www.newresalhaider.com/ontologies/aesop/foxstork/invitation2> <http://www.newresalhaider.com/ontologies/aesop/foxstork/serves-using> <http://www.newresalhaider.com/ontologies/aesop/foxstork/narrow-mouthed-jug> <http://www.newresalhaider.com/ontologies/aesop/foxstork/dinner2> .
<http://www.newresalhaider.com/ontologies/aesop/foxstork/invitation2> <http://www.newresalhaider.com/ontologies/aesop/foxstork/has-food> <http://www.newresalhaider.com/ontologies/aesop/foxstork/crumbled-food> <http://www.newresalhaider.com/ontologies/aesop/foxstork/dinner2> .
<http://www.newresalhaider.com/ontologies/aesop/foxstork/invitation2> <http://www.newresalhaider.com/ontologies/aesop/foxstork/has-invited> <http://www.newresalhaider.com/ontologies/aesop/foxstork/fox> <http://www.newresalhaider.com/ontologies/aesop/foxstork/dinner2> .
<http://www.newresalhaider.com/ontologies/aesop/foxstork/stork> <http://www.newresalhaider.com/ontologies/aesop/foxstork/gives-invitation> <http://www.newresalhaider.com/ontologies/aesop/foxstork/invitation2> <http://www.newresalhaider.com/ontologies/aesop/foxstork/dinner2> .
<http://www.newresalhaider.com/ontologies/aesop/foxstork/invitation1> <http://www.newresalhaider.com/ontologies/aesop/foxstork/serves-using> <http://www.newresalhaider.com/ontologies/aesop/foxstork/narrow-mouthed-jug> <http://www.newresalhaider.com/ontologies/aesop/foxstork/dinner2> .
<http://www.newresalhaider.com/ontologies/aesop/foxstork/fox> <http://www.newresalhaider.com/ontologies/aesop/foxstork/gives-invitation> <http://www.newresalhaider.com/ontologies/aesop/foxstork/invitation1> <http://www.newresalhaider.com/ontologies/aesop/foxstork/dinner1> .
<http://www.newresalhaider.com/ontologies/aesop/foxstork/invitation1> <http://www.newresalhaider.com/ontologies/aesop/foxstork/has-food> <http://www.newresalhaider.com/ontologies/aesop/foxstork/soup> <http://www.newresalhaider.com/ontologies/aesop/foxstork/dinner1> .
<http://www.newresalhaider.com/ontologies/aesop/foxstork/invitation1> <http://www.newresalhaider.com/ontologies/aesop/foxstork/serves-using> <http://www.newresalhaider.com/ontologies/aesop/foxstork/shallow-plate> <http://www.newresalhaider.com/ontologies/aesop/foxstork/dinner1> .
<http://www.newresalhaider.com/ontologies/aesop/foxstork/invitation1> <http://www.newresalhaider.com/ontologies/aesop/foxstork/has-invited> <http://www.newresalhaider.com/ontologies/aesop/foxstork/stork> <http://www.newresalhaider.com/ontologies/aesop/foxstork/dinner1> .
")

(def fox-and-stork-named-graph-json-ld
  "{
  \"@graph\" : [ {
    \"@graph\" : [ {
      \"@id\" : \"http://www.newresalhaider.com/ontologies/aesop/foxstork/fox\",
      \"http://www.newresalhaider.com/ontologies/aesop/foxstork/gives-invitation\" : {
        \"@id\" : \"http://www.newresalhaider.com/ontologies/aesop/foxstork/invitation1\"
      }
    }, {
      \"@id\" : \"http://www.newresalhaider.com/ontologies/aesop/foxstork/invitation1\",
      \"http://www.newresalhaider.com/ontologies/aesop/foxstork/has-food\" : {
        \"@id\" : \"http://www.newresalhaider.com/ontologies/aesop/foxstork/soup\"
      },
      \"http://www.newresalhaider.com/ontologies/aesop/foxstork/has-invited\" : {
        \"@id\" : \"http://www.newresalhaider.com/ontologies/aesop/foxstork/stork\"
      },
      \"http://www.newresalhaider.com/ontologies/aesop/foxstork/serves-using\" : {
        \"@id\" : \"http://www.newresalhaider.com/ontologies/aesop/foxstork/shallow-plate\"
      }
    } ],
    \"@id\" : \"http://www.newresalhaider.com/ontologies/aesop/foxstork/dinner1\",
    \"http://www.w3.org/2006/time#before\" : {
      \"@id\" : \"http://www.newresalhaider.com/ontologies/aesop/foxstork/dinner2\"
    }
  }, {
    \"@graph\" : [ {
      \"@id\" : \"http://www.newresalhaider.com/ontologies/aesop/foxstork/invitation1\",
      \"http://www.newresalhaider.com/ontologies/aesop/foxstork/serves-using\" : {
        \"@id\" : \"http://www.newresalhaider.com/ontologies/aesop/foxstork/narrow-mouthed-jug\"
      }
    }, {
      \"@id\" : \"http://www.newresalhaider.com/ontologies/aesop/foxstork/invitation2\",
      \"http://www.newresalhaider.com/ontologies/aesop/foxstork/has-food\" : {
        \"@id\" : \"http://www.newresalhaider.com/ontologies/aesop/foxstork/crumbled-food\"
      },
      \"http://www.newresalhaider.com/ontologies/aesop/foxstork/has-invited\" : {
        \"@id\" : \"http://www.newresalhaider.com/ontologies/aesop/foxstork/fox\"
      },
      \"http://www.newresalhaider.com/ontologies/aesop/foxstork/serves-using\" : {
        \"@id\" : \"http://www.newresalhaider.com/ontologies/aesop/foxstork/narrow-mouthed-jug\"
      }
    }, {
      \"@id\" : \"http://www.newresalhaider.com/ontologies/aesop/foxstork/stork\",
      \"http://www.newresalhaider.com/ontologies/aesop/foxstork/gives-invitation\" : {
        \"@id\" : \"http://www.newresalhaider.com/ontologies/aesop/foxstork/invitation2\"
      }
    } ],
    \"@id\" : \"http://www.newresalhaider.com/ontologies/aesop/foxstork/dinner2\"
  }, {
    \"@id\" : \"http://www.newresalhaider.com/ontologies/aesop/foxstork/fox\",
    \"@type\" : \"http://www.newresalhaider.com/ontologies/aesop/foxstork/animal\"
  }, {
    \"@id\" : \"http://www.newresalhaider.com/ontologies/aesop/foxstork/stork\",
    \"@type\" : \"http://www.newresalhaider.com/ontologies/aesop/foxstork/animal\"
  } ]
}")

(def fox-and-stork-blank-node-edn
  {::aes/context
   {nil "http://www.newresalhaider.com/ontologies/aesop/foxstork/"
    :rdf "http://www.w3.org/1999/02/22-rdf-syntax-ns#"}
   ::aes/facts
   #{[:fox :rdf/type :animal]
     [:stork :rdf/type :animal]
     [:fox :gives-invitation 'invitation1]
     ['invitation1 :has-invited :stork]
     ['invitation1 :has-food :soup]
     ['invitation1 :serves-using :shallow-plate]
     [:stork :gives-invitation 'invitation2]
     ['invitation2 :has-invited :fox]
     ['invitation2 :has-food :crumbled-food]
     ['invitation2 :serves-using :narrow-mouthed-jug]
     [:fox :can-eat-food-served-using :shallow-plate]
     [:fox :can-not-eat-food-served-using :narrow-mouthed-jug]
     [:stork :can-eat-food-served-using :narrow-mouthed-jug]
     [:stork :can-not-eat-food-served-using :shallow-plate]}})

(def fox-and-stork-blank-node-turtle
  "<http://www.newresalhaider.com/ontologies/aesop/foxstork/fox>
        a       <http://www.newresalhaider.com/ontologies/aesop/foxstork/animal> ;
        <http://www.newresalhaider.com/ontologies/aesop/foxstork/can-eat-food-served-using>
                <http://www.newresalhaider.com/ontologies/aesop/foxstork/shallow-plate> ;
        <http://www.newresalhaider.com/ontologies/aesop/foxstork/can-not-eat-food-served-using>
                <http://www.newresalhaider.com/ontologies/aesop/foxstork/narrow-mouthed-jug> ;
        <http://www.newresalhaider.com/ontologies/aesop/foxstork/gives-invitation>
                [ <http://www.newresalhaider.com/ontologies/aesop/foxstork/has-food>
                          <http://www.newresalhaider.com/ontologies/aesop/foxstork/soup> ;
                  <http://www.newresalhaider.com/ontologies/aesop/foxstork/has-invited>
                          <http://www.newresalhaider.com/ontologies/aesop/foxstork/stork> ;
                  <http://www.newresalhaider.com/ontologies/aesop/foxstork/serves-using>
                          <http://www.newresalhaider.com/ontologies/aesop/foxstork/shallow-plate>
                ] .

<http://www.newresalhaider.com/ontologies/aesop/foxstork/stork>
        a       <http://www.newresalhaider.com/ontologies/aesop/foxstork/animal> ;
        <http://www.newresalhaider.com/ontologies/aesop/foxstork/can-eat-food-served-using>
                <http://www.newresalhaider.com/ontologies/aesop/foxstork/narrow-mouthed-jug> ;
        <http://www.newresalhaider.com/ontologies/aesop/foxstork/can-not-eat-food-served-using>
                <http://www.newresalhaider.com/ontologies/aesop/foxstork/shallow-plate> ;
        <http://www.newresalhaider.com/ontologies/aesop/foxstork/gives-invitation>
                [ <http://www.newresalhaider.com/ontologies/aesop/foxstork/has-food>
                          <http://www.newresalhaider.com/ontologies/aesop/foxstork/crumbled-food> ;
                  <http://www.newresalhaider.com/ontologies/aesop/foxstork/has-invited>
                          <http://www.newresalhaider.com/ontologies/aesop/foxstork/fox> ;
                  <http://www.newresalhaider.com/ontologies/aesop/foxstork/serves-using>
                          <http://www.newresalhaider.com/ontologies/aesop/foxstork/narrow-mouthed-jug>
                ] .")

(def fox-and-stork-blank-node-reif-edn
  {::aes/context
   {nil "http://www.newresalhaider.com/ontologies/aesop/foxstork/"
    :rdf "http://www.w3.org/1999/02/22-rdf-syntax-ns#"}
   ::aes/facts
   #{[:fox :rdf/type :animal]
     [:stork :rdf/type :animal]
     [:fox :gives-invitation :invitation1 'dinner1]
     [:invitation1 :has-invited :stork 'dinner1]
     [:invitation1 :has-food :soup 'dinner1]
     [:invitation1 :serves-using :shallow-plate 'dinner1]
     [:stork :gives-invitation :invitation2 'dinner2]
     [:invitation2 :has-invited :fox 'dinner2]
     [:invitation2 :has-food :crumbled-food 'dinner2]
     [:invitation2 :serves-using :narrow-mouthed-jug 'dinner2]
     [:fox :can-eat-food-served-using :shallow-plate]
     [:fox :can-not-eat-food-served-using :narrow-mouthed-jug]
     [:stork :can-eat-food-served-using :narrow-mouthed-jug]
     [:stork :can-not-eat-food-served-using :shallow-plate]}})
