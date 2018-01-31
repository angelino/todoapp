(ns todoapp.list.handler
  (:require [todoapp.list.view :refer [lists-page]]
            [ring.util.response :refer [response redirect]]))

(defn handle-index-lists [req]
  (let [lists [{:id 1
                :name "Shopping List"
                :items-left 6}
               {:id 2
                :name "Todo Today"
                :items-left 1}]]
    (response (lists-page lists))))

(defn handle-create-list [req]
  (redirect "/lists"))

(defn handle-delete-list [req]
  (redirect "/lists"))
