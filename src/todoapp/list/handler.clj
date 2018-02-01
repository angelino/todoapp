(ns todoapp.list.handler
  (:require [todoapp.list.model :refer [create-list
                                        read-lists]]
            [todoapp.list.view :refer [lists-page]]
            [ring.util.response :refer [response redirect]]))

(defn handle-index-lists [req]
  (let [db (:todoapp/db req)
        lists (read-lists db)]
    (response (lists-page lists))))

(defn handle-create-list [req]
  (let [db (:todoapp/db req)
        name (get-in req [:params "name"])
        list (create-list db name)] 
    (redirect "/lists")))

(defn handle-delete-list [req]
  (redirect "/lists"))
