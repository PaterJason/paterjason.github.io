(ns github-page.core
  (:require
   [github-page.config :as config]
   [github-page.views :as views]
   [helix.core :refer [$]]
   ["react-dom/client" :refer [createRoot]]))

(defn dev-setup []
  (when config/debug?
    (println "dev mode")))

(defn ^:dev/after-load mount-root []
  (let [container (js/document.getElementById "app")
        root (createRoot container)]
    (.render root ($ views/app))))

(defn init []
  (dev-setup)
  (mount-root))
