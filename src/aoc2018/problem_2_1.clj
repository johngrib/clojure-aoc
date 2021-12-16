(ns aoc2018.problem-2-1
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(comment "
## 문제
https://adventofcode.com/2018/day/2

주어진 각각의 문자열에서, 같은 문자가 두 번 혹은 세 번씩 나타난다면 각각을 한 번씩 센다.
두 번 나타난 문자가 있는 문자열의 수 * 세 번 나타난 문자가 있는 문자열의 수를 반환하시오.

abcdef (해당 없음)
bababc (두 번 나오는 문자: a, 세 번 나오는 문자: b)
abbcde (두 번 나오는 문자: b)
abcccd (세 번 나오는 문자: c)
aabcdd (두 번 나오는 문자: a, d)
abcdee (두 번 나오는 문자: e)
ababab (세 번 나오는 문자: a, b)

이 때,
  두 번 나타난 문자가 있는 문자열의 수: 4 개
  세 번 나타난 문자가 있는 문자열의 수: 3 개
  답 : 4 * 3 = 12
")
(def data-file (-> "aoc2018/input2.txt" (io/resource) (slurp)))
(def input-strings (str/split-lines data-file))

(defn solve-2-1
  [string-list]
  (let [
        count-candidates (->> string-list (map frequencies) (map vals) (map set))
        two-times (->> count-candidates (filter #(% 2)) (count))
        three-times (->> count-candidates (filter #(% 3)) (count))
        ]
    (* two-times three-times)))


