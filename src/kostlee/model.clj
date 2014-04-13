(ns kostlee.model
  (:use kostlee.uuid)
  (:require [clojure.java.io :as io]
            [clojure.data.csv :as csv]
            [clj-time.format :as f]
            [clj-time.core :as t]))

(def daymoney-state
  "Global state keeping the data"
  (atom {
    "8" { :date "2014-01-27T21:19:37+0100" :people 16 :amount 1989M :increase 1M }
    "4" { :date "2014-01-28T21:19:37+0100" :people 16 :amount 1989M :increase 0M }
    "9" { :date "2014-01-29T21:19:37+0100" :people 16 :amount 1989M :increase 10M }
    "0" { :date "2014-01-30T21:19:37+0100" :people 17 :amount 2989M :increase 0M }
    "1" { :date "2014-01-31T21:19:37+0100" :people 18 :amount 3989M :increase 1000M }
    "2" { :date "2014-02-01T21:19:37+0100" :people 18 :amount 3989M :increase 10M }
    "5" { :date "2014-02-02T21:19:37+0100" :people 18 :amount 3989M :increase 5M }
    "3" { :date "2014-02-03T21:19:37+0100" :people 18 :amount 3989M :increase 0M }}))

(defn daymoneys-list []
  (map (fn [d] (assoc (second d) :id (first d)))
       @daymoney-state))

(defn daymoneys-sorted []
  (sort-by :date (daymoneys-list)))

(defn daymoneys-per-weekday []
  (let [daymoneys-in-weekdays (group-by
                                (fn [d] (t/day-of-week (f/parse (d :date))))
                                (vals @daymoney-state))
        sorted-in-weekdays (vals (into (sorted-map) daymoneys-in-weekdays))]
    (map (fn [dow] (reduce + (map (fn [d] (:increase d)) dow)))
         sorted-in-weekdays)))

(defn avg-daymoneys-per-people []
  (let [daymoneys (vals @daymoney-state)]
    (reduce + (map (fn [d] (with-precision 10 (/ (d :increase) (d :people))))
                   daymoneys))))

(defn avg-daymoneys-per-day []
  (let [daymoneys (sort-by :date (vals @daymoney-state))
        first-daymoney (first daymoneys)
        last-daymoney (last daymoneys)
        sum-amount (- (last-daymoney :amount) 
                      (first-daymoney :amount))
        num-days (t/in-days (t/interval (f/parse (first-daymoney :date))
                                        (f/parse (last-daymoney :date))))]
    (with-precision 10 (/ sum-amount num-days))))

(defn max-daymoneys-per-day []
  (apply max-key :increase (daymoneys-list)))

(defn max-daymoneys-per-people []
  (let [increases (map (fn [d] (assoc d 
                         :increasePerPeople (with-precision 10 
                                   (/ (d :increase) (d :people)))))
                       (daymoneys-list))]
    (apply max-key :increasePerPeople increases)))

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

