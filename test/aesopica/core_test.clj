(ns aesopica.core-test
  (:require [clojure.test :refer :all]
            [aesopica.core :as aes :refer :all]
            [clojure.spec.alpha :as s]
            [aesopica.examples :refer :all]))

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

(deftest valid-context-fox-stork-literals-test
  (let [context (::aes/context fox-and-stork-literals-edn)]
    (is (s/valid? ::aes/context context))))

(deftest valid-kb-fox-stork-literals-test
  (let [facts (::aes/facts fox-and-stork-literals-edn)]
    (is (s/valid? ::aes/facts facts))))

(deftest valid-kb-fox-stork-test
  (let [kg fox-and-stork-literals-edn]
    (is (s/valid? ::aes/knowledge-graph kg))))

(deftest contextualize-base-test
  (let [context {nil "http://www.newresalhaider.com/ontologies/aesop/foxstork/"}
        kw :fox]
    (is (= "http://www.newresalhaider.com/ontologies/aesop/foxstork/fox" (contextualize context kw)))))

(deftest contextualize-keyword-test
  (let [context {:foxstork "http://www.newresalhaider.com/ontologies/aesop/foxstork/"}
        kw :foxstork/fox]
    (is (= "http://www.newresalhaider.com/ontologies/aesop/foxstork/fox" (contextualize context kw)))))
