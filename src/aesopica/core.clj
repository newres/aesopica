(ns aesopica.core
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
