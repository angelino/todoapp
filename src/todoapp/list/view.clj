(ns todoapp.list.view
  (:require [hiccup.page :refer [html5]]
            [hiccup.core :refer [h html]]))

(defn create-list-form []
  (html
   [:form.form-horizontal
    {:method "POST" :action "/lists"}
    [:div.form-group.row
     [:div.col-sm-2
      [:label.control-label {:for :name-input} "Name"]]
     [:div.col-sm-10
      [:input#name-input.form-control {:name :name
                                       :placeholder "Name"}]]]
    [:div.form-group.row
     [:div.col-sm.offset-sm-2
      [:input.btn.btn-primary {:type :submit
                               :value "New List"}]]]]))

(defn lists-ul [lists]
  (html
   [:ul
    (for [list lists]
      [:li
       [:a {:href (str "/lists/" (h (:id list)) "/items")}
        (h (:name list))]
       [:span.badge.badge-info
        (h (:items-left list))]])]))

(defn breadcrumb []
  (html
   [:nav {:aria-label "breadcrumb"}
    [:ol.breadcrumb
     [:li.breadcrumb-item [:a {:href "/"} "Home"]]
     [:li.breadcrumb-item.active "Lists"]]]))

(defn lists-page [lists]
  (html5 {:lang :en}
         [:head
          [:title "Todo App"]
          [:meta {:name :viewport
                  :content "width=device-width, initial-scale=1.0"}]
          [:link {:href "/bootstrap/css/bootstrap.min.css"
                  :rel :stylesheet}]]
         [:body
          [:div.container
           [:div.row
            [:h1 "My Lists"]]
           [:div.row
            (breadcrumb)]
           [:div.row
            (lists-ul lists)]
           [:div.row
            [:div.col-sm-6 (create-list-form)]]]
          [:script {:src "https://cdnjs.cloudflare.com/ajax/libs/jquery/3.3.1/jquery.min.js"}]
          [:script {:src "/bootstrap/js/bootstrap.min.js"}]]))
