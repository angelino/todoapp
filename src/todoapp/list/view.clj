(ns todoapp.list.view
  (:require [hiccup.page :refer [html5]]
            [hiccup.core :refer [h html]]))

(defn lists-ul [lists]
  (html
   [:ul
    (for [list lists]
      [:li
       [:a {:href (str "/lists/" (h (:id list)) "/items")}
        (h (:name list))]
       [:span.badge.badge-info
        (h (:items-left list))]])]))

(defn lists-page [lists]
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
             [:h1 "My Lists"]]
            [:div.row
             (lists-ul lists)]]
           [:script {:src "https://cdnjs.cloudflare.com/ajax/libs/jquery/3.3.1/jquery.min.js"}]
           [:script {:src "/bootstrap/js/bootstrap.min.js"}]]]))
