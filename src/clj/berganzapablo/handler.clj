(ns berganzapablo.handler
  (:require [reitit.ring :as reitit-ring]
            [berganzapablo.middleware :refer [middleware]]
            [hiccup.page :refer [include-js include-css html5]]
            [config.core :refer [env]]
            [cheshire.core :refer [generate-string]]
            [berganzapablo.api.body :refer [api-test-body about-body]]
            [berganzapablo.layout.about :refer [about-layout]]
            [berganzapablo.layout.current :refer [current-page-layout]]
            [berganzapablo.layout.home :refer [home-layout]]
            [berganzapablo.layout.blogs :refer [blogs-layout]]
            [berganzapablo.db.blogs :refer [blogs-find]]
            [berganzapablo.menu :refer [menu-routes]]))

(defn head []
  [:head
   [:meta {:charset "utf-8"}]
   [:meta {:name "viewport"
           :content "width=device-width, initial-scale=1"}]
   [:meta {:name "author" :content "Pablo Berganza"}]
   [:title "Pablo Berganza"]
   (include-css (if (env :dev) "/css/site.css" "/css/site.min.css"))])

(defn mount-layout [layout]
  [:div#app
   (current-page-layout layout menu-routes)])

(defn current-page [uri]
  (html5
   (head)
   [:body.body-container
    (mount-layout
     (case uri
       "/" (home-layout)
       "/about" (about-layout (about-body))
       "/blog" (blogs-layout (blogs-find))))
    (include-js "/js/app.js")]))

(defn index-handler
  [{:keys [uri] :as request}]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (current-page uri)})

(defn keywordize-map [str-map]
  (into {} (map #(vector (keyword (first %)) (second %)) str-map)))

(defn body-for
  [{:keys [uri query-params body] :as request}]
  (case uri
    "/api/test" (api-test-body (keywordize-map query-params))
    "/api/about" (about-body)
    "/api/blogs" (blogs-find (keywordize-map query-params))))

(defn api-handler
  [request]
  {:status 200
   :headers {"Content-Type" "application/json"}
   :body (generate-string (body-for request))})

(def app
  (reitit-ring/ring-handler
   (reitit-ring/router
    [["/" {:get {:handler index-handler}}]
     ["/about" {:get {:handler index-handler}}]
     ["/blog" {:get {:handler index-handler}}]
     ["/api"
      ["/test" {:get {:handler api-handler}}]
      ["/about" {:get {:handler api-handler}}]
      ["/blogs" {:get {:handler api-handler}}]]]
    {:data {:middleware middleware}})
   (reitit-ring/routes
    (reitit-ring/create-resource-handler {:path "/" :root "/public"})
    (reitit-ring/create-default-handler))))
