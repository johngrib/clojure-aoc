(ns aoc2020.problem-8-2-test
  (:require [clojure.test :refer :all]
            [aoc2020.problem-8-1 :refer [sample-input-strings
                                         input-strings]]
            [aoc2020.problem-8-2 :refer [solve-8-2]]))

(deftest desc:solve-8-2
  (testing "예제 입력을 제공하면 누산기 값으로 8을 리턴합니다."
    (is (= 8 (:accumulator (solve-8-2 sample-input-strings)))))
  (testing "본문 입력을 제공하면 누산기 값으로 2001을 리턴합니다."
    (is (= 2001 (:accumulator (solve-8-2 input-strings))))))

(comment
  (run-tests))
