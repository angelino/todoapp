(ns todoapp.core
  (:require [ring.adapter.jetty :as jetty]
            [ring.middleware.reload :refer [wrap-reload]]))

(defn greet [req] 
  (cond 
    (= (:uri req) "/")
    {:status 200
     :body "Hello World!!!!!!"
     :headers {}}
    (= (:uri req) "/goodbye") 
    {:status 200
     :body "Goodbye, Cruel World!"
     :headers {}}
    :else
    {:status 404
     :body "Not found!"
     :headers {}}))

(defn -main [port]
  (jetty/run-jetty greet {:port (Integer. port)}))

(defn -dev-main [port]
  (jetty/run-jetty (wrap-reload #'greet) {:port (Integer. port)}))
