(ns aoc2018.problem-3-1-test
  (:require [clojure.test :refer :all])
  (:require [aoc2018.problem-3-1 :refer :all]))

(deftest description:to-location-code
  "to-location-code 함수는"
  (testing "주어진 코드 라인을 위치정보 객체로 변환해 리턴합니다"
    (are [code expect]
      (= (to-location-code code) expect)
      "#6 @ 868,833: 18x20"
      {:id 6, :x 868, :y 833, :width 18, :height 20}

      "#43 @ 98,544: 25x28"
      {:id 43, :x 98, :y 544, :width 25, :height 28})))


(deftest description:expand-code-map
  "expand-code-map 함수는"
  (testing "위치정보 객체를 id를 포함하는 좌표의 리스트로 확장해 리턴합니다."
    (are [code-map expect]
      (= (expand-code-map code-map) expect)
      {:id 6, :x 8, :y 3, :width 2, :height 3}
      [{:id 6, :x 8, :y 3} {:id 6, :x 8, :y 4} {:id 6, :x 8, :y 5} {:id 6, :x 9, :y 3} {:id 6, :x 9, :y 4} {:id 6, :x 9, :y 5}]

      {:id 2, :x 1, :y 10, :width 2, :height 2}
      [{:id 2, :x 1, :y 10} {:id 2, :x 1, :y 11} {:id 2, :x 2, :y 10} {:id 2, :x 2, :y 11}])))


(deftest describe:solve-3-1
  "solve-3-1 함수"
  (let
    [sample-string
     ["#1 @ 1,3: 4x4"
      "#2 @ 3,1: 4x4"
      "#3 @ 5,5: 2x2"]]
    (testing
      "https://adventofcode.com/2018/day/3 에 올라온 part1 예제를 제공하면 예제와 같은 결과를 리턴한다"
      (is (= 4 (solve-3-1 sample-string)))

      "https://adventofcode.com/2018/day/3 의 part1 정답을 리턴한다"
      (is (= 118223 (solve-3-1 input-strings))))))


(comment
  (run-tests))

