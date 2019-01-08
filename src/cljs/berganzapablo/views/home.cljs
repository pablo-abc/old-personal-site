(ns berganzapablo.views.home
  (:require [berganzapablo.routes :refer [path-for]]))


(defn home-page []
  (fn []
    [:span.main
     [:h1 "Welcome to berganzapablo"]
     [:p "Programmer. Wish there were more LISP in the world."]]))
