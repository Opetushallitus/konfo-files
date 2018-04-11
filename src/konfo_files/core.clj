(ns konfo-files.core
  (:require
    [clj-log.access-log :refer [with-access-logging]]
    [compojure.api.sweet :refer :all]
    [ring.middleware.cors :refer [wrap-cors]]
    [ring.util.http-response :refer :all]
    [clojure.tools.logging :as log]
    [environ.core :refer [env]]))

(defn init []
  (intern 'clj-log.access-log 'service "konfo-files")
  ;(intern 'clj-log.error-log 'test false)
  )

(def konfo-api
  (api
    {:swagger {:ui   "/konfo-files"
               :spec "/konfo-files/swagger.json"
               :data {:info {:title       "Konfo-files"
                             :description "File service for Konfo koulutusinformaatio UI."}}}
     ;;:exceptions {:handlers {:compojure.api.exception/default logging/error-handler*}}
     }
    (context "/konfo-files" []
      (GET "/healthcheck" [:as request]
        :summary "Healthcheck API"
        (with-access-logging request (ok "OK")))

      )))

(def app
  (wrap-cors konfo-api :access-control-allow-origin [#".*"] :access-control-allow-methods [:get]))
