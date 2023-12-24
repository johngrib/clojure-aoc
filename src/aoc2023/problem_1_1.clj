(ns aoc2023.problem-1-1
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(comment
  "## 문제
  https://adventofcode.com/2023/day/1

  Part 1.
  - 각 라인의 첫 번째 숫자와 마지막 숫자를 얻는다.
  - 두 숫자를 조합해 두자리 숫자를 만든다. 예를 들어 1과 2를 얻었다면 12가 된다.
    - 숫자가 하나라면 그 수를 두 번 반복한다. 예를 들어 1을 얻었다면 11이 된다.
  - 각 라인에서 얻은 모든 두자리 숫자를 더한 결과가 답이다.

  예제
  1abc2       -> 12
  pqr3stu8vwx -> 38
  a1b2c3d4e5f -> 15
  treb7uchet  -> 77

  합계는 12 + 38 + 15 + 77 = 142
  ")

(def data-file (-> "aoc2023/input1.txt" io/resource slurp))
(def input-lines (str/split-lines data-file))

(comment
  (->> input-lines
       ;; 각 행에서 숫자 char만 추출한다
       (map (fn [line] (filter #(Character/isDigit %) line)))
       ;; 첫째 숫자와 마지막 숫자를 얻는다
       (map (fn [numbers] [(first numbers) (last numbers)]))
       ;; 두 숫자를 조합해 두 자리 숫자 하나를 만든다
       (map (fn [[a b]] (Integer/parseInt (str a b))))
       ;; 합계를 구한다
       (apply +))
  ;; 54304
  ;;
  )
