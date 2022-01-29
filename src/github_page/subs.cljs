(ns github-page.subs
  (:require
   [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::current-route
 (fn [db]
   (:current-route db)))

(re-frame/reg-sub
 ::burger-expanded
 (fn [db]
   (:navbar-burger-expanded db)))
