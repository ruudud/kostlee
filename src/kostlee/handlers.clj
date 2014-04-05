(ns kostlee.handlers
  (:use [kostlee.uuid]
        [kostlee.model :only [daymoney-state]]
        [ring.util.response]))

;;; Daymoney handlers
(defn get-all-daymoneys []
  (response (sort-by :date (map (fn [d] (assoc (second d) :id (first d)))
                      @daymoney-state))))

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

