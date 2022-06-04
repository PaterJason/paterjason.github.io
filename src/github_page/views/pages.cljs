(ns github-page.views.pages
  (:require
   [ajax.core :as ajax]
   [github-page.views.component :as component]
   [helix.core :refer [$ defnc]]
   [helix.dom :as d]
   [helix.hooks :as hooks]
   ["react-markdown" :default ReactMarkdown]))

(defnc home []
  (d/div {:class ["has-text-centered" "is-flex" "is-justify-content-center"]}
         (d/div {:style {:position "absolute"
                         :bottom "50vh"
                         :animation-name "slide-in"
                         :animation-duration "1.5s"
                         :animation-timing-function "ease-out"}}
                (d/h1 {:class ["title" "is-size-1"]}
                      "Jason Paterson")
                (d/h2 {:class ["subtitle"  "has-text-grey"]}
                      "Full Stack Engineer"))))

(defnc about []
  (d/div {:class "container"}
         (d/div {:class ["columns" "m-0"]}
                (d/div {:class ["column" "is-half-tablet" "is-one-third-desktop"]}
                       ($ component/user-card))
                (d/div {:class ["column" "content"]}
                       ($ ReactMarkdown {:children "
# About

I am a full stack developer specialising in Clojure/ClojureScript.

"})))))

(defnc blog [{:keys [match]}]
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
