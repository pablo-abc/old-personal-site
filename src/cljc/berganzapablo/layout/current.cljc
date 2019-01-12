(ns berganzapablo.layout.current
  (:require [berganzapablo.layout.navbar :refer [navbar-layout]]))

(defn current-page-layout [page paths]
  [:div
   [:header
    (navbar-layout)]
   page])
