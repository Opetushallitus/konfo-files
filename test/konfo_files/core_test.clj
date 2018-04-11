(ns konfo-files.core-test
  (:require [clj-s3.s3-connect :as s3]
            [clj-test-utils.s3-mock-utils :refer :all]
            [clojure.test :refer :all]
            [konfo-files.core :refer :all]
            [ring.mock.request :as mock]))

(intern 'clj-log.access-log 'service "konfo-files")

(defonce koulutus-oid "1.2.3.456")
(defonce organisaatio-oid "1.2.3.4.5.6")

(defn create-mock-data-fixture [f]
  (s3/upload (.getBytes "moi") "text/plain" "moi.txt" "koulutus" koulutus-oid "kieli_fi")
  (s3/upload (.getBytes "hej") "text/plain" "moi.txt" "koulutus" koulutus-oid "kieli_sv")
  (s3/upload (.getBytes "koulu") "text/plain" "koulu.txt" "organisaatio" organisaatio-oid)
  (f))

(use-fixtures :once mock-s3-fixture create-mock-data-fixture)

(deftest healthcheck-test
  (testing "Healthcheck API test"
    (let [response (app (mock/request :get "/konfo-files/healthcheck"))]
      (is (= 200 (:status response)))))
  (testing "Fetch koulutus image"
    (let [response (app (mock/request :get (str "/konfo-files/images/koulutus/" koulutus-oid "?lang=fi")))]
      (is (= 200 (:status response)))
      (is (= "moi" (slurp (:body response))))))
  (testing "Fetch organisaatio image"
    (let [response (app (mock/request :get (str "/konfo-files/images/organisaatio/" organisaatio-oid "?lang=fi")))]
      (is (= 200 (:status response)))
      (is (= "koulu" (slurp (:body response))))))
  (testing "No image found"
    (let [response (app (mock/request :get (str "/konfo-files/images/koulutus/foo?lang=fi")))]
      (is (= 404 (:status response))))))
