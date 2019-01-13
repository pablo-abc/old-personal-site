(ns berganzapablo.layout.blog
  (:require #?(:clj [cheshire.core :refer [generate-string]])))

(defn blog-layout
  "Fill component with blog content"
  [blog]
  [:article#blog #?(:clj {:data-state (generate-string blog)})
   [:h2 (:title blog)]
   [:p.introduction (:introduction blog)]
   (-> (:content blog)
      (clojure.string/split #"\n")
      (->> (map #(vector :p %)))
      (conj :section.content)
      vec)])
