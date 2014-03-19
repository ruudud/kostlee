(defproject kostlee "0.1.0-SNAPSHOT"
  :description "list data from grasrotandelen"
  :url "https://github.com/ruudud/kostlee"
  :license {:name "MIT"
            :url "http://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [ring/ring-json "0.3.0"]
                 [compojure "1.1.6"]]
  :plugins [[lein-ring "0.8.10"]]
  :ring {:handler kostlee.handler/app}
  :profiles {:dev {:dependencies [[ring-mock "0.1.5"]]}
             :uberjar {:aot :all}})

