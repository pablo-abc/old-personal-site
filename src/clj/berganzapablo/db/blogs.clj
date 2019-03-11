(ns berganzapablo.db.blogs
  (:require [honeysql.helpers :refer :all :as helpers]
            [berganzapablo.db.config.db
             :refer [execute map->eq-query]]))

(defn build-find-query
  ([] (-> (select :*)
         (from :blog_post)))
  ([filter]
   (-> (apply where (map->eq-query filter))
      (select :*)
      (from :blog_post))))

(defn find-many
  "Retrieve blog list from database."
  ([] (find-many {}))
  ([filter]
   (execute (build-find-query))))

(defn find-one
  "Retrieve una blog from database."
  ([] (find-one {}))
  ([filter]
   (first (execute (build-find-query filter)))))
