(ns berganzapablo.db.blogs
  (:require [clojure.java.jdbc :as jdbc]
            [honeysql.core :as sql]
            [honeysql.helpers :refer :all :as helpers]
            [berganzapablo.db.config.db :refer [pg-db]]))

(defn find-query []
  (-> (select :*)
     (from :blog_post)))

(defn blogs-find
  "Retrieve blog list from database."
  ([] (blogs-find {}))
  ([filter]
   (jdbc/query pg-db (sql/format (find-query)))))
