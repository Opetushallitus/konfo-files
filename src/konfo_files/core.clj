(ns konfo-files.core
  (:require
    [konfo-files.image_fetch :as img]
    [clj-log.access-log :refer [with-access-logging]]
    [compojure.api.sweet :refer :all]
    [ring.middleware.cors :refer [wrap-cors]]
    [ring.util.http-response :refer :all]
    [clojure.tools.logging :as log]
    [environ.core :refer [env]]))

(defn init []
  (intern 'clj-log.access-log 'service "konfo-files")
  (img/init-s3-connection))

(defn img-to-http-response [img]
  (if (nil? img)
    (not-found)
    {:status 200
     :headers {}
     :Content-type (:content-type img)
     :Content-lengt (:content-length img)
     ;Note! this stream comes directly from s3 and must be closed properly!
     ;Otherwise http connections to s3 are left open.
     ;Ring should close stream automatically after http response is sent.
     :body (:stream img)}))

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

      (context "/images" []
        (GET "/koulutus/:oid" [:as request]
          :summary "Hae koulutukseen liittyvä kuva"
          :path-params [oid :- String]
          :query-params [lang :- String]
          (with-access-logging request (img-to-http-response (img/fetch-koulutus-image oid lang))))

        (GET "/organisaatio/:oid" [:as request]
          :summary "Hae organisaatioon liittyvä kuva"
          :path-params [oid :- String]
          :query-params [lang :- String]
          (with-access-logging request (img-to-http-response (img/fetch-organisaatio-image oid lang)))))

      )))

(def app
  (wrap-cors konfo-api :access-control-allow-origin [#".*"] :access-control-allow-methods [:get]))
