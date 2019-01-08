(ns berganzapablo.views.about
  (:require-macros [cljs.core.async :refer [go]])
  (:require [reagent.core :as reagent]
            [cljs-http.client :as http]
            [cljs.core.async :refer [<!]]))


(defn about-page []
  (let [state (reagent/atom {:text ""})]
    (reagent/create-class
     {:component-did-mount
      (fn []
        (go (let [response (<! (http/get "/api/about"
                                         {:with-credentials? false}))]
              (case (:status response)
                200 (reset! state (:body response))))))
      :reagent-render
      (fn [] [:span.main
              [:h1 "About me"]
              [:p (:text @state)]])})))
