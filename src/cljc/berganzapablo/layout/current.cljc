(ns berganzapablo.layout.current)

(defn current-page-layout [page [index-path about-path]]
  [:div
   [:header
    [:p [:a {:href index-path} "Home"] " | "
     [:a {:href about-path} "About me"]]]
   page])
