(ns berganzapablo.layout.blogs
  (:require #?(:clj [cheshire.core :refer [generate-string]])))

(defn- fill-blogs
  "Fill component with the blog's information."
  [blog]
  [:section.blog-item #?(:cljs {:key (:id blog)})
   [:h3 (:title blog)]
   [:p (:introduction blog)]])

(defn blogs-layout
  "Return layout for blog list page."
  [blogs]
  (-> blogs
     (->> (map fill-blogs))
     #?(:clj (conj {:data-state (generate-string blogs)})
       :cljs identity)
     (conj :section#blog-list)
     vec))
