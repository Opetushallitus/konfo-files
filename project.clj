(defproject konfo-files "0.1.0-SNAPSHOT"
  :description "Konfo-files"
  :repositories [["oph-releases" "https://artifactory.opintopolku.fi/artifactory/oph-sade-release-local"]
                 ["oph-snapshots" "https://artifactory.opintopolku.fi/artifactory/oph-sade-snapshot-local"]
                 ["ext-snapshots" "https://artifactory.opintopolku.fi/artifactory/ext-snapshot-local"]]
  :managed-dependencies [[org.flatland/ordered "1.5.7"]]
  :dependencies [[org.clojure/clojure "1.8.0"]
                 ; Rest + server
                 [metosin/compojure-api "1.1.11"]
                 [compojure "1.6.0"]
                 [ring/ring-core "1.6.3"]
                 [ring/ring-jetty-adapter "1.6.3"]
                 [ring-cors "0.1.11"]
                 ; Logging
                 [oph/clj-log "0.2.2-SNAPSHOT"]
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
                 [oph/clj-s3 "0.2.2-SNAPSHOT"]
                 ; JAXB-fix for Java >= 9
                 [javax.xml.bind/jaxb-api "2.2.11"]
                 [com.sun.xml.bind/jaxb-core "2.2.11"]
                 [com.sun.xml.bind/jaxb-impl "2.2.11"]
                 [javax.activation/activation "1.1.1"]]
  :ring {:handler konfo-files.core/app
         :init konfo-files.core/init
         ;:destroy konfo-files.core/destroy
         :browser-uri "konfo-files"}
  :jvm-opts ["-Dlog4j.configurationFile=test/resources/log4j2.properties" "-Dconf=dev-configuration/konfo-files.edn"]
  :target-path "target/%s"
  :plugins [[lein-ring "0.12.4"]
            [lein-environ "1.1.0"]]
  :profiles {:test {:dependencies [[ring/ring-mock "0.3.2"]
                                   [oph/clj-test-utils "0.2.3-SNAPSHOT"]]}
             :ci-test {:dependencies [[ring/ring-mock "0.3.2"]
                                     [oph/clj-test-utils "0.2.3-SNAPSHOT"]]
                       :jvm-opts ["-Dlog4j.configurationFile=test/resources/log4j2.properties" "-Dconf=ci-configuration/konfo-files.edn"]}
             :uberjar {:ring {:port 8080}}}
  :aliases {"run" ["ring" "server" "3007"]
            "ci-test" ["with-profile" "+ci-test" "test"]
            "uberjar" ["do" "clean" ["ring" "uberjar"]]})
