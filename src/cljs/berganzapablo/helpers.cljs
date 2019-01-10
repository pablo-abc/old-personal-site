(ns berganzapablo.cljs.helpers)

(defn getById [id]
  (.getElementById js/document id))

(defn getByClassName [class-name]
  (.getElementsByClassName js/document class-name))

(defn getHTMLById [id]
  (let [el (getById id)]
    (if (nil? el) "" (.-innerHTML el))))

(getById "blog-list")
