(ns aoc2018.problem-6-2-test
  (:require [clojure.test :refer :all]
            [aoc2018.problem-6-1 :refer :all]
            [aoc2018.problem-6-2 :refer :all]))

(deftest desc:solve-6-2
  (testing
    "예제 입력을 제공하면 예제 정답을 리턴한다."
    (is (= 16 (solve-6-2 sample-input-string 32)))
    (testing
      "문제의 입력을 제공하면 정답을 리턴한다."
      (is (= 34829 (solve-6-2 input-string 10000))))))
