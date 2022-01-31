(ns github-page.routes
  (:require
   [github-page.events :as events]
   [github-page.views.pages :as pages]
   [goog.object :as gobj]
   [re-frame.core :as rf]
   [reitit.core :as r]
   [reitit.frontend.easy :as rfe]
   [reitit.frontend.history :as rfh]))

(def routes
  ["/"
   [""
    {:name ::home
     :view pages/home}]
   ["about"
    {:name ::about
     :view pages/about}]
   ["blog"
    {:name ::blog
     :view pages/blog}]])

(def router (r/router routes))

(defn on-navigate-fn [match history]
  (rf/dispatch [::events/on-nav match history]))

(defn init! []
  (rfe/start!
   router
   on-navigate-fn
   {:use-fragment true
    :ignore-anchor-click? (fn [router e el uri]
                            (and (rfh/ignore-anchor-click? router e el uri)
                                 (not= "false" (gobj/get (.-dataset el) "reititHandleClick"))))}))
