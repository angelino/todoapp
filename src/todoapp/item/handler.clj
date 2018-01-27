(ns todoapp.item.handler
  (:require [todoapp.item.model :refer [create-item
                                        read-items
                                        update-item
                                        delete-item]]
            [todoapp.item.view :refer [items-page]]))

(defn handle-index-items [req]
  (let [db (:todoapp/db req)
        items (read-items db)]
    {:status 200
     :headers {}
     :body (items-page items)}))

(defn handle-create-item [req]
  (let [db (:todoapp/db req)
        name (get-in req [:params "name"])
        description (get-in req [:params "description"])
        item-id (create-item db name description)]
    {:status 302
     :headers {"Location" "/items"}
     :body ""}))

(defn handle-delete-item [req]
  (let [db (:todoapp/db req)
        item-id (java.util.UUID/fromString (get-in req [:params :item-id])) 
        deleted? (delete-item db item-id)]
    (if deleted?
      {:status 302
       :headers {"Location" "/items"}
       :body ""}
      {:status 404
       :headers {}
       :body "Not found"})))

(defn handle-update-item [req]
  (let [db (:todoapp/db req)
        item-id (java.util.UUID/fromString (get-in req [:params :item-id]))
        checked (get-in req [:params "checked"])
        updated? (update-item db item-id (= "true" checked))]
    (if updated?
      {:status 302
       :headers {"Location" "/items"}
       :body ""}
      {:status 404
       :headers {}
       :body "Not found"})))
