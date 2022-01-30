(ns github-page.views.component
  (:require
   [re-frame.core :as rf]
   [github-page.subs :as subs]))

(defn user-card []
  (let [github @(rf/subscribe [::subs/github])]
    [:div.card
     [:div.card-image
      [:figure.image
       [:img {:src (:avatar_url github)}]]]
     [:div.card-content
      [:p.title (:name github)]
      [:p.subtitle (:location github)]
      [:ul
       [:li
        [:a {:href "mailto:jasonbipaterson@gmail.com"}
         [:span.icon [:i.fas.fa-envelope]]
         "jasonbipaterson@gmail.com"]]
       [:li
        [:a {:href (:html_url github)}
         [:span.icon [:i.fab.fa-github]]
         "Github"]]
       [:li
        [:a {:href "https://www.linkedin.com/in/jason-paterson-470642146/"}
         [:span.icon [:i.fab.fa-linkedin]]
         "LinkedIn"]]]]]))
