(ns github-page.views
  (:require
   [re-frame.core :as re-frame]
   [github-page.subs :as subs]))

(defn main-panel []
  (let [name (re-frame/subscribe [::subs/name])]
    [:div
     [:h1
      "Henlo " @name]
     [:progress.progress.is-primary {:max "100", :value "15"} "15%"]]))
