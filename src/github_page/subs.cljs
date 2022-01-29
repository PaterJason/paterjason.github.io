(ns github-page.subs
  (:require
   [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::name
 (fn [db]
   (:name db)))

(re-frame/reg-sub
 ::burger-expanded
 (fn [db]
   (:navbar-burger-expanded db)))
