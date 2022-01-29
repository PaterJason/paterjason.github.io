(ns github-page.views
  (:require
   [reagent.core :as r]
   [re-frame.core :as re-frame]
   [reitit.frontend.easy :as rfe]
   [github-page.routes :as routes]
   [github-page.subs :as subs]))

(defn navbar []
  (r/with-let [burger-expanded (r/atom false)]
    [:nav.navbar.is-dark
     {:aria-label "main navigation"
      :role "navigation"}
     [:div.navbar-brand
      [:div.navbar-item.has-text-weight-bold "Jason Paterson"]
      [:a.navbar-burger
       {:class (when @burger-expanded
                 "is-active")
        :on-click (fn []
                    (swap! burger-expanded not))
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
        {:on-click #(rfe/push-state ::routes/home)}
        "Home"]
       [:a.navbar-item
        {:on-click #(rfe/push-state ::routes/about)}
        "About"]
       [:div.navbar-item.has-dropdown.is-hoverable
        [:a.navbar-link "More"]
        [:div.navbar-dropdown
         [:a.navbar-item "About"]
         [:a.navbar-item "Contact"]
         [:hr.navbar-divider]
         [:a.navbar-item "Report an issue"]]]]
      [:div.navbar-end]]]))

(defn main-panel []
  (let [name (re-frame/subscribe [::subs/name])]
    [:div
     [navbar]
     [:h1
      "Henlo " @name]
     [:progress.progress.is-primary {:max "100" :value "15"} "15%"]]))
