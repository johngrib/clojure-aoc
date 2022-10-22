(ns aoc2018.problem-1-2-test
  (:require [clojure.test :refer [deftest testing are run-tests is]]
            [aoc2018.problem-1-1 :as problem]
            [aoc2018.problem-1-2 :refer [solve-1-2 solve-1-2:loop solve-1-2:reduce]]))

(deftest describe:solve-1-2
  (testing "solve-1-2"
    (testing "https://adventofcode.com/2018/day/1 에 올라온 part2 예제를 제공하면 예제와 같은 결과를 리턴한다"
      "
      Here are other examples:
      +1, -1 first reaches 0 twice.
      +3, +3, +4, -2, -4 first reaches 10 twice.
      -6, +3, +8, +5, -6 first reaches 5 twice.
      +7, +7, -2, -7, -4 first reaches 14 twice.
      "
      (are [expect-result input-list]
           (= expect-result (solve-1-2 input-list)
                            (solve-1-2:loop input-list)
                            (solve-1-2:reduce input-list))
        0 [+1 -1]
        10 [+3 +3 +4 -2 -4]
        5 [-6, +3, +8, +5, -6]
        14 [+7, +7, -2, -7, -4])))

  (testing "https://adventofcode.com/2018/day/1 의 part2 정답을 리턴한다"
    (is (= 367
           (solve-1-2:loop (cycle (problem/to-numbers problem/data-file))))
        "문제 1-2의 정답은 367")))
(comment
  (run-tests)
  ;;
  )