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
  (fn []
    [layout/home]))

(defn contact-page []
  (let [state (reagent/atom {:text ""})]
    (reagent/create-class
     {:component-did-mount
      (fn []
        (go (let [response (<! (http/get "/api/contact"
                                         {:with-credentials? false}))]
              (case (:status response)
                200 (reset! state (:body response))))))

      :display-name "contact-page"

      :reagent-render
      (fn []
        (let [text (get-html-by-id "contact-text")]
          (when (not= text "")
            (reset! state {:text text}))
          [layout/contact @state]))})))

(defn blogs-page []
  (let [state (reagent/atom {:blogs '()})]
    (reagent/create-class
     {:component-did-mount
      (fn []
        (go (let [response (<! (http/get "/api/blogs"
                                         {:with-credentials? false}))]
              (case (:status response)
                200 (reset! state {:blogs (:body response)})))))

      :display-name "blogs-page"

      :reagent-render
      (fn []
        (let [blogs (get-state-data-set! "blog-list" '())]
          (when (zero? (count (:blogs @state)))
            (reset! state {:blogs blogs}))
          [layout/blogs (:blogs @state)]))})))

(defn blog-page []
  (let [state (reagent/atom {})]
    (reagent/create-class
     {:component-did-mount
      (fn []
        (go (let [routing-data (session/get :route)
                  blog-id (get-in routing-data [:route-params :blog-id])
                  response (<! (http/get (str "/api/blogs/" blog-id)
                                         {:with-credentials? false}))]
              (case (:status response)
                200 (reset! state (:body response))))))

      :display-name "blog-page"

      :reagent-render
      (fn []
        (let [blog (get-state-data-set! "blog" '())]
          (when (zero? (count @state))
            (reset! state blog))
          [layout/blog @state]))})))
