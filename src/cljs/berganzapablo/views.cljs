(ns berganzapablo.views
  (:require-macros [cljs.core.async :refer [go]])
  (:require [berganzapablo.layout :as layout]
            [reagent.core :as reagent]
            [reagent.session :as session]
            [cljs-http.client :as http]
            [cljs.core.async :refer [<!]]
            [berganzapablo.cljs.helpers
             :refer [get-html-by-id get-state-data-set!]]))

(defn home-page []
  [layout/home])

(defn contact-page []
  (let [state (reagent/atom {:text ""
                             :times-clicked 0})]
    (go (let [response (<! (http/get "/api/contact"
                                     {:with-credentials? false}))]
          (case (:status response)
            200 (swap! state assoc :text (:text (:body response))))))
    (fn []
      (let [text (get-state-data-set! "contact-text")]
        (when text
          (swap! state assoc :text text))
        [layout/contact
         @state
         #(swap! state
                 assoc
                 :times-clicked
                 (inc (:times-clicked @state)))]))))

(defn blogs-page []
  (let [state (reagent/atom {:blogs '()})]
    (go (let [response (<! (http/get "/api/blogs"
                                     {:with-credentials? false}))]
          (case (:status response)
            200 (reset! state {:blogs (:body response)}))))
    (fn []
      (let [blogs (get-state-data-set! "blog-list" '())]
        (when (zero? (count (:blogs @state)))
          (reset! state {:blogs blogs}))
        [layout/blogs (:blogs @state)]))))

(defn blog-page []
  (let [state (reagent/atom {})]
    (go (let [routing-data (session/get :route)
              blog-id (get-in routing-data [:route-params :blog-id])
              response (<! (http/get (str "/api/blogs/" blog-id)
                                     {:with-credentials? false}))]
          (case (:status response)
            200 (reset! state (:body response)))))
    (fn []
      (let [blog (get-state-data-set! "blog" '())]
        (when (zero? (count @state))
          (reset! state blog))
        [layout/blog @state]))))
