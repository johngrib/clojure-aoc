(ns aoc2018.problem-4-2-test
  (:require [aoc2018.problem-4-1 :refer [sample-input-string input-strings]]
            [aoc2018.problem-4-2 :refer [solve-4-2]]
            [clojure.test :refer [deftest testing is]]))

(deftest describe:solve-4-2
  (testing "solve-4-1 함수는"
    (testing "https://adventofcode.com/2018/day/4 에 올라온 part2 예제를 제공하면 정답 결과를 리턴한다"
      (is (= 4455
             (:solution (solve-4-2 sample-input-string)))))

    (testing "https://adventofcode.com/2018/day/4 에 올라온 part2 입력을 제공하면 정답 결과를 리턴한다"
      (is (= 73001
             (:solution (solve-4-2 input-strings)))))))

(comment
  (clojure.test/run-tests)
  ;;
  )
