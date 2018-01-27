(ns todoapp.dev
  (:require [ring.adapter.jetty :as jetty]
            [ring.middleware.reload :refer [wrap-reload]]
            [todoapp.core :as core]
            [todoapp.item.model :as items]))

(defn -main [port]
  (items/create-table core/db)
  (jetty/run-jetty (wrap-reload #'core/app) {:port (Integer. port)}))
