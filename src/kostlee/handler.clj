;;;; Main entry point for the Kostlee web server
(ns kostlee.handler
  (:use compojure.core)
  (:use ring.util.response)
  (:require [compojure.handler :as handler]
            [ring.middleware.json :as middleware]
            [compojure.route :as route]))

(defn uuid [] (str (java.util.UUID/randomUUID)))

;;; TODO Read in from somewhere™ at startup
(def daymoney-state (atom [
  {:id "1" :date "2014-01-27" :people 16 :amount 2609.13M}
  {:id "2" :date "2014-01-28" :people 16 :amount 2609.13M}
  {:id "3" :date "2014-01-29" :people 16 :amount 2709.13M}
  {:id "4" :date "2014-01-30" :people 17 :amount 2739.13M}]))

;;; API index provides links to resources
(defn index []
  (response {:links {:daymoneys "/daymoneys"}}))

;;; Daymoney handlers
(defn get-all-daymoneys []
  (response @daymoney-state))

(defn get-daymoney [id]
  (let [daymoney (get (group-by :id @daymoney-state) id)]
    (cond
      (empty? daymoney) {:status 404}
      :else (response (first daymoney)))))

;TODO validate input
(defn create-new-daymoney [daymoney]
  (let [id (uuid)]
    (swap! daymoney-state conj (assoc daymoney :id id))
    (get-daymoney id)))

;FIXME not implemented
(defn update-daymoney [id daymoney]
  (get-daymoney id))

;FIXME not implemented
(defn delete-daymoney [id]
  {:status 204})


(defroutes api-routes
  (GET "/" [] (index))
  (context "/daymoneys" []
    (GET "/" [] (get-all-daymoneys))
    (POST "/" {body :body} (create-new-daymoney body))
    (context "/:id" [id]
      (GET    "/" [] (get-daymoney id))
      (PUT    "/" {body :body} (update-daymoney id body))
      (DELETE "/" [] (delete-daymoney id))))
  (route/not-found "Not Found"))

(def app
  (-> (handler/api api-routes)
    (middleware/wrap-json-body)
    (middleware/wrap-json-response)))
