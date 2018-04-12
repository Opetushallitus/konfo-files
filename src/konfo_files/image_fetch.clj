(ns konfo-files.image_fetch
  (:require
    [clj-s3.s3-connect :as s3]
    [konfo-files.config :refer [config]]
    [clj-log.error-log :refer [with-error-logging]]
    [clojure.tools.logging :as log]
    [ring.util.http-response :refer :all]
    [environ.core :refer [env]]))

(defn init-s3-connection []
  (if-let [s3-region (:s3-region config)]
    (intern 'clj-s3.s3-connect 's3-region s3-region)
    (throw (IllegalStateException. "Could not read s3-region from configuration!")))
  (if-let [s3-bucket (:s3-bucket config)]
    (intern 'clj-s3.s3-connect 's3-bucket s3-bucket)
    (throw (IllegalStateException. "Could not read s3-bucket from configuration!")))
  (s3/init-s3-client))

(defn- fetch-by-first-key [keys]
  (if (not (empty? keys))
    (s3/download (first keys))
    nil))

(defn fetch-koulutus-image [oid lang]
  (with-error-logging
    (let [keys (s3/list-keys "koulutus" oid (str "kieli_" (.toLowerCase lang)))]
      (if (< 1 (count keys)) (log/warn (str "Koulutukselle " oid " löytyi useampi kuin yksi kuva kielellä " lang "!")))
      (fetch-by-first-key keys))))

(defn fetch-organisaatio-image [oid lang]
  (with-error-logging
    (let [keys (s3/list-keys "organisaatio" oid)]
      (if (< 1 (count keys)) (log/warn (str "Organisaatiolle " oid " löytyi useampi kuin yksi kuva!")))
      (fetch-by-first-key keys))))

