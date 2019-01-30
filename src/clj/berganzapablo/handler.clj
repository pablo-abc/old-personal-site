(ns berganzapablo.handler
  (:require [reitit.ring :as reitit-ring]
            [berganzapablo.middleware :refer [middleware]]
            [hiccup.page :refer [include-js include-css html5]]
            [config.core :refer [env]]
            [cheshire.core :refer [generate-string]]
            [berganzapablo.api.body :refer [api-test-body contact-body]]
            [berganzapablo.db.blogs :as blogs]
            [berganzapablo.menu :refer [menu-routes]]
            [berganzapablo.layout :as layout]))

(defn head []
  [:head
   [:meta {:charset "utf-8"}]
   [:meta {:name "viewport"
           :content "width=device-width, initial-scale=1"}]
   [:meta {:name "author" :content "Pablo Berganza"}]
   [:title "Pablo Berganza"]
   [:link {:type "text/css"
           :crossorigin "anonymous"
           :integrity "sha384-lZN37f5QGtY3VHgisS14W3ExzMWZxybE1SJSEsQp9S+oqd12jhcu+A56Ebc1zFSJ"
           :href "https://use.fontawesome.com/releases/v5.7.0/css/all.css"
           :rel "stylesheet"}]
   (include-css (if (env :dev) "/css/site.css" "/css/site.min.css"))])

(defn mount-layout [layout]
  [:div#app
   (layout/current-page layout menu-routes)])

(defn current-page [{:keys [uri path-params] :as request}]
  (html5
   (head)
   [:body.body-container
    (mount-layout
     (condp re-matches uri
       #"/" (layout/home)
       #"/contact" (layout/contact (contact-body))
       #"/blog" (layout/blogs (blogs/find-many))
       #"/blog/[0-9]+" (layout/blog
                        (blogs/find-one
                         {:id (Integer/valueOf (:blog-id path-params))}))))
    (include-js "/js/app.js")]))

(defn index-handler
  [request]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (current-page request)})

(defn keywordize-map [str-map]
  (into {} (map #(vector (keyword (first %)) (second %)) str-map)))

(defn body-for
  [{:keys [uri query-params path-params body] :as request}]
  (condp re-matches uri
    #"/api/test" (api-test-body (keywordize-map query-params))
    #"/api/contact" (contact-body)
    #"/api/blogs" (blogs/find-many (keywordize-map query-params))
    #"/api/blogs/[0-9]+" (blogs/find-one
                          {:id (Integer. (:blog-id path-params))})))

(defn api-handler
  [request]
  {:status 200
   :headers {"Content-Type" "application/json"}
   :body (generate-string (body-for request))})

(def app
  (reitit-ring/ring-handler
   (reitit-ring/router
    [["/" {:get {:handler index-handler}}]
     ["/contact" {:get {:handler index-handler}}]
     ["/blog"
      ["" {:get {:handler index-handler}}]
      ["/:blog-id" {:get {:handler index-handler
                          :parameters {:path {:blog-id int?}}}}]]
     ["/api"
      ["/test" {:get {:handler api-handler}}]
      ["/contact" {:get {:handler api-handler}}]
      ["/blogs"
       ["" {:get {:handler api-handler}}]
       ["/:blog-id" {:get {:handler api-handler
                           :parameters {:path {:blog-id int?}}}}]]]]
    {:data {:middleware middleware}})
   (reitit-ring/routes
    (reitit-ring/create-resource-handler {:path "/" :root "/public"})
    (reitit-ring/create-default-handler))))
