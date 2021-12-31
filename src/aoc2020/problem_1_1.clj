(ns aoc2020.problem-1-1
  (:require [clojure.java.io :as io]
            [clojure.string :as string]))

(comment "
https://adventofcode.com/2020/day/1  part 1
주어진 숫자들 중 서로 더했을 때 2020이 되는 수 A, B를 찾은 다음 두 수의 곱을 구하여라.
")

(defn string->numbers [input-string]
  (->> input-string
       string/split-lines
       (map #(Integer/parseInt %))))

(def sample-input-numbers (-> "aoc2020/input1-sample.txt"
                              io/resource
                              slurp
                              string->numbers))
(def input-numbers (-> "aoc2020/input1.txt"
                       io/resource
                       slurp
                       string->numbers))

(defn solve-1-1 [numbers target-sum]
  (let [[found-numbers] (for [x numbers
                              y numbers
                              :when (= target-sum (+ x y))]
                          #{x y})]
    {:numbers  found-numbers
     :multiple (apply * found-numbers)}))

(comment
  (solve-1-1 sample-input-numbers 2020)                     ; {:numbers [1721 299], :multiple 514579}
  (solve-1-1 input-numbers 2020))                           ; {:needles [1078 942], :solution 1015476}
