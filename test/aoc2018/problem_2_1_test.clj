(ns aoc2018.problem-2-1-test
  (:require [aoc2018.problem-2-1 :refer [input-strings solve-2-1]]
            [clojure.test :refer [deftest testing is are]]))

(deftest describe:frequencies
  (testing "frequencies 함수는"
    (testing "주어진 리스트 아이템의 빈도를 카운트한 결과를 리턴한다"
      (is (= {1 3}
             (frequencies [1 1 1])))
      (is (= {1 2, :a 2}
             (frequencies [1 :a 1 :a])))
      (is (= {\a 2, \b 1}
             (frequencies [\b \a \a])))
      (is (= {\a 2, \b 1}
             (frequencies "aab")))
      (is (= {\a 2, \b 1, \c 1, \d 2}
             (frequencies "aabcdd"))))))

(deftest describe:vals
  (testing "vals 함수는"
    (testing "map의 value만 수집한 리스트를 리턴한다"
      (is (= [1 2] (vals {:a 1, :b 2})))
      (is (= [4 2 42] (vals {:a 4, :b 2, :c 42}))))))

(deftest describe:solve-2-1
  (testing "solve-2-1 함수는"
    (testing "https://adventofcode.com/2018/day/2 에 올라온 part1 예제를 제공하면 예제와 같은 결과를 리턴한다"
      (are [expect result]
           (= expect result)
        12 (solve-2-1 ["abcdef" "bababc" "abbcde" "abcccd" "aabcdd" "abcdee" "ababab"])
        ))
    (testing "https://adventofcode.com/2018/day/2 의 part1 정답을 리턴한다"
      (are [expect result]
           (= expect result)
        6200 (solve-2-1 input-strings)))))

(comment
  (clojure.test/run-tests))
