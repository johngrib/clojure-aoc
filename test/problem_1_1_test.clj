(ns problem-1-1-test
  (:require [clojure.test :refer :all])
  (:require [problem-1-1 :refer :all]
            [clojure.string :as str]))

(deftest describe:read-string
  "read-string 함수는"
  (testing "주어진 숫자 형식의 문자열을 읽어 숫자를 리턴한다"
    (is (= 1 (read-string "+1")))
    (is (= 0 (read-string "+0")))
    (is (= 0 (read-string "-0")))
    (is (= -1 (read-string "-1")))
    ))

(deftest describe:to-numbers
  "to-numbers 함수는"
  (testing "개행문자가 섞인 숫자 입력이 주어지면, 숫자 리스트를 리턴한다"
    (is (= [1 2 -3]
           (to-numbers "+1\n+2\n-3")))
    )
  )

(deftest describe:solve-1-1
  "solve-1-1 함수는"
  (let [subject solve-1-1]
    (testing "주어진 텍스트를 읽어 포함된 숫자 값들의 합을 리턴한다."
      (is (= 6 (subject
                 "+1
                 +2
                 +3")))
      (is (= 11 (subject
                  "+10
                  -2
                  +3")))
      (is (= -99 (subject
                   "-100
                   -2
                   +3"))))
    (testing "https://adventofcode.com/2018/day/1/input 에 제시된 입력이 주어지면 484 를 리턴한다."
      (is (-> data-file (subject) (= 484))))))
