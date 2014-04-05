;;;; Main entry point for the Kostlee web server
(ns kostlee.core
  (:gen-class)
  (:use compojure.core)
  (:require [kostlee [ring-jetty-adapter :as jetty]
                     [model :as model]
                     [handlers :as handlers]]
            [compojure.handler :as handler]
            [ring.middleware.json :as middleware]
            [ring.util.response :as resp]
            [compojure.route :as route]))

(defn allow-cross-origin
  "middleware function to allow cross origin requests"
  [handler]
  (fn [request]
   (let [response (handler request)]
    (assoc-in response [:headers "Access-Control-Allow-Origin"] "*"))))

(defroutes api-routes
  (GET "/" [] (resp/resource-response "index.html" {:root "public"}))
  (context "/daymoneys" []
    (GET "/" [] (handlers/get-all-daymoneys))
    (POST "/" {body :body} (handlers/create-new-daymoney body))
    (context "/:id" [id]
      (GET    "/" [] (handlers/get-daymoney id))
      (PUT    "/" {body :body} (handlers/update-daymoney id body))
      (DELETE "/" [] (handlers/delete-daymoney id))))
  (route/resources "/")
  (route/not-found "Not Found"))

(def app (-> (handler/api api-routes)
             (middleware/wrap-json-body)
             (middleware/wrap-json-response)
             (allow-cross-origin)))

(defn init "Executed on startup." []
  (model/read-data-from-csv (or (System/getenv "DATAFILE")
                                "/srv/leemoney.dat")))

(defn -main [& args]
  (init)
  (jetty/run-jetty app {:port (Integer/parseInt (or (System/getenv "PORT")
                                                    "3000"))}))
