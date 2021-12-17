(ns aoc2018.problem-3-2-test
  (:require [clojure.test :refer :all])
  (:require [aoc2018.problem-3-1 :as problem])
  (:require [aoc2018.problem-3-2 :refer :all]))

(deftest describe:collect-duplicated-dots
  "collect-duplicated-dots 함수는"
  (testing "주어진 점들의 리스트에서, 중복된 위치를 갖는 점을 수집해 리턴합니다"
    (are [all-dots result]
      (= (collect-duplicated-dots all-dots) result)
      [
       {:id 1, :x 1, :y 1}  ; 중복
       {:id 2, :x 1, :y 1}  ; 중복
       {:id 2, :x 1, :y 2}]

      [{:id 1, :x 1, :y 1} {:id 2, :x 1, :y 1}]

      [
       {:id 1, :x 1, :y 1}
       {:id 2, :x 3, :y 1}  ; 중복
       {:id 3, :x 3, :y 1}  ; 중복
       {:id 4, :x 2, :y 1}]

      [{:id 2, :x 3, :y 1} {:id 3, :x 3, :y 1}])))

(deftest describe:solve-3-2
  "solve-3-2 함수"
  (let
    [sample-string
     ["#1 @ 1,3: 4x4"
      "#2 @ 3,1: 4x4"
      "#3 @ 5,5: 2x2"]]
    (testing
      "https://adventofcode.com/2018/day/3 에 올라온 part1 예제를 제공하면 3을 리턴한다
      참고 => https://github.com/green-labs/bootcamp-aoc/blob/main/translations/aoc2018/day3.md
      "
      (is (= #{3} (solve-3-2 sample-string)))

      "https://adventofcode.com/2018/day/3 의 part2 정답을 리턴한다"
      (is (= #{412} (solve-3-2 problem/input-strings))))))

(comment
  (run-tests))
