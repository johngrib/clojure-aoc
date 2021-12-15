(ns problem-1-2-test
  (:require [clojure.test :refer :all])
  (:use [problem-1-1])
  (:require [problem-1-2 :refer :all]))

(deftest describe:solve-1-2
  "solve-1-2 함수"
  (testing "https://adventofcode.com/2018/day/1 에 올라온 part2 예제를 제공하면 예제와 같은 결과를 리턴한다"
    "
    Here are other examples:
      +1, -1 first reaches 0 twice.
      +3, +3, +4, -2, -4 first reaches 10 twice.
      -6, +3, +8, +5, -6 first reaches 5 twice.
      +7, +7, -2, -7, -4 first reaches 14 twice.
    "
    (is (= 0 (solve-1-2 (cycle [+1 -1]))))
    (is (= 10 (solve-1-2 (cycle [+3 +3 +4 -2 -4]))))
    (is (= 5 (solve-1-2 (cycle [-6, +3, +8, +5, -6]))))
    (is (= 14 (solve-1-2 (cycle [+7, +7, -2, -7, -4]))))
    )
  (testing "https://adventofcode.com/2018/day/1 의 part2 정답을 리턴한다"
    "문제 1-2의 정답은 367"
    (is (= 367 (solve-1-2 (cycle (to-numbers data-file)))))
    )
  )

