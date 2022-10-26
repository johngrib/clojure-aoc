(ns aoc2020.problem-8-1-test
  (:require [clojure.test :refer [deftest testing is run-tests]]
            [aoc2020.problem-8-1 :refer [strings->codes solve-8-1 sample-input-strings input-strings]]
            [aoc2020.problem-8-1-with-iterate :refer [solve-8-1-with-iterate]]))

(deftest desc:strings-codes
  (testing "주어진 명령 코드를 해석에 code map list를 생성해 리턴합니다."
    (is (= (strings->codes
            "nop +0
            acc +1
            jmp +4")
           [{:id 0, :op :nop, :arg-number 0}
            {:id 1, :op :acc, :arg-number 1}
            {:id 2, :op :jmp, :arg-number 4}]))))

(deftest desc:solve-8-1
  (testing "예제 입력을 제공하면 누산기 값으로 5를 리턴합니다."
    (is (= 5
           (:accumulator (solve-8-1 sample-input-strings)))))
  (testing "예제 입력을 제공하면 누산기 값으로 2025를 리턴합니다."
    (is (= 2025
           (:accumulator (solve-8-1 input-strings))))))

(deftest desc:solve-8-1-with-iterate
  (testing "예제 입력을 제공하면 누산기 값으로 5를 리턴합니다."
    (is (= 5
           (:accumulator (solve-8-1-with-iterate sample-input-strings)))))
  (testing "예제 입력을 제공하면 누산기 값으로 2025를 리턴합니다."
    (is (= 2025
           (:accumulator (solve-8-1-with-iterate input-strings))))))

(comment
  (run-tests)
  ;;
  )