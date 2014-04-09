;;;; Handlers for the API endpoints
(ns kostlee.handlers
  (:use [kostlee.uuid]
        [kostlee.model :only [daymoney-state]]
        [ring.util.response])
  (:require [clj-time.format :as f]
            [clj-time.core :as t]))

(defn- all-daymoneys []
  (response (sort-by :date (map (fn [d] (assoc (second d) :id (first d)))
                                @daymoney-state))))

(defn daymoneys-per-weekday []
  (let [daymoneys-in-weekdays (group-by
                                (fn [d] (t/day-of-week (f/parse ((second d) :date))))
                                @daymoney-state)
        sorted-in-weekdays (into (sorted-map) daymoneys-in-weekdays)
        in-weekdays-sorted-list (map
                                  (fn [dow] (map (fn [d] (second d))
                                                 (second dow)))
                                  sorted-in-weekdays)]
    (response (map
                (fn [dow] (reduce + (map (fn [d] (:increase d)) dow)))
                in-weekdays-sorted-list))))


(defn get-all-daymoneys [params]
  (if (= (params :weekday) "1")
   (daymoneys-per-weekday)
   (all-daymoneys)))

(defn get-daymoney [id]
  (let [daymoney (get @daymoney-state id)]
    (cond
      (empty? daymoney) { :status 404 }
      :else (response (assoc daymoney :id id)))))

;TODO validate input
(defn create-new-daymoney [daymoney]
  (let [id (uuid)]
    (reset! daymoney-state (merge @daymoney-state { id daymoney }))
    (get-daymoney id)))

;FIXME not implemented
(defn update-daymoney [id daymoney]
  {:status 501})

;FIXME not implemented
(defn delete-daymoney [id]
  {:status 501})

