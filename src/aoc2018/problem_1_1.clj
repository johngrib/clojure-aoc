(ns aoc2018.problem-1-1
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(def data-file (-> "aoc2018/input1.txt" io/resource slurp))

(defn to-numbers [input-text]
  "주어진 문자열을 개행문자를 기준으로 분리한 숫자들의 리스트로 변환해 리턴합니다."
  (map read-string (str/split-lines input-text)))

(defn solve-1-1
  "주어진 문자열을 숫자열로 간주하여, 모든 숫자의 합계를 리턴합니다.
  https://adventofcode.com/2018/day/1 "
  [input-text]
  (apply + (to-numbers input-text)))
