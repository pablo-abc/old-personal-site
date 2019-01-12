(ns berganzapablo.db.config.db
  (:require [config.core :refer [env]]
            [honeysql.format :as fmt]))

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
