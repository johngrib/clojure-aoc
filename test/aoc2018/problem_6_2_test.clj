(ns aoc2018.problem-6-2-test
  (:require [aoc2018.problem-6-1 :refer [sample-input-string input-string]]
            [aoc2018.problem-6-2 :refer [solve-6-2]]
            [clojure.test :refer [deftest testing is run-tests]]))

(deftest desc:solve-6-2
  (testing
   "예제 입력을 제공하면 예제 정답을 리턴한다."
    (is (= 16 (solve-6-2 sample-input-string 32))))
  (testing
   "문제의 입력을 제공하면 정답을 리턴한다."
    (is (= 34829 (solve-6-2 input-string 10000)))))

(comment
  (run-tests)
  ;;
  )
