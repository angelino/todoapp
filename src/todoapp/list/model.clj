(ns todoapp.list.model
  (:require [clojure.java.jdbc :as db]))

(defn create-table [db]
  (db/execute!
   db
   ["CREATE EXTENSION IF NOT EXISTS \"uuid-ossp\""])
  (db/execute!
   db
   ["CREATE TABLE IF NOT EXISTS lists(
       id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
       name TEXT NOT NULL,
       created_at TIMESTAMPTZ NOT NULL DEFAULT now())"]))

(defn create-list [db name]
  (:id (first (db/query
               db
               ["INSERT INTO lists(name) VALUES (?) RETURNING id" name]))))

(defn read-lists [db]
  (db/query
   db
   ["SELECT id, name, created_at FROM lists ORDER BY created_at"]))
