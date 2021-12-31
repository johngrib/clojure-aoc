(ns aoc2020.problem-1-2
  (:require [aoc2020.problem-1-1 :refer :all]))

(defn solve-1-2
  "https://adventofcode.com/2020/day/1  part 2 문제를 풀어 답을 리턴합니다."
  [numbers target-sum]
  (let [[found-numbers] (for [x numbers
                              y numbers
                              z numbers :when (= target-sum (+ x y z))]
                          #{x y z})]
    {:numbers  found-numbers
     :multiple (apply * found-numbers)}))

(comment
  (solve-1-2 sample-input-numbers 2020)                     ; 241861950
  (solve-1-2 input-numbers 2020))                           ; 200878544
