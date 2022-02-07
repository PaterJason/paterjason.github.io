(ns github-page.routes
  (:require
   [github-page.views.pages :as pages]
   [reitit.core :as r]))

(def routes
  ["/"
   [""
    {:name ::home
     :view pages/home}]
   ["about"
    {:name ::about
     :view pages/about}]
   ["blog"
    [""
     {:name ::blog-root
      :view pages/blog}]
    ["/:id"
     {:name ::blog
      :view pages/blog}]
    ]])

(def router (r/router routes))
