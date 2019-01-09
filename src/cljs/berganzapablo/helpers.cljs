(ns berganzapablo.cljs.helpers)

(defn getById [id]
  (.getElementById js/document id))

(defn getHTMLById [id]
  (let [el (getById id)]
    (if (nil? el) "" (.-innerHTML el))))
