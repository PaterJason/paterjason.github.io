(ns github-page.events
  (:require
   [re-frame.core :as rf]
   [github-page.db :as db]))

(rf/reg-event-db
 ::initialize-db
 (fn [_ _]
   db/default-db))

(rf/reg-event-fx
 ::navbar-toggle
 (fn [{:keys [db]} [_]]
   {:db (update db :navbar-burger-expanded not)}))

(rf/reg-event-fx
 ::nav
 (fn [{:keys [db]} [_ match history]]
   {:db (-> db
            (assoc :navbar-burger-expanded false)
            (assoc :name (get-in match [:data :name])))}))
