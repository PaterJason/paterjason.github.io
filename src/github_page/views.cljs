(ns github-page.views
  (:require
   [re-frame.core :as re-frame]
   [reitit.frontend.easy :as rfe]
   [github-page.routes :as routes]
   [github-page.subs :as subs]
   [github-page.events :as events]))

(defn navbar []
  (let [burger-expanded (re-frame/subscribe [::subs/burger-expanded])]
    [:nav.navbar.is-primary
     {:aria-label "main navigation"
      :role "navigation"}
     [:div.navbar-brand
      [:div.navbar-item.has-text-weight-bold "Jason Paterson"]
      [:a.navbar-burger
       {:class (when @burger-expanded
                 "is-active")
        :on-click (fn []
                    (re-frame/dispatch [::events/navbar-toggle]))
        :aria-expanded "false"
        :aria-label "menu"
        :role "button"
        :data-target "navbar-menu"}

       [:span {:aria-hidden "true"}]
       [:span {:aria-hidden "true"}]
       [:span {:aria-hidden "true"}]]]
     [:div.navbar-menu
      {:id "navbar-menu"
       :class (when @burger-expanded
                "is-active")}
      [:div.navbar-start
       [:a.navbar-item
        {:href (rfe/href ::routes/home)}
        "Home"]
       [:a.navbar-item
        {:href (rfe/href ::routes/about)}
        "About"]
       [:div.navbar-item.has-dropdown.is-hoverable
        [:a.navbar-link "More"]
        [:div.navbar-dropdown
         [:a.navbar-item "About"]
         [:a.navbar-item "Contact"]
         [:hr.navbar-divider]
         [:a.navbar-item "Report an issue"]]]]
      [:div.navbar-end]]]))

(defn router-component []
  (let [current-route @(re-frame/subscribe [::subs/current-route])]
    [:div
     (when current-route
       [(get-in current-route [:data :view])])]))

(defn main-panel []
  [:div
   [navbar]
   [router-component]])
