(ns github-page.views.component
  (:require
   [ajax.core :as ajax]
   [helix.core :refer [defnc $]]
   [helix.dom :as d]
   [helix.hooks :as hooks]))

(defnc error-notification [{:keys [error on-click]}]
  (d/div {:class ["notification" "is-danger"]}
         (d/h1 {:class "title"} (:status error))
         (d/h2 {:class "subtitle"} (:status-text error))
         (d/button {:class ["button" "is-danger" "is-inverted"]
                    :on-click on-click}
                   (d/span {:class ["icon"]} (d/i {:class ["fas" "fa-sync"]}))
                   (d/span "Retry"))))

(defnc user-card []
  (let [[github set-github] (hooks/use-state nil)
        get-github (fn []
                     (set-github nil)
                     (ajax/GET "https://api.github.com/users/paterjason"
                       {:handler set-github
                        :error-handler set-github
                        :response-format (ajax/json-response-format {:keywords? true})}))]
    (hooks/use-effect :once (get-github))
    (condp apply [github]
      nil? (d/progress {:class "progress"})
      :failure ($ error-notification {:error github
                                      :on-click get-github})
      (d/div {:class "card"}
             (d/div {:class "card-image"}
                    (d/figure {:class ["image" "is-square"]}
                              (d/img {:src (:avatar_url github)})))
             (d/div {:class "card-content"}
                    (d/h1 {:class "title"} (:name github))
                    (d/ul
                     (d/li
                      (d/a {:href "mailto:jasonbipaterson@gmail.com"}
                           (d/span {:class ["icon-text"]}
                                   (d/span {:class ["icon"]} (d/i {:class ["fas" "fa-envelope"]}))
                                   (d/span "jasonbipaterson@gmail.com"))))
                     (d/li
                      (d/a {:href (:html_url github)}
                           (d/span {:class ["icon-text"]}
                                   (d/span {:class ["icon"]} (d/i {:class ["fab" "fa-github"]}))
                                   (d/span "GitHub"))))
                     (d/li
                      (d/a {:href "https://www.linkedin.com/in/jason-paterson-470642146/"}
                           (d/span {:class ["icon-text"]}
                                   (d/span {:class ["icon"]} (d/i {:class ["fab" "fa-linkedin"]}))
                                   (d/span "LinkedIn"))))
                     (d/li
                      (d/span {:class ["icon-text"]}
                              (d/span {:class ["icon"]} (d/i {:class ["fas" "fa-map-marker"]}))
                              (d/span (:location github))))))))))
