(ns github-page.routes
  (:require
   [github-page.events :as events]
   [reitit.core :as r]
   [reitit.frontend.easy :as rfe]
   [reitit.frontend.history :as rfh]
   [goog.object :as gobj]
   [re-frame.core :as re-frame]))

(def routes
  [["/" ::home]
   ["/about" ::about]])

(def router (r/router routes))

(defn on-navigate-fn [match history]
  (re-frame/dispatch [::events/nav match history]))

(rfe/start!
 router
 on-navigate-fn
 {:use-fragment false
  :ignore-anchor-click? (fn [router e el uri]
                           ;; Add additional check on top of the default checks
                          (and (rfh/ignore-anchor-click? router e el uri)
                               (not= "false" (gobj/get (.-dataset el) "reititHandleClick"))))})


