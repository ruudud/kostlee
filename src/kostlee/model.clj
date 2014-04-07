(ns kostlee.model
  (:use kostlee.uuid)
  (:require [clojure.java.io :as io]
            [clojure.data.csv :as csv]))

(def daymoney-state
  "Global state keeping the data"
  (atom {
    "1" { :date "2014-01-31T21:19:37+0100" :people 18 :amount 3989M :increase 1000M }
    "4" { :date "2014-01-28T21:19:37+0100" :people 16 :amount 1989M :increase 0M }
    "0" { :date "2014-01-30T21:19:37+0100" :people 17 :amount 2989M :increase 0M }}))

(defn- read-csv [data-file]
  (with-open [in-file (io/reader data-file)]
    (doall
      (csv/read-csv in-file))))

(defn- parseRow
  "Apply data types to read in row"
  [r]
  [(r 0)
   (bigdec (r 1))
   (Integer/parseInt (r 2))
   (bigdec (r 3))])

(defn- report-data-stats
  "Print out some stats on the data read in"
  []
  (println (clojure.string/join " " ["Read in"
                                     (count @daymoney-state)
                                     "items from CSV…"])))
(defn read-data-from-csv
  "Read in data from given CSV file path"
  [data-file]
  (reset! daymoney-state
    (apply conj (map (fn [o] { (uuid) o })
                     (map
                       (fn [m] (zipmap [:date :amount :people :increase] 
                                       (parseRow m)))
                       (read-csv data-file)))))
  (report-data-stats))

