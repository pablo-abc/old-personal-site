(ns berganzapablo.api.body)

(defn api-test-body [{:keys [name]}]
  {:name name})

(defn contact-body []
  {:text "Programmer. And stuff"
   :times-clicked 0})
