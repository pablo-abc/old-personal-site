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
   [:a.nav-item {:href "/contact"} "Contact me"]])

(defn home []
  [:section.main
   [:div.img-fit
    [:img {:src (str "https://scontent.fsal2-1.fna.fbcdn.net/"
                     "v/t1.0-9/30516719_10216530245715580_95"
                     "9662321477615616_n.jpg?_nc_cat=109&_nc_"
                     "ht=scontent.fsal2-1.fna&oh=6c0dc7645989"
                     "74ffae0215fb767e92b6&oe=5CB5AD16")}]]
   [:p "Welcome to my (over-engineered) personal page!"]])

(defn contact
  ([state] (contact state nil))
  ([state f]
   [:span.main
    [:button (when f {:on-click f})
     (str "Click me! Clicked: " (:times-clicked state))]
    [:p#contact-text #?(:clj {:data-state (generate-string (:text state))})
     (:text state)]]))

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
   [:h3 (:title blog)]
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
