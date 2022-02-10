(ns github-page.views.pages
  (:require
   [github-page.views.component :as component]
   [helix.core :refer [$ defnc]]
   [helix.dom :as d]
   ["react-markdown" :default ReactMarkdown]
   [helix.hooks :as hooks]
   [ajax.core :as ajax]))

(defnc home []
  (d/div {:class ["container" "content"]}
         (d/h1 "WIP")))

(defnc about []
  (d/div {:class "container"}
         (d/div {:class ["columns" "m-0"]}
                (d/div {:class ["column" "is-half-tablet" "is-one-third-desktop"]}
                       ($ component/user-card))
                (d/div {:class ["column" "content"]}
                       (d/h1 "About")
                       (d/p "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.")))))

(defnc blog [{match :children}]
  (let [[md set-md] (hooks/use-state nil)
        id (get-in match [:path-params :id] "index")
        get-md (fn []
                 (set-md nil)
                 (ajax/GET
                   (str "assets/blog/" id ".md")
                   {:handler set-md
                    :error-handler set-md}))]
    (hooks/use-effect [id] (get-md))
    (condp apply [md]
      nil? (d/progress {:class "progress"})
      string? (d/div {:class "container"}
                     (d/div {:class ["content" "m-3"]}
                            ($ ReactMarkdown {:children md})))
      (d/div {:class "container"}
             (d/div {:class "m-3"}
                    ($ component/error-notification {:error md
                                                     :error-handler get-md}))))))
