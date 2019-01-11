(ns berganzapablo.cljs.helpers)

(defn getById [id]
  (.getElementById js/document id))

(defn getByClassName [class-name]
  (.getElementsByClassName js/document class-name))

(defn getHTMLById [id]
  (let [el (getById id)]
    (if (nil? el) "" (.-innerHTML el))))

(defn getStateDataSet
  "Obtain info from data-state attribute and then remove it.

  This is done in order to not cause a mismatch between server and client
  and to not clutter the resulting html with the data in the attribute."
  ([id] (getStateDataSet id nil))
  ([id default]
   (let [el (getById id)
         data (some-> el
                     .-dataset
                     .-state
                     js/JSON.parse
                     (js->clj :keywordize-keys true))]
     (when el (.removeAttribute el "data-state"))
     (or data default))))
