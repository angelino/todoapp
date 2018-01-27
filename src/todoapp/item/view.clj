(ns todoapp.item.view
  (:require [hiccup.page :refer [html5]]
            [hiccup.core :refer [html h]]))

(defn delete-item-form [item-id]
  (html 
   [:form {:method "POST" :action (str "/items/" item-id)}
    [:input {:type :hidden :name "_method" :value "DELETE"}]
    [:div.btn-group
     [:input.btn.btn-danger.btn-xs {:type :submit :value "Delete"}]]]))

(defn items-page [items]
  (html5 {:lang :en}
         [:head
          [:title "Todo App"]
          [:meta {:name :viewport
                  :content "width=device-width, initial-scale=1.0"}]
          [:link {:href "/bootstrap/css/bootstrap.min.css"
                  :rel :stylesheet}]
          [:body
           [:div.container
            [:div.row
             [:h1 "My Items"]
             (if (seq items)
               [:table.table.table-striped
                [:thead
                 [:tr
                  [:th "Remove"]
                  [:th "Name"]
                  [:th "Description"]]]
                [:tbody
                 (for [item items]
                   [:tr
                    [:td (delete-item-form (:id item))]
                    [:td (h (:name item))]
                    [:td (h (:description item))]])]]
               [:div.col-sm-offset-1 "There are no items."])]]
           [:script {:src "https://cdnjs.cloudflare.com/ajax/libs/jquery/3.3.1/jquery.min.js"}]
           [:script {:src "/bootstrap/js/bootstrap.min.js"}]]]))
