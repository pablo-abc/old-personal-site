(ns berganzapablo.core
  (:require [reagent.core :as reagent :refer [atom]]
            [reagent.session :as session]
            [reitit.frontend :as reitit]
            [clerk.core :as clerk]
            [accountant.core :as accountant]
            [berganzapablo.views.home :refer [home-page]]
            [berganzapablo.views.about :refer [about-page]]
            [berganzapablo.views.blogs :refer [blogs-page]]
            [berganzapablo.routes :refer [path-for router]]
            [berganzapablo.layout.current :refer [current-page-layout]]
            [berganzapablo.menu :refer [menu-routes]]))

;; -------------------------
;; Translate routes -> page components

(defn page-for [route]
  (case route
    :index #'home-page
    :about #'about-page
    :blogs #'blogs-page))

;; -------------------------
;; Page mounting component

(defn current-page []
  (fn []
    (let [page (:current-page (session/get :route))]
      (current-page-layout [page] menu-routes))))

;; -------------------------
;; Initialize app

(defn mount-root []
  (js/ReactDOM.hydrate
   (reagent/as-element [current-page]) (.getElementById js/document "app")))

(defn init! []
  (clerk/initialize!)
  (accountant/configure-navigation!
   {:nav-handler
    (fn [path]
      (let [match (reitit/match-by-path router path)
            current-page (:name (:data  match))
            route-params (:path-params match)]
        (reagent/after-render clerk/after-render!)
        (session/put! :route {:current-page (page-for current-page)
                              :route-params route-params})
        (clerk/navigate-page! path)
        ))
    :path-exists?
    (fn [path]
      (boolean (reitit/match-by-path router path)))})
  (accountant/dispatch-current!)
  (mount-root))
