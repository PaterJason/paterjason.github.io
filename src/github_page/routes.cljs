(ns github-page.routes
  (:require
   [github-page.views.pages :as pages]
   [reitit.core :as r]))

(def routes
  ["/"
   [""
    {:name ::home
     :title "Home"
     :icon ["fas" "fa-home"]
     :view pages/home}]
   ["about"
    {:name ::about
     :title "About"
     :icon ["fas" "fa-info"]
     :view pages/about}]
   ["blog"
    {:title "Blog"
     :icon ["fas" "fa-blog"]
     :view pages/blog}
    ["" {:name ::blog-index}]
    ["/:id" {:name ::blog}]]])

(def router (r/router routes))
