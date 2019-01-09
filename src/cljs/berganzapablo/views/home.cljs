(ns berganzapablo.views.home
  (:require [berganzapablo.routes :refer [path-for]]
            [berganzapablo.layout.home :refer [home-layout]]))


(defn home-page []
  (fn []
    (home-layout)))
