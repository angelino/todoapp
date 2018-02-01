(ns todoapp.item.view
  (:require [hiccup.page :refer [html5]]
            [hiccup.core :refer [html h]]))

(defn create-item-form []
  (html
   [:form.form-horizontal
    {:method "POST" :action "/items"}
    [:div.form-group
     [:label.control-label.col-sm-2 {:for :name-input}
      "Name"]
     [:div.col-sm-10
      [:input#name-input.form-control
       {:name :name
        :placeholder "Name"}]]]
    [:div.form-group
     [:label.control-label.col-sm-2 {:for :desc-input}
      "Description"]
     [:div.col-sm-10
      [:input#desc-input.form-control
       {:name :description
        :placeholder "Description"}]]]
    [:div.form-group
     [:div.col-sm-offset-2.col-sm-10
      [:input.btn.btn-primary
       {:type :submit
        :value "New item"}]]]]))

(defn delete-item-form [item-id]
  (html 
   [:form {:method "POST" :action (str "/items/" item-id)}
    [:input {:type :hidden :name "_method" :value "DELETE"}]
    [:div.btn-group
     [:input.btn.btn-danger.btn-xs {:type :submit :value "Delete"}]]]))

(defn update-item-form [item-id checked]
  (html
   [:form {:method "POST" :action (str "/items/" item-id)}
    [:input {:type :hidden :name "_method" :value "PUT"}]
    [:input {:type :hidden :name "checked" :value (if checked "false" "true")}]
    [:div.btn-group
     [:input.btn.btn-primary.btn-xs {:type :submit :value (if checked "DONE" "TODO")}]]]))

(defn items-table [items]
  (html
   (if (seq items)
     [:table.table.table-striped
      [:thead
       [:tr
        [:th "Remove"]
        [:th "Status"]
        [:th "Name"]
        [:th "Description"]]]
      [:tbody
       (for [item items]
         [:tr
          [:td (delete-item-form (:id item))]
          [:td (update-item-form (:id item) (:checked item))]
          [:td (h (:name item))]
          [:td (h (:description item))]])]]
     [:div.col-sm-offset-1 "There are no items."])))

(defn items-page [items]
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
            [:h1 "My Items"]]
           [:div.row
            (items-table items)]
           [:div.row
            [:div.col-sm-6
             [:h3 "Create new Item"
              (create-item-form)]]]]
          [:script {:src "https://cdnjs.cloudflare.com/ajax/libs/jquery/3.3.1/jquery.min.js"}]
          [:script {:src "/bootstrap/js/bootstrap.min.js"}]]))
