(ns aoc2018.problem-6-2
  (:require [aoc2018.problem-6-1 :refer :all]))

(comment "
https://adventofcode.com/2018/day/6 문제의 part2.

모든 군주-점과의 맨해튼 거리의 총합이 N '미만'인 점을 안전하다고 정의한다.
N = 10000 일 때 모든 안전한 점의 수를 구하여라.
")

(defn safe-dot?
  "주어진 점이 안전하다면 true를 리턴합니다.
  dot: 안전을 판별할 점
  king-dots: 군주-점 목록
  safe-limit: 안전 기준 점수 N"
  [dot king-dots safe-limit]
  (cond
    (>= 0 safe-limit) false
    (empty? king-dots) true
    :else (recur
            dot
            (rest king-dots)
            (- safe-limit (manhattan-distance dot (first king-dots))))))

(defn solve-6-2
  "https://adventofcode.com/2018/day/6 문제 part2 를 풀이합니다.
  모든 안전한 지역의 넓이를 리턴합니다."
  [input-string safe-limit]
  (let [king-dots (input->dots input-string)

        min-x (select min :x king-dots)
        max-x (select max :x king-dots)
        min-y (select min :y king-dots)
        max-y (select max :y king-dots)

        x-좌표들 (range min-x (inc max-x))
        y-좌표들 (range min-y (+ 1 max-y))

        game-모든-점들 (for [y y-좌표들, x x-좌표들] {:x x, :y y})]
    (count
      (filter #(safe-dot? % king-dots safe-limit) game-모든-점들))))

(comment
  (solve-6-2 sample-input-string 32)                        ; 16
  (solve-6-2 input-string 10000)                            ; 34829
  )
