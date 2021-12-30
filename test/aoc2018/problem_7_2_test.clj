(ns aoc2018.problem-7-2-test
  (:require [clojure.test :refer :all]
            [aoc2018.problem-7-1 :refer :all]
            [aoc2018.problem-7-2 :refer :all]))

(deftest desc:solve-7-2
  "2018 day 7 part 2 문제"
  (testing "예제 입력 (워커 2개, 각 작업의 시간은 1초부터)을 넣으면 예제 정답을 리턴한다."
    (is (= 15 (solve-7-2 sample-input-string 2 1))))
  (testing "예제 입력 (워커 5개, 각 작업의 시간은 61초부터)을 넣으면 예제 정답을 리턴한다."
    (is (= 1105 (solve-7-2 input-string 5 61)))))

(comment
  (run-tests))
