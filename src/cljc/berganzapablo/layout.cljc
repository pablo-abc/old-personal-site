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
   [:div.left-nav
    [:div.logo
     [:a#logo {:href "/"} "Pablo Berganza"]]
    [:div.nav-items
     [:a.nav-item {:href "/blog"} "Blog"] " | "
     [:a.nav-item {:href "/contact"} "Contact me"]]]
   [:div.right-nav
    [:a.nav-item [:i.fab.fa-github]]
    [:a.nav-item [:i.fab.fa-instagram]]
    [:a.nav-item [:i.fab.fa-facebook]]]])

(defn home []
  [:section.main
   [:section.main-info
    [:div#profile-box
     [:img#profile-pic {:src (str "https://scontent.fsal2-1.fna.fbcdn.net/"
                                  "v/t1.0-9/30516719_10216530245715580_95"
                                  "9662321477615616_n.jpg?_nc_cat=109&_nc_"
                                  "ht=scontent.fsal2-1.fna&oh=6c0dc7645989"
                                  "74ffae0215fb767e92b6&oe=5CB5AD16")}]
     [:h3#profile-bubble "Hi, I make web stuff! Welcome!"]]]
   [:section.abilities
    [:h2 "My Abilities"]
    [:section.box-abilities
     [:section.ability.color-1
      [:img {:src "https://raw.githubusercontent.com/voodootikigod/logo.js/master/js.png"}]]
     [:section.ability.color-2
      [:img {:src "https://raw.githubusercontent.com/remojansen/logo.ts/master/ts.png"}]]
     [:section.ability.color-1
      [:img {:src "https://upload.wikimedia.org/wikipedia/commons/thumb/5/5d/Clojure_logo.svg/768px-Clojure_logo.svg.png"}]]
     [:section.ability.color-2
      [:img {:src "https://raw.githubusercontent.com/cljs/logo/master/cljs-white.png"}]]
     [:section.ability.color-1
      [:img {:src "https://upload.wikimedia.org/wikipedia/commons/thumb/c/c3/Python-logo-notext.svg/768px-Python-logo-notext.svg.png"}]]
     [:section#docker-logo.ability.color-2
      [:img {:src "https://dwglogo.com/wp-content/uploads/2017/09/1300px-Docker_container_engine_logo.png"}]]]]])

(defn contact
  "Return layout for contact page."
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
