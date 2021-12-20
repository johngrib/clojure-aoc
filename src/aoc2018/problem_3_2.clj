(ns aoc2018.problem-3-2
  (:require clojure.set)
  (:require [aoc2018.problem-3-1 :as problem]))


(comment "
## 문제
https://adventofcode.com/2018/day/3

........
...2222.
...2222.
.11XX22.
.11XX22.
.111133.
.111133.
........

입력대로 모든 격자를 채우고 나면, 정확히 한 ID에 해당하는 영역이 다른 어떤 영역과도 겹치지 않음
위의 예시에서는 ID 3 이 ID 1, 2와 겹치지 않으므로 3을 출력.
겹치지 않는 영역을 가진 ID를 출력하시오. (문제에서 답이 하나만 나옴을 보장함)
")

(defn collect-duplicated-dots [dots]
  "주어진 점들의 리스트에서 중복된 위치를 가진 점들을 수집해 리턴합니다."
  (as-> dots v
        (group-by (juxt :x :y) v)
        (for [[_ value] v :when (< 1 (count value))] value)
        (reduce into v)))

(defn solve-3-2 [input-strings]
  "https://adventofcode.com/2018/day/3 의 답을 리턴한다"
  (let [
        all-dots (problem/collect-all-dots input-strings)
        all-id (set (for [i all-dots] (i :id)))

        duplicated-dots (collect-duplicated-dots all-dots)
        duplicated-id (set (for [i duplicated-dots] (i :id)))

        not-duplicated-id (clojure.set/difference all-id duplicated-id)]

    not-duplicated-id))
