(ns kostlee.test.core
  (:use clojure.test
        ring.mock.request
        kostlee.core))

(deftest test-app
  (testing "index"
    (let [response (handler (request :get "/"))]
      (is (= (:status response) 200))))

  (testing "not-found route"
    (let [response (handler (request :get "/invalid"))]
      (is (= (:status response) 404)))))

