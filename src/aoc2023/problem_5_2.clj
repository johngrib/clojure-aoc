(ns aoc2023.problem-5-2
  (:require [aoc2023.problem-5-1 :as part1]
            [clojure.string :as s]))

(comment
  " https://adventofcode.com/2023/day/5#part2

  Part 1 에서의 입력에서 첫째 줄, 즉 씨앗에 대한 내용이 바뀐다.

  예제의 첫째줄은 다음과 같았다.
  seeds: 79 14 55 13  => 심어야 하는 씨앗은 4개, 씨앗 번호는 79, 14, 55, 13

  하지만 이제 의미가 바뀌어서, 씨앗의 범위를 나타낸다.
  seeds: 79 14 55 13  => 79 부터 92까지 (14개 숫자), 그리고 55부터 67까지 (13개 숫자)

  즉 Part2 에서는 씨앗이 더 많아진다.

  문제: Part2 의 조건에서 가장 작은 location 값은 얼마인가?
  ")

(defn parse-numbers->checker
  "공백으로 구분된 숫자들의 문자열을 받아서 체커 리스트를 만들어 리턴합니다.
  체커 하나는 3개의 숫자로 이루어져 있습니다.

  :key-head 키 시작 숫자
  :key-tail 키 끝 숫자
  :val-head 값 시작 숫자

  예제의 '50 98 2' 가 입력된다면 다음과 같은 체커 리스트가 리턴됩니다.
    ({:key-head 98, :key-tail 99, :val-head 50})
  "
  [numbers-string]
  (->> numbers-string
       (re-seq #"\d+")
       (map parse-long)
       (partition 3)
       (map (fn [[a b c]]
              (let [length c
                    key-first b
                    key-last (+ b length -1)
                    val-first a
                    val-last (+ a length -1)]
                {:in  [key-first key-last]
                 :out [val-first val-last]})))
       (sort-by :key-head)))

(defn convert-filter
  "하나의 seed 에 맞춰 주어진 하나의 soil의 범위를 축소한 결과를 리턴합니다."
  [{[A-head A-tail] :out #_#_:as seed}
   {[b-head b-tail] :in
    [c-head c-tail] :out #_#_:as soil}]
  (let [head-diff (abs (- b-head A-head))
        tail-diff (abs (- b-tail A-tail))]
    (cond
      (or (< b-tail A-head)
          (< A-tail b-head))
      nil

      :else
      {:in  [(if (<= A-head b-head) b-head A-head)
             (if (<= A-tail b-tail) A-tail b-tail)]
       :out [(if (<= A-head b-head) c-head (+ c-head head-diff))
             (if (<= A-tail b-tail) (- c-tail tail-diff) c-tail)]})))

(comment
  ;; 겹치지 않는 경우
  (convert-filter {:out [14 21]} {:in [0 7], :out [100 107]}) ; nil
  (convert-filter {:out [14 21]} {:in [22 23], :out [85 86]}) ; nil

  ;; 포함하는 경우
  (convert-filter {:out [4 5]} {:in [0 30], :out [100 130]}) ; {:in [4 5], :out [104 105]}
  (convert-filter {:out [0 10]} {:in [2 4], :out [12 14]}) ; {:in [2 4], :out [12 14]}

  ;; 겹치는 경우
  (convert-filter {:out [0 10]} {:in [5 15], :out [25 35]}) ; {:in [5 10], :out [25 30]}
  (convert-filter {:out [5 15]} {:in [0 10], :out [20 30]}) ; {:in [5 10], :out [25 30]}

  ; 경계에 걸치는 경우
  (convert-filter {:out [10 11]} {:in [11 13], :out [21 23]}) ; {:in [11 11], :out [21 21]}
  (convert-filter {:out [14 21]} {:in [10 14], :out [20 24]}) ; {:in [14 14], :out [24 24]}
  ;;
  )

(defn remake-filters
  "이전 필터와 이후 필터를 받아, 이후 필터를 재구성합니다"
  [a-list b-list]
  (->> (for [a a-list
             b b-list]
         [a b])
       (map #(apply convert-filter %))
       (filter identity)
       (sort-by :out)))

(def process-in-almanac
  ["seed-to-soil"
   "soil-to-fertilizer"
   "fertilizer-to-water"
   "water-to-light"
   "light-to-temperature"
   "temperature-to-humidity"
   "humidity-to-location"])


(defn solve [data-file]
  (let [splits (->>
                (s/replace data-file "\n" " ")
                (re-seq #"([a-z\-]+)(?: map)?: ([0-9 ]+)"))
        raw-numbers (map rest splits)
        input-data (->> raw-numbers
                        flatten
                        (apply hash-map))
        seeds (->> (input-data "seeds")
                   (re-seq #"\d+")
                   (map parse-long)
                   (partition 2)
                   (map (fn [[a b]] {:out [a (+ a b -1)]}))
                   (sort-by :out))
        min-location (->> process-in-almanac
                          (map #(parse-numbers->checker (input-data %)))
                          (reduce remake-filters seeds)
                          first
                          :out
                          first)]
    min-location))

(comment
  (solve part1/data-file)  ; 78775051
  )

