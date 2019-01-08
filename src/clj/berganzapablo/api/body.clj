(ns berganzapablo.api.body)

(defn api-test-body [{:keys [name]}]
  {:name name})

(defn about-body []
  {:text "Programmer. And stuff"})
