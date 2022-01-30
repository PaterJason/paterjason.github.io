(ns github-page.events
  (:require
   [ajax.core :as ajax]
   [day8.re-frame.http-fx]
   [github-page.db :as db]
   [re-frame.core :as rf]))

(rf/reg-event-db
 ::initialize-db
 (fn [_ _]
   db/default-db))

(rf/reg-event-fx
 ::navbar-toggle
 (fn [{:keys [db]} [_]]
   {:db (update db :navbar-burger-expanded not)}))

(rf/reg-event-fx
 ::on-nav
 (fn [{:keys [db]} [_ match history]]
   {:db (-> db
            (assoc :navbar-burger-expanded false)
            (assoc :current-route match))}))

(rf/reg-event-fx
 ::get-github
 (fn [{:keys [db]} _]
   {:http-xhrio {:method :get
                 :uri "https://api.github.com/users/paterjason"
                 :timeout 8000
                 :response-format (ajax/json-response-format {:keywords? true})
                 :on-success [::get-github-success]
                 :on-failure [::get-github-failure]}}))

(rf/reg-event-fx
 ::get-github-success
 (fn [{:keys [db]} [_ response]]
   {:db (assoc db :github response)}))

(rf/reg-event-fx
 ::get-github-failure
 (fn [{:keys [db]} [_ response]]
   (let [message "Failed to fetch GitHub profile"]
     {:db (-> db
              (assoc :github response)
              (assoc-in [:github :message] message))})))
