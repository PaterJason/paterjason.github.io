(ns github-page.core
  (:require
   [github-page.config :as config]
   [github-page.views :as views]
   [helix.core :refer [$]]
   ["react-dom" :as react-dom]))

(defn dev-setup []
  (when config/debug?
    (println "dev mode")))

(defn ^:dev/after-load mount-root []
  (react-dom/render ($ views/app) (js/document.getElementById "app")))

(defn init []
  (dev-setup)
  (mount-root))
