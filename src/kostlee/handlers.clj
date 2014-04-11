;;;; Handlers for the API endpoints
(ns kostlee.handlers
  (:use [kostlee.uuid]
        [ring.util.response])
  (:require [kostlee.model :as m]))


(defn get-all-daymoneys [params]
  (cond
    (= (params :weekday) "1") (response (m/daymoneys-per-weekday))
    (= (params :transform) "per-weekday") (response (m/daymoneys-per-weekday))
    (= (params :transform) "avg") (response {:perDay (m/avg-daymoneys-per-day)
                                             :perPeople (m/avg-daymoneys-per-people)})
    :else (response (m/daymoneys-sorted))))

(defn get-daymoney [id]
  (let [daymoney (get @m/daymoney-state id)]
    (cond
      (empty? daymoney) { :status 404 }
      :else (response (assoc daymoney :id id)))))

;TODO validate input
(defn create-new-daymoney [daymoney]
  (let [id (uuid)]
    (reset! m/daymoney-state (merge @m/daymoney-state { id daymoney }))
    (get-daymoney id)))

;FIXME not implemented
(defn update-daymoney [id daymoney]
  {:status 501})

;FIXME not implemented
(defn delete-daymoney [id]
  {:status 501})

