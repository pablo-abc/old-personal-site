(ns berganzapablo.db.blogs)

(defn placeholder-blogs []
  (map (fn [num]
         {:id num
          :title (str "Title " num)
          :introduction (str "Introduction of " num)})
       (range 1 11)))

(defn blogs-find
  "Retrieve blog list from database."
  ([] (blogs-find {}))
  ([filter]
   (placeholder-blogs)))
