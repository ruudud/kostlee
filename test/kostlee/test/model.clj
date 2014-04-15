(ns kostlee.test.model
  (:use clojure.test
        ring.mock.request
        kostlee.model))

(deftest model
  (testing "daymoney list"
    (let [ds (daymoneys-list)]
      (is (= (count ds) 8))))

  (testing "avg increase per person per day"
    (let [res (avg-daymoneys-per-people)]
      (is (= res 57.0763888934M))))

  (testing "avg increase per day"
    (let [res (avg-daymoneys-per-day)]
      (is (= res 285.7142857M))))

  (testing "max increase per person per day"
    (let [res (max-daymoneys-per-people)]
      (is (= (:increasePerPeople res) 55.55555556M))))

  (testing "max increase per day"
    (let [result (max-daymoneys-per-day)]
      (is (= (:increase result) 1000M)))))

