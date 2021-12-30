(ns aoc2020.problem-1-1-test
  (:require [clojure.test :refer :all]
            [aoc2020.problem-1-1 :refer :all]))

(deftest desc:solve-1-1
  (testing "2020 day1 part1 예제를 입력하면 예제 정답을 리턴한다."
    (is (= {:numbers #{1721 299}, :multiple 514579}
           (solve-1-1 sample-input-numbers 2020))))
  (testing "2020 day1 part1 본문을 입력하면 정답을 리턴한다."
    (is (= {:numbers #{1078 942}, :multiple 1015476}
           (solve-1-1 input-numbers 2020)))))

(comment
  (run-tests))
