(ns konfo-files.image_fetch
  (:require
    [clj-s3.s3-connect :as s3]
    [clj-log.error-log :refer [with-error-logging]]
    [clojure.tools.logging :as log]
    [ring.util.http-response :refer :all]
    [environ.core :refer [env]]))

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

