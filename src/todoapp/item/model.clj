(ns todoapp.item.model
  (:require [clojure.java.jdbc :as db]))

(defn create-table [db]
  (db/execute!
   db
   ["CREATE EXTENSION IF NOT EXISTS \"uuid-ossp\""])
  (db/execute!
   db
   ["CREATE TABLE IF NOT EXISTS items(
       id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
       list_id UUID REFERENCES lists(id),
       name TEXT NOT NULL,
       description TEXT NOT NULL,
       checked BOOLEAN NOT NULL DEFAULT FALSE,
       created_at TIMESTAMPTZ NOT NULL DEFAULT now())"]))

(defn create-item [db list-id name description]
  (:id (first (db/query
               db
               ["INSERT INTO items(name, description, list_id) VALUES (?, ?, ?) RETURNING id" name description list-id]))))

(defn update-item [db id checked]
  (= [1] (db/execute!
          db
          ["UPDATE items SET checked = ? WHERE id =?" checked id])))

(defn delete-item [db id]
  (= [1] (db/execute!
          db
          ["DELETE FROM items WHERE id = ?" id])))

(defn read-items [db list-id]
  (db/query
   db
   ["SELECT id, list_id, name, description, checked, created_at FROM items WHERE list_id = ? ORDER BY created_at" list-id]))
