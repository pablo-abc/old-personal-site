(ns berganzapablo.layout.current)

(defn current-page-layout [page paths]
  [:div
   [:nav
    [:p
     [:a {:href (:index paths)} "Home"] " | "
     [:a {:href (:blogs paths)} "Blog"] " | "
     [:a {:href (:about paths)} "About me"]]]
   page])
