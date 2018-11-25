(ns aesopica.core
  (:require [clojure.spec.alpha :as s]))

(s/def ::context (s/map-of (s/or :keyword keyword? :nil nil?) string?))
(s/def ::custom-literal (s/keys :req-un [::value ::type]))
(s/def ::blank-node symbol?)
(s/def ::subject (s/or :keyword keyword? :blank-node ::blank-node))
(s/def ::object (s/or :keyword keyword? :string string? :number number? :boolean boolean? :blank-node ::blank-node :custom-literal ::custom-literal))
(s/def ::triple (s/cat :subject ::subject :predicate keyword? :object ::object))
(s/def ::quad (s/cat :subject ::subject :predicate keyword? :object ::object :graph keyword?))
(s/def ::fact (s/or :triple ::triple :quad ::quad))
(s/def ::facts (s/coll-of ::fact :kind set?))
(s/def ::knowledge-graph (s/keys :req [::context ::facts]))

(defn contextualize [context kw]
  "Given a context and a keyword, it creates a namespaced version of the keyword that is appropriate given the context."
  (let [ns (namespace kw)
        prefix (if (= ns "") (get context nil)
                   (get context (keyword ns)))]
    (str prefix (name kw))))
