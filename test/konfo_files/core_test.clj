(ns konfo-files.core-test
  (:require [clojure.test :refer :all]
            [konfo-files.core :refer :all]
            [ring.mock.request :as mock]))

(intern 'clj-log.access-log 'service "konfo-files")

(deftest healthcheck-test
  (testing "Healthcheck API test"
    (let [response (app (mock/request :get "/konfo-files/healthcheck"))]
      (is (= 200 (:status response))))))
