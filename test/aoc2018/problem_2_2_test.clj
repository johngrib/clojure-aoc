(ns aoc2018.problem-2-2-test
  (:require [clojure.test :refer :all])
  (:require [aoc2018.problem-2-2 :refer :all]))

(deftest describe:collect-intersection
  "collect-intersection 함수는"
  (testing "입력된 두 리스트를 비교하여 위치와 값이 같은 아이템의 리스트를 리턴한다"
    (are [result input-list1 input-list2]
      (= result (collect-intersection input-list1 input-list2))
      ;      v v     v v
      [1 2] [1 2 3] [1 2 4]
      ;           v   vv     v   vv
      [\h \b \a] "his bag." "her-back"
      )))

(deftest describe:analyze-two-string
  "analyze-two-string 함수는"
  (testing "입력된 두 문자열이 같지 않다면, 비교 정보를 리턴합니다."
    (are [result input-string1 input-string2]
      (= result (analyze-two-string input-string1 input-string2))

      {:string1 "abcd", :string2 "abdd", :mismatch-count 1, :intersection-string "abd"}
      "abcd" "abdd"
      )
    )
  (testing "입력된 두 문자열이 같다면, nil을 리턴합니다"
    (are [result input-string1 input-string2]
      (= result (analyze-two-string input-string1 input-string2))
      nil "abcd" "abcd"
      nil "red fox jumps" "red fox jumps"
      )
    )
  )

(deftest describe:solve-2-2
  "solve-2-2 함수"
  (let
    [sample-string ["abcde" "fghij" "klmno" "pqrst" "fguij" "axcye" "wvxyz"]]
    (testing
      "https://adventofcode.com/2018/day/2 에 올라온 part2 예제를 제공하면 예제와 같은 결과를 리턴한다"
      (is (= "fgij" (solve-2-2 sample-string)))

      "https://adventofcode.com/2018/day/2 의 part2 정답을 리턴한다"
      (is (= "xpysnnkqrbuhefmcajodplyzw" (solve-2-2 input-strings)))
      ))
  )

(comment
  (run-tests)
  )
