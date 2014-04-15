(defproject kostlee "0.1.0-SNAPSHOT"
  :description "List data from grasrotandelen"
  :url "https://github.com/ruudud/kostlee"
  :main kostlee.core
  :license {:name "MIT"
            :url "http://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/data.csv "0.1.2"]
                 [clj-time "0.6.0"]
                 [selmer "0.6.5"]
                 [ring/ring-json "0.3.0"]
                 [ring "1.2.2"]
                 [compojure "1.1.6"]]
  :plugins [[lein-ring "0.8.10"]]
  :profiles {:dev {:dependencies [[ring-mock "0.1.5"]]}
             :uberjar {:aot :all}})

