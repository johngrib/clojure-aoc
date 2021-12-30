(ns aoc2018.problem-7-1-test
  (:require [clojure.test :refer :all])
  (:require [aoc2018.problem-7-1 :refer :all]))

(deftest desc:string->work-context
  (testing "string->work-context 함수는 입력을 분석해 문제 해결에 필요한 정보를 생성해 리턴한다."
    (is (= (string->work-context sample-input-string)
           {:root-works     [\C],
            :required-works {\A #{\C}, \F #{\C}, \B #{\A}, \D #{\A}, \E #{\B \D \F}},
            :next-works     {\C #{\A \F}, \A #{\B \D}, \B #{\E}, \D #{\E}, \F #{\E}}}))))

(deftest desc:solve-7-1
  (testing "예제 입력이 주어지면 예제 해답을 리턴한다."
    (is (= (solve-7-1 sample-input-string) "CABDFE")))
  (testing "본 문제 입력이 주어지면 해답을 리턴한다."
    (is (= (solve-7-1 input-string) "GLMVWXZDKOUCEJRHFAPITSBQNY"))))

(comment
  (run-tests))