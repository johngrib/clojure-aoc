(ns aoc2018.problem-5-2-test
  (:require [clojure.test :refer :all]
            [aoc2018.problem-5-1 :refer :all]
            [aoc2018.problem-5-2 :refer [solve-5-2]]))

(deftest describe:solve-5-2
  "https://adventofcode.com/2018/day/5 part 2 문제를 풀이하여, 반응이 끝난 후의 최소 문자열 길이를 리턴합니다."
  (testing
    (is (= 4 (:size (solve-5-2 sample-input-string))))
    (is (= 5524 (:size (solve-5-2 input-string))))))

(comment
  (run-tests))
