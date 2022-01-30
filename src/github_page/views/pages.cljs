(ns github-page.views.pages
  (:require [github-page.views.component :as component]))

(defn home []
  [:div
   [:div.container
    [:p.title "WIP"]]])

(defn about []
  [:div.container
   [:div.columns.m-0
    [:div.column.is-half-tablet.is-one-third-desktop
     [component/user-card]]
    [:div.column
     [:p.title "ABOUT"]
     [:p
      "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."]]]])
