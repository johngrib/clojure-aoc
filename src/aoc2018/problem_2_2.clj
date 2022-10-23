(ns aoc2018.problem-2-2
  (:require [clojure.string :as str]
            [clojure.java.io :as io]))

(comment "
## 문제
https://adventofcode.com/2018/day/2

여러 개의 문자열 중, 같은 위치에 정확히 하나의 문자가 다른 문자열 쌍에서 같은 부분만을 리턴하시오.

예)
abcde
fghij <
klmno
pqrst
fguij <
axcye
wvxyz

정답: fgij
")

(def data-file (-> "aoc2018/input2.txt"
                   io/resource
                   slurp))
(def input-strings (str/split-lines data-file))

(defn collect-intersection
  "두 리스트를 비교하여, 위치와 값이 같은 아이템을 리스트로 수집해 리턴합니다"
  [list1 list2]
  (for [index (range (count list1))
        :let [char1 (get list1 index),
              char2 (get list2 index)]
        :when (= char1 char2)]
    char1))

(defn analyze-two-string
  " 두 문자열을 받아 분석한 결과를 리턴합니다 "
  [string1 string2]
  (when-not (= string1 string2)
    (let [intersection (collect-intersection string1 string2)]
      {:string1             string1
       :string2             string2
       :mismatch-count      (- (count string1) (count intersection))
       :intersection-string (str/join intersection)})))

(defn solve-2-2
  [string-list]
  (->> string-list
       (map #(partial analyze-two-string %))
       (map #(map % string-list))
       flatten
       (filter some?)
       (filter #(= 1 (% :mismatch-count)))
       (map #(% :intersection-string))
       distinct
       first))


