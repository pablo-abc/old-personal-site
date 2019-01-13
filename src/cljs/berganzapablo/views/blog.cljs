(ns berganzapablo.views.blog
  (:require-macros [cljs.core.async :refer [go]])
  (:require [reagent.core :as reagent]
            [reagent.session :as session]
            [cljs-http.client :as http]
            [cljs.core.async :refer [<!]]
            [berganzapablo.layout.blog :refer [blog-layout]]
            [berganzapablo.cljs.helpers :refer [get-state-data-set!]]))

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
          [blog-layout @state]))})))
