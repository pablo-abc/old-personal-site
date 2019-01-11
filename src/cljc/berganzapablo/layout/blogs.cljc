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
     (conj {:data-state #?(:clj (generate-string blogs)
                          :cljs (js/JSON.stringify (clj->js blogs)))})
     (conj :section#blog-list)
     vec))
