(ns todoapp.core
  (:require [ring.adapter.jetty :as jetty]
            [ring.handler.dump :refer [handle-dump]]
            [ring.middleware.params :refer [wrap-params]]
            [compojure.core :refer [defroutes GET]]
            [compojure.route :refer [not-found]]
            [todoapp.item.model :as items]))

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

(def operations {"+" +
                 "-" -
                 "*" *
                 ":" /})

(defn calc [req]
  (let [n1 (Long. (get-in req [:route-params :n1]))
        n2 (Long. (get-in req [:route-params :n2]))
        op (get-in req [:route-params :op])
        f  (get operations op)]
    (if f
      {:status 200
       :body (str (f n1 n2))
       :headers {}}
      {:status 404
       :body (str "Operation unknown: " op)
       :headers {}})))

(defroutes routes
  (GET "/" [] greet)
  (GET "/goodbye" [] goodbye)
  (GET "/about" [] about)
  (GET "/request" [] handle-dump)
  (GET "/yo/:name" [] yo)
  (GET "/calc/:n1/:op/:n2" [] calc)
  (not-found "Page not found"))

(defn wrap-db [handler]
  (fn [req]
    (handler (assoc req :todoapp/db db))))

(def app
  (-> routes
      wrap-params
      wrap-db))

(def db "jdbc:postgres://localhost/todoapp")

(defn -main [port]
  (items/create-table db)
  (jetty/run-jetty app {:port (Integer. port)}))
