(ns todoapp.dev
  (:require [ring.adapter.jetty :as jetty]
            [ring.middleware.reload :refer [wrap-reload]]
            [todoapp.core :as core]
            [todoapp.item.model :as items]
            [todoapp.list.model :as lists]))

(defn -main [port]
  (lists/create-table core/db)
  (items/create-table core/db)
  (jetty/run-jetty (wrap-reload #'core/app) {:port (Integer. port)}))
