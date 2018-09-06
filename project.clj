(defproject konfo-files "0.1.0-SNAPSHOT"
  :description "Konfo-files"
  :repositories [["oph-releases" "https://artifactory.opintopolku.fi/artifactory/oph-sade-release-local"]
                 ["oph-snapshots" "https://artifactory.opintopolku.fi/artifactory/oph-sade-snapshot-local"]
                 ["ext-snapshots" "https://artifactory.opintopolku.fi/artifactory/ext-snapshot-local"]]
  :dependencies [[org.clojure/clojure "1.8.0"]
                 ; Rest + server
                 [metosin/compojure-api "1.1.11"]
                 [compojure "1.6.0"]
                 [ring/ring-core "1.6.3"]
                 [ring/ring-jetty-adapter "1.6.3"]
                 [ring-cors "0.1.11"]
                 ; Logging
                 [oph/clj-log "0.1.0-SNAPSHOT"]
                 [org.clojure/tools.logging "0.4.0"]
                 [org.apache.logging.log4j/log4j-api "2.9.0"]
                 [org.apache.logging.log4j/log4j-core "2.9.0"]
                 [org.apache.logging.log4j/log4j-slf4j-impl "2.9.0"]
                 [clj-log4j2 "0.2.0"]
                 ; Configuration
                 [fi.vm.sade.java-utils/java-properties "0.1.0-SNAPSHOT"]
                 [environ "1.1.0"]
                 [cprop "0.1.10"]
                 ;S3
                 [oph/clj-s3 "0.1.0-SNAPSHOT"]]
  :ring {:handler konfo-files.core/app
         :init konfo-files.core/init
         ;:destroy konfo-files.core/destroy
         :browser-uri "konfo-files"}
  :jvm-opts ["-Dlog4j.configurationFile=test/resources/log4j2.properties" "-Dconf=dev-configuration/konfo-files.edn"]
  :target-path "target/%s"
  :plugins [[lein-ring "0.12.4"]
            [lein-environ "1.1.0"]]
  :profiles {:test {:dependencies [[ring/ring-mock "0.3.2"]
                                   [oph/clj-test-utils "0.1.0-SNAPSHOT"]]}
             :uberjar {:ring {:port 8080}}}
  :aliases {"run" ["ring" "server" "3007"]
            "uberjar" ["do" "clean" ["ring" "uberjar"]]})
