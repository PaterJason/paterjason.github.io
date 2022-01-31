(ns github-page.views.component
  (:require
   [re-frame.core :as rf]
   [github-page.subs :as subs]
   [github-page.events :as events]))

(defn user-card []
  (let [github @(rf/subscribe [::subs/github])]
    (condp apply [github]
      nil? [:progress.progress]
      :failure [:div.notification.is-danger
                [:p.title (:message github)]
                [:p.is-family-monospace (:debug-message github)]
                [:button.button
                 {:on-click #(rf/dispatch [::events/get-github])}
                 [:span.icon-text
                  [:span.icon [:i.fas.fa-sync]]
                  [:span "Retry"]]]]
      [:div.card
       [:div.card-image
        [:figure.image.is-square
         [:img {:src (:avatar_url github)}]]]
       [:div.card-content
        [:h1.title (:name github)]
        [:ul
         [:li
          [:a {:href "mailto:jasonbipaterson@gmail.com"}
           [:span.icon-text
            [:span.icon [:i.fas.fa-envelope]]
            [:span "jasonbipaterson@gmail.com"]]]]
         [:li
          [:a {:href (:html_url github)}
           [:span.icon-text
            [:span.icon [:i.fab.fa-github]]
            [:span "Github"]]]]
         [:li
          [:a {:href "https://www.linkedin.com/in/jason-paterson-470642146/"}
           [:span.icon-text
            [:span.icon [:i.fab.fa-linkedin]]
            [:span "LinkedIn"]]]]
         [:li
          [:span.icon-text
           [:span.icon [:i.fas.fa-map-marker-alt]]
           [:span (:location github)]]]]]])))
