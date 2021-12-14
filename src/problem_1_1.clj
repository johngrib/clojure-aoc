(ns problem-1-1
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(def data-file (io/resource "input1.txt"))

(defn to-numbers [input-text]
  "주어진 문자열을 개행문자를 기준으로 분리한 숫자들의 리스트로 변환해 리턴합니다."
  (map read-string (str/split-lines input-text)))

(defn solve-1-1
  "주어진 문자열을 숫자열로 간주하여, 모든 숫자의 합계를 리턴합니다.
  https://adventofcode.com/2018/day/1 "
  [input-text]
  (reduce + (to-numbers input-text)))

(solve-1-1 (slurp data-file))
