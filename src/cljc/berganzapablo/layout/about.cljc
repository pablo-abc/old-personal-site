(ns berganzapablo.layout.about)

(defn about-layout [state]
  [:span.main
   [:h1 "About me"]
   [:p#about-text (:text state)]])
