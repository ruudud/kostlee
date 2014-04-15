(ns kostlee.test.core
  (:use clojure.test
        ring.mock.request
        kostlee.core))

(deftest default-handlers
  (testing "index"
    (let [response (app (request :get "/"))]
      (is (= (:status response) 200))))

  (testing "not-found route"
    (let [response (app (request :get "/invalid"))]
      (is (= (:status response) 404)))))

