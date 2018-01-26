(ns todoapp.core
  (:require [ring.adapter.jetty :as jetty]
            [ring.handler.dump :refer [handle-dump]]
            [compojure.core :refer [defroutes GET]]
            [compojure.route :refer [not-found]]))

(defn greet [req]
  {:status 200
   :body "Hello World!!!!!!"
   :headers {}})

(defn goodbye [req]
  {:status 200
   :body "Goodbye, Cruel World!"
   :headers {}})

(defn about [req]
  {:status 200
   :body "Hi my name is Lucas and I've built this project to learn Ring and Compojure"
   :headers {}})

(defn yo [req]
  {:status 200
   :body (str "Yo! " (get-in req [:route-params :name]) "!")
   :headers {}})

(defroutes app
  (GET "/" [] greet)
  (GET "/goodbye" [] goodbye)
  (GET "/about" [] about)
  (GET "/request" [] handle-dump)
  (GET "/yo/:name" [] yo)
  (not-found "Page not found"))

(defn -main [port]
  (jetty/run-jetty app {:port (Integer. port)}))
