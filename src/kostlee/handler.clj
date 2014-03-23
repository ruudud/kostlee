;;;; Main entry point for the Kostlee web server
(ns kostlee.handler
  (:use compojure.core)
  (:use ring.util.response)
  (:require [compojure.handler :as handler]
            [ring.middleware.json :as middleware]
            [compojure.route :as route]))

(defn uuid [] (str (java.util.UUID/randomUUID)))

(defn allow-cross-origin
  "middleware function to allow crosss origin"
  [handler]
  (fn [request]
   (let [response (handler request)]
    (assoc-in response [:headers "Access-Control-Allow-Origin"] "*"))))


;;; TODO Read in from somewhere™ at startup
(def daymoney-state (atom {
  "1" { :date "2014-01-27T21:19:37+0100" :people 16 :amount 2209.13M }
  "2" { :date "2014-01-28T21:19:37+0100" :people 16 :amount 2459.13M }
  "3" { :date "2014-01-29T21:19:37+0100" :people 16 :amount 2709.13M }
  "4" { :date "2014-01-30T21:19:37+0100" :people 17 :amount 2989M }}))

;;; API index provides links to resources
(defn index []
  (response {:links {:daymoneys "/daymoneys"}}))

;;; Daymoney handlers
(defn get-all-daymoneys []
  (response (map (fn [d] (assoc (second d) :id (first d)))
                 @daymoney-state)))

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
    (middleware/wrap-json-response)
    (allow-cross-origin)))
