(ns github-page.views.pages
  (:require
   [github-page.views.component :as component]
   [helix.core :refer [defnc $]]
   ["react-markdown" :default ReactMarkdown]
   [helix.dom :as d]))

(defnc home []
  (d/div
   {:class "container"}
   (d/div
    {:class "content"}
    (d/h1 "WIP"))))

(defnc about []
  (d/div
   {:class "container"}
   (d/div
    {:class ["columns" "m-0"]}
    (d/div
     {:class ["column" "is-half-tablet" "is-one-third-desktop"]}
     ($ component/user-card-))
    (d/div
     {:class "column"}
     (d/div
      {:class "content"}
      (d/h1 "About")
      (d/p "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."))))))

(def md
  "
# Test blog

testing markdown component

A paragraph with *emphasis* and **strong importance**.

> A block quote with ~strikethrough~ and a URL: https://reactjs.org.

* Lists
* [ ] todo
* [x] done

A table:

| a | b |
| - | - |
  ")

(defnc blog []
  (d/div {:class ["container" "content"]}
         ($ ReactMarkdown {:children md})))
