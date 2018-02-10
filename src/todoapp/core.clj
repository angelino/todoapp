(ns todoapp.core
  (:require [ring.adapter.jetty :as jetty]
            [ring.handler.dump :refer [handle-dump]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.session :refer [wrap-session]]
            [ring.middleware.resource :refer [wrap-resource]]
            [ring.middleware.file-info :refer [wrap-file-info]]
            [ring.util.response :refer [response]]
            [compojure.core :refer [defroutes ANY GET POST PUT DELETE]]
            [compojure.route :refer [not-found]]
            [cemerick.friend :as friend]
            [cemerick.friend.workflows :as workflows]
            [cemerick.friend.credentials :as credentials]
            [hiccup.core :refer [html]]
            [hiccup.page :refer [html5]]
            [todoapp.item.model :as items]
            [todoapp.list.model :as lists]
            [todoapp.item.handler :refer [handle-index-items
                                          handle-create-item
                                          handle-update-item
                                          handle-delete-item]]
            [todoapp.list.handler :refer [handle-index-lists
                                          handle-create-list
                                          handle-delete-list]]))

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

(defn login-form []
  (html
   [:form.form-signin {:method :post :action "/login"}
    [:h1 "Please sign in"]
    [:input#email.form-control {:type :email :name "username" :placeholder "Email Address"}]
    [:input#password.form-control {:type :password :name "password" :placeholder "Password"}]
    [:button.btn.btn-primary.btn-lg.btn-block {:type :submit} "Sign in"]]))

(defn login-page []
  (html5 {:lang :en}
         [:head
          [:title "Todo App"]
          [:meta {:name :viewport
                  :content "width=device-width, initial-scale=1.0"}]
          [:link {:href "/bootstrap/css/bootstrap.min.css"
                  :rel :stylesheet}]
          [:link {:href "/css/signin.css"
                  :rel :stylesheet}]]
         [:body.text-center (login-form)
          [:script {:src "https://cdnjs.cloudflare.com/ajax/libs/jquery/3.3.1/jquery.min.js"}]
          [:script {:src "/bootstrap/js/bootstrap.min.js"}]]))

(defn handle-login [req]
  (response (login-page)))

(defroutes routes
  (GET "/" [] greet)
  (GET "/goodbye" [] goodbye)
  (GET "/about" [] about)
  (ANY "/request" [] handle-dump)
  (GET "/yo/:name" [] yo)
  (GET "/calc/:n1/:op/:n2" [] calc)

  (GET "/login" [] handle-login)

  (GET "/lists" [] (friend/authenticated handle-index-lists))
  (POST "/lists" [] handle-create-list)
  (DELETE "/lists/:list-id" [] handle-delete-list)

  (GET "/lists/:list-id/items" [] handle-index-items)
  (POST "/lists/:list-id/items" [] handle-create-item)
  (DELETE "/lists/:list-id/items/:item-id" [] handle-delete-item)
  (PUT "/lists/:list-id/items/:item-id" [] handle-update-item)

  (not-found "Page not found"))

(def db (or (System/getenv "DATABASE_URL")
            "jdbc:postgres://localhost/todoapp?user=las&password=12345"))

(defn wrap-db [handler]
  (fn [req]
    (handler (assoc req :todoapp/db db))))

(defn wrap-server-name [handler]
  (fn [req]
    (assoc-in (handler req) [:headers "Server"] "Todo App")))

(def sim-methods {"DELETE" :delete "PUT" :put})

(defn wrap-sim-methods [handler]
  (fn [req]
    (if-let [method (and (:post (:request-method req))
                         (sim-methods (get-in req [:params "_method"])))]
      (handler (assoc req :request-method method))
      (handler req))))

(def users {"root@admin.com" {:username "root@admin.com"
                              :password (credentials/hash-bcrypt "admin")
                              :roles #{::admin}}})

(def app
  (-> routes
      (friend/authenticate {:credential-fn (partial credentials/bcrypt-credential-fn users)
                            :workflows [(workflows/interactive-form)]
                            :default-landing-uri "/lists"})
      (wrap-session)
      (wrap-params)
      (wrap-sim-methods)
      (wrap-db)
      (wrap-server-name)
      (wrap-resource "static")
      (wrap-file-info)))

(defn -main [port]
  (lists/create-table db)
  (items/create-table db)
  (jetty/run-jetty app {:port (Integer. port)}))
