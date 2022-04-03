(ns github-page.views
  (:require
   [github-page.routes :as routes]
   [goog.object :as gobj]
   [helix.core :refer [$ <> defnc] :as helix]
   [helix.dom :as d]
   [helix.hooks :as hooks]
   [reitit.core :as r]
   [reitit.frontend.easy :as rfe]
   [reitit.frontend.history :as rfh]))

(defnc navbar [{:keys [match]}]
  (let [[burger set-burger] (hooks/use-state false)
        href (fn [route-name]
               (let [{:keys [title icon]} (:data (r/match-by-name routes/router route-name))]
                 (d/a {:class "navbar-item"
                       :href (rfe/href route-name)}
                      (d/span {:class "icon-text"}
                              (d/span {:class "icon"} (d/i {:class icon}))
                              (d/span title)))))]
    (hooks/use-effect
     [match]
     (set-burger false))
    (d/nav {:class ["navbar" "is-primary"]
            :aria-label "main navigation"
            :role "navigation"}
           (d/div {:class "navbar-brand"}
                  (d/div {:class ["navbar-item" "has-text-weight-bold"]} "Jason Paterson")
                  (d/a {:class ["navbar-burger" (when burger "is-active")]
                        :on-click #(set-burger not)
                        :aria-expanded "false"
                        :aria-label "menu"
                        :role "button"
                        :data-target "navbar-menu"}
                       (d/span {:aria-hidden "true"})
                       (d/span {:aria-hidden "true"})
                       (d/span {:aria-hidden "true"})))
           (d/div {:id "navbar-menu"
                   :class ["navbar-menu" (when burger "is-active")]}
                  (d/div {:class "navbar-start"}
                         (href ::routes/home)
                         (href ::routes/about)
                         (href ::routes/blog-index))
                  (d/div {:class "navbar-end"}
                         (d/a {:class "navbar-item"
                               :href "https://github.com/PaterJason/paterjason.github.io"}
                              (d/span {:class "icon-text"}
                                      (d/span {:class "icon"} (d/i {:class ["fab" "fa-github"]}))
                                      (d/span "Source"))))))))

(defnc router-component [{:keys [match]}]
  (when match
    (let [view (get-in match [:data :view])]
      ($ view match))))

(defnc app []
  (let [[match set-match] (hooks/use-state nil)
        on-navigate-fn (fn [next-match _history]
                         (set! js/document.title (str "Jason Paterson - " (get-in next-match [:data :title])))
                         (set-match next-match))
        router routes/router]
    (hooks/use-effect
     [router]
     (rfe/start!
      router
      on-navigate-fn
      {:use-fragment true
       :ignore-anchor-click? (fn [router e el uri]
                               (and (rfh/ignore-anchor-click? router e el uri)
                                    (not= "false" (gobj/get (.-dataset el) "reititHandleClick"))))}))
    (if match
      (<>
       ($ navbar {:match match})
       ($ router-component {:match match}))
      (d/div {:class "content"}
             (d/h1 "Router error")))))
