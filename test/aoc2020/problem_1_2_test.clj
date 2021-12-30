(ns aoc2020.problem-1-2-test
  (:require [clojure.test :refer :all])
  (:require [aoc2020.problem-1-1 :refer :all])
  (:require [aoc2020.problem-1-2 :refer :all])
  )

(deftest desc:solve-1-2
  (testing "2020 day1 part2 문제의 예제 입력을 제공하면 예제 정답이 리턴된다."
    (is (= {:numbers #{979 366 675}, :multiple 241861950}
           (solve-1-2 sample-input-numbers 2020))))
  (testing "2020 day1 part2 문제의 입력을 제공하면 정답이 리턴된다."
    (is (= {:numbers #{956 262 802}, :multiple 200878544}
           (solve-1-2 input-numbers 2020)))))

(comment (run-tests))
