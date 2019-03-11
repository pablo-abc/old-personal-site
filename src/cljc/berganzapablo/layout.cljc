(ns berganzapablo.layout
  (:require #?(:clj [cheshire.core :refer [generate-string]])
            [clojure.string :as string]
            [markdown.core :as md]))

(def ability-images
  '("https://raw.githubusercontent.com/voodootikigod/logo.js/master/js.png"
    "https://raw.githubusercontent.com/remojansen/logo.ts/master/ts.png"
    "https://upload.wikimedia.org/wikipedia/commons/thumb/5/5d/Clojure_logo.svg/768px-Clojure_logo.svg.png"
    "https://raw.githubusercontent.com/cljs/logo/master/cljs-white.png"
    "https://upload.wikimedia.org/wikipedia/commons/thumb/c/c3/Python-logo-notext.svg/768px-Python-logo-notext.svg.png"
    "https://dwglogo.com/wp-content/uploads/2017/09/1300px-Docker_container_engine_logo.png"))

(defmacro wait-for [[content value] color element]
  `(let [~content ~value]
     (if ~content
       ~element
       [:div.loader (loader ~color)])))

(defn- loader [color]
  (vec (conj (take 4 (repeat [:div {:class (str "lds-" color)}]))
             :div.lds-ellipsis)))

(defn- ability-colors [quantity]
  (flatten
   (map vector
        (repeat quantity "color-1") (repeat quantity "color-2"))))

(defn- format-js-date [timestamp]
  #?(:cljs
    (let [js-date (js/Date. timestamp)
          year (.getFullYear js-date)
          month (inc (.getMonth js-date))
          date (.getDate js-date)
          month-zero (when (= (count (str month)) 1) "0")
          date-zero (when (= (count (str date)) 1) "0")]
      (str year "-" month-zero month "-" date-zero date))))

(defn- format-time [timestamp]
  (when timestamp
    #?(:cljs (format-js-date timestamp)
      :clj (first (string/split (str timestamp) #" ")))))

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

(defn- home-img-attr [source]
  (cond-> {}
    source (assoc :src source)))

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
    (vec (conj
          (map #(vector :section.ability {:class %2}
                        [:img
                         (home-img-attr %1)])
               ability-images
               (ability-colors (count ability-images)))
          :section.box-abilities))]])

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
   [:header
    [:h1 (or (:title blog) (loader "white"))]
    [:p.introduction (:introduction blog)]]
   [:p.created  (format-time (:created blog))]
   (let [content (:content blog)]
     (if content
       [:section.content
        (-> content
           #?(:clj (md/md-to-html-string)
             :cljs (-> (md/md->html)
                      (->> (assoc {} :__html)
                         (assoc {} :dangerouslySetInnerHTML)))))]
       [:div.loader (loader "pink")]))])

(defn current-page [page paths]
  [:div
   [:header
    (navbar)]
   page])
