(ns berganzapablo.db.config.db
  (:require [config.core :refer [env]]
            [honeysql.format :as fmt]
            [clojure.java.jdbc :as jdbc]
            [honeysql.core :as sql]))

(defmethod fmt/fn-handler "like" [_ field value]
  (str (fmt/to-sql field) " LIKE "
       (fmt/to-sql value)))

(defmethod fmt/fn-handler "ilike" [_ field value]
  (str (fmt/to-sql field) " ILIKE "
       (fmt/to-sql value)))

(def pg-db {:dbtype "postgresql"
            :dbname (env :db-database)
            :user (env :db-user)
            :password (env :db-password)})

(defn map->eq-query [m]
  (map #(vector := (first %) (second %)) m))

(defn execute [query]
  (jdbc/query pg-db (sql/format query)))
