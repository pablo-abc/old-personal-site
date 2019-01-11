(ns berganzapablo.cljs.helpers)

(defn get-by-id [id]
  (.getElementById js/document id))

(defn get-by-class-name [class-name]
  (.getElementsByClassName js/document class-name))

(defn get-html-by-id [id]
  (let [el (get-by-id id)]
    (if (nil? el) "" (.-innerHTML el))))

(defn get-state-data-set!
  "Obtain info from data-state attribute and then remove it.

  This is done in order to not cause a mismatch between server and client
  and to not clutter the resulting html with the data in the attribute."
  ([id] (get-state-data-set! id nil))
  ([id default]
   (let [el (get-by-id id)
         data (some-> el
                     .-dataset
                     .-state
                     js/JSON.parse
                     (js->clj :keywordize-keys true))]
     (when el (.removeAttribute el "data-state"))
     (or data default))))
