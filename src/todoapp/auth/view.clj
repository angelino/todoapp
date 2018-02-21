(ns todoapp.auth.view
  (:require [hiccup.core :refer [html]]
            [hiccup.page :refer [html5]]))

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
