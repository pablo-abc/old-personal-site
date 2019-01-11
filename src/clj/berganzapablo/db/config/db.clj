(ns berganzapablo.db.config.db
  (:require [clojure.java.jdbc :as jdbc]
            [config.core :refer [env]]
            [honeysql.core :as sql]
            [honeysql.helpers :refer :all :as helpers]))

(def pg-db {:dbtype "postgresql"
            :dbname (env :db-database)
            :user (env :db-user)
            :password (env :db-password)})

(jdbc/query pg-db (sql/format (-> (select :*)
                                 (from :author))))
