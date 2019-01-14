(ns berganzapablo.layout
  (:require #?(:clj [cheshire.core :refer [generate-string]])
            [clojure.string :as string]))

(defn- fill-blogs
  "Fill component with the blog's information."
  [blog]
  [:a.blog-item {:href (str "/blog/" (:id blog))}
   [:section #?(:cljs {:key (:id blog)})
    [:h3 (:title blog)]
    [:p (:introduction blog)]]])

(defn navbar []
  [:nav
   [:a#logo {:href "/"} "Pablo Berganza"]
   [:a.nav-item {:href "/blog"} "Blog"] " | "
   [:a.nav-item {:href "/about"} "About me"]])

(defn home []
  [:span.main
   [:p "Welcome to my (over-engineered) personal page!"]])

(defn about [state]
  [:span.main
   [:h1 "About me"]
   [:p#about-text (:text state)]])

(defn blogs
  "Return layout for blog list page."
  [blogs]
  (-> blogs
     (->> (map fill-blogs))
     #?(:clj (conj {:data-state (generate-string blogs)})
       :cljs identity)
     (conj :section#blog-list)
     vec))

(defn blog
  "Fill component with blog content"
  [blog]
  [:article#blog #?(:clj {:data-state (generate-string blog)})
   [:h2 (:title blog)]
   [:p.introduction (:introduction blog)]
   (-> (:content blog)
      (string/split #"\n")
      (->> (map #(vector :p %)))
      (conj :section.content)
      vec)])

(defn current-page [page paths]
  [:div
   [:header
    (navbar)]
   page])