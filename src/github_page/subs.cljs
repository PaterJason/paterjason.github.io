(ns github-page.subs
  (:require
   [github-page.events :as events]
   [re-frame.core :as rf]
   [reagent.ratom :as ratom]))

(rf/reg-sub
 ::current-route
 (fn [db]
   (:current-route db)))

(rf/reg-sub
 ::burger-expanded
 (fn [db]
   (:navbar-burger-expanded db)))

(rf/reg-sub-raw
 ::github
 (fn [app-db]
   (when (-> @app-db :github nil?)
     (rf/dispatch [::events/get-github]))
   (ratom/make-reaction
    (fn [] (get @app-db :github)))))
