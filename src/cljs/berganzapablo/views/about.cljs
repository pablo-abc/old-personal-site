(ns berganzapablo.views.about
  (:require-macros [cljs.core.async :refer [go]])
  (:require [reagent.core :as reagent]
            [cljs-http.client :as http]
            [cljs.core.async :refer [<!]]
            [berganzapablo.layout.about :refer [about-layout]]
            [berganzapablo.cljs.helpers :refer [getHTMLById]]))

(defn about-page []
  (let [state (reagent/atom {:text ""})]
    (reagent/create-class
     {:component-did-mount
      (fn []
        (go (let [response (<! (http/get "/api/about"
                                         {:with-credentials? false}))]
              (case (:status response)
                200 (reset! state (:body response))))))
      :display-name "about-page"
      :reagent-render
      (fn []
        (let [text (getHTMLById "about-text")]
          (when (not= text "")
            (reset! state {:text text}))
          (about-layout @state)))})))
