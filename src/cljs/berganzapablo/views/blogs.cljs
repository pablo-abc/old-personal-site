(ns berganzapablo.views.blogs
  (:require-macros [cljs.core.async :refer [go]])
  (:require [reagent.core :as reagent]
            [cljs-http.client :as http]
            [cljs.core.async :refer [<!]]
            [berganzapablo.layout.blogs :refer [blogs-layout]]
            [berganzapablo.cljs.helpers :refer [getById]]))

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
        (let [el (getById "blog-list")
              blogs (cond-> el
                      el .-dataset
                      el .-state
                      el js/JSON.parse
                      el (js->clj :keywordize-keys true))]
          (when (= (count (:blogs @state)) 0)
            (reset! state {:blogs blogs}))
          (blogs-layout (:blogs @state))))})))
