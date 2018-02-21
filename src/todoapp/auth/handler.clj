(ns todoapp.auth.handler
  (:require [ring.util.response :refer [response]]
            [todoapp.auth.view :refer [login-page]]))

(defn handle-login [req]
  (response (login-page)))
