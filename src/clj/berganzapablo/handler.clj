(ns berganzapablo.handler
  (:require [reitit.ring :as reitit-ring]
            [berganzapablo.middleware :refer [middleware]]
            [hiccup.page :refer [include-js include-css html5]]
            [config.core :refer [env]]
            [cheshire.core :refer [generate-string]]
            [berganzapablo.api.body :refer [api-test-body about-body]]
            [berganzapablo.layout.about :refer [about-layout]]
            [berganzapablo.layout.current :refer [current-page-layout]]
            [berganzapablo.layout.home :refer [home-layout]]))

(def menu-routes ["/" "/about"])

(def mount-target
  [:div#app
   [:h2 "Welcome to berganzapablo"]
   [:p "please wait while Figwheel is waking up ..."]
   [:p "(Check the js console for hints if nothing exсiting happens.)"]])

(defn head []
  [:head
   [:meta {:charset "utf-8"}]
   [:meta {:name "viewport"
           :content "width=device-width, initial-scale=1"}]
   (include-css (if (env :dev) "/css/site.css" "/css/site.min.css"))])

(defn loading-page []
  (html5
   (head)
   [:body {:class "body-container"}
    mount-target
    (include-js "/js/app.js")]))

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
       "/about" (about-layout (about-body))))
    (include-js "/js/app.js")]))

(defn index-handler
  [{:keys [uri] :as request}]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (case uri
           "/" (current-page uri)
           "/about" (current-page uri)
           (loading-page))})

(defn keywordize-map [str-map]
  (into {} (map #(vector (keyword (first %)) (second %)) str-map)))

(defn body-for
  [{:keys [uri query-params body] :as request}]
  (case uri
    "/api/test" (api-test-body (keywordize-map query-params))
    "/api/about" (about-body)))

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
     ["/api"
      ["/test" {:get {:handler api-handler}}]
      ["/about" {:get {:handler api-handler}}]]]
    {:data {:middleware middleware}})
   (reitit-ring/routes
    (reitit-ring/create-resource-handler {:path "/" :root "/public"})
    (reitit-ring/create-default-handler))))
