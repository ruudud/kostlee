;;;; Main entry point for the Kostlee web server
(ns kostlee.core
  (:gen-class)
  (:use compojure.core)
  (:use ring.middleware.keyword-params)
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

(defmacro redef
  "Redefine an existing value, keeping the metadata intact."
  [name value]
  `(let [m# (meta #'~name)
         v# (def ~name ~value)]
     (alter-meta! v# merge m#)
     v#))

(defmacro decorate-with
  "Wrap multiple functions in a single decorator."
  [decorator & funcs]
  `(do ~@(for [f funcs]
          `(redef ~f (-> ~f ~decorator)))))

(defn- with-header [handler header value]
  (fn [request]
    (when-let [response (handler request)]
      (assoc-in response [:headers header] value))))

(defroutes css-vendor-resource-routes
  (route/resources "/css/vendor" { :root "public/css/vendor" }))

(defroutes js-vendor-resource-routes
  (route/resources "/js/vendor" { :root "public/js/vendor/" }))

(defroutes gfx-resource-routes
  (route/resources "/gfx" { :root "public/gfx/" }))

(decorate-with (with-header "Cache-Control" "max-age=31536000")
               gfx-resource-routes
               js-vendor-resource-routes
               css-vendor-resource-routes)

(defroutes api-routes
  (GET "/" [] (resp/resource-response "index.html" {:root "public"}))
  (GET "/status" [] {:status 200 :body "Yey Okay"})
  (context "/daymoneys" []
    (GET "/" {params :params} (handlers/get-all-daymoneys params))
    (POST "/" {body :body} (handlers/create-new-daymoney body))
    (context "/:id" [id]
      (GET    "/" [] (handlers/get-daymoney id))
      (PUT    "/" {body :body} (handlers/update-daymoney id body))
      (DELETE "/" [] (handlers/delete-daymoney id))))
  gfx-resource-routes
  js-vendor-resource-routes
  css-vendor-resource-routes
  (route/resources "/")
  (route/not-found "Not Found"))

(def app (-> (handler/api api-routes)
             (middleware/wrap-json-body)
             (middleware/wrap-json-response)
             (wrap-keyword-params)
             (allow-cross-origin)))

(defn init "Executed on startup." []
  (model/read-data-from-csv (or (System/getenv "DATAFILE")
                                "/srv/leemoney.csv")))

(defn -main [& args]
  (init)
  (jetty/run-jetty app {:port (Integer/parseInt (or (System/getenv "PORT")
                                                    "3000"))}))
