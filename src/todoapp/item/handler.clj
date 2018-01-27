(ns todoapp.item.handler
  (:require [todoapp.item.model :refer [create-item
                                        read-items
                                        update-item
                                        delete-item]]))

(defn handle-index-items [req]
  (let [db (:todoapp/db req)
        items (read-items db)]
    {:status 200
     :body (str "<div>" (mapv :name items) "</div>"
                "<form action=\"request\" method=\"POST\">"
                "<input type=\"text\" name=\"name\" placeholder=\"Name\">"
                "<input type=\"text\" name=\"description\" placeholder=\"Description\">"
                "<input type=\"submit\">"
                "</form>")
     :headers {}}))
