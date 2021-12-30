(ns aoc2018.problem-6-1-test
  (:require [clojure.test :refer :all])
  (:require [aoc2018.problem-6-1 :refer :all]))

(deftest desc:absolute
  (testing
    (are [expect input]
      (= expect (absolute input))
      0 0,
      1 -1,
      100.2 -100.2)))

(deftest desc:manhattan-distance
  (testing
    (are [expect dot1 dot2]
      (= expect (manhattan-distance dot1 dot2))
      1 {:x 1 :y 0} {:x 1 :y 1}
      2 {:x 0 :y 0} {:x -1 :y -1}
      4 {:x -1 :y -1} {:x 1 :y 1})))

(deftest desc:solve-6-1
  (testing
    "예제 입력을 제공하면 예제 정답을 리턴한다."
    (is (= 17 (solve-6-1 sample-input-string)))
    (testing
      "문제의 입력을 제공하면 정답을 리턴한다."
      (is (= 5187 (solve-6-1 input-string))))))

(comment
  (run-tests))
