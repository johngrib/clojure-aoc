(ns aoc2018.problem-4-2
  (:require [clojure.string :as str]
            [clojure.java.io :as io]
            [aoc2018.problem-4-1 :refer :all]))

(comment "
## 문제
https://adventofcode.com/2018/day/4 part2

모든 경비원 중 같은 minute에 가장 자주 잠드는 경비원은 누구인가?
해당 경비원의 ID 와 가장 많이 잠들었던 minute 의 곱을 구하여라. ")

(defn most-of
  "frequencies 함수로 빈도를 계산한 결과를 받아 그 중 가장 높은 빈도를 가진 엔트리를 리턴합니다."
  [list]
  (let [[value frequency] (first (sort-by val > (frequencies list)))]
    {:value value, :frequency frequency}))

(comment
  (most-of [:a :a :b :b :b :c]))

(defn solve-4-2
  "https://adventofcode.com/2018/day/4 문제를 풀이합니다."
  [raw-strings]
  (let [
        날짜별-로그-리스트 (simplify-logs raw-strings)
        날짜별-수면-기록 (map #(assoc % :minutes (range (:sleep %) (:wake %))) 날짜별-로그-리스트)
        경비원별-수면-기록 (group-by :guard-id
                             (map #(select-keys % [:guard-id :minutes]) 날짜별-수면-기록))
        경비원별-가장-많이-잠들었던-분 (map (fn [[guard-id v]]
                                 (merge
                                   {:guard-id guard-id}
                                   (most-of (reduce into (map #(:minutes %) v)))))
                               경비원별-수면-기록)
        정답-대상자 (last (sort-by :frequency 경비원별-가장-많이-잠들었던-분))]

    (merge 정답-대상자 {:solution (* (:guard-id 정답-대상자) (:value 정답-대상자))})))

(comment
  ; 4455
  (solve-4-2 sample-input-string)
  ; 73001
  (solve-4-2 input-strings))
