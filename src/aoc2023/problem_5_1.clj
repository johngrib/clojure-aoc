(ns aoc2023.problem-5-1
  (:require [clojure.java.io :as io]
            [clojure.string :as s]))

(comment
  " https://adventofcode.com/2023/day/5

  심어야 하는 모든 씨앗들이 나열되어 있는 연감을 입력으로 받게 된다.
  연감에는 다음과 같은 것들이 포함되어 있다.

  - 각 '씨앗'에 어떤 '토양'을 사용해야 하는지
  - 각 '토양'에 어떤 '비료'를 사용해야 하는지
  - 각 '비료'에 어떤 '물'을 사용해야 하는지

  씨앗, 토양, 비료는 모두 숫자 아이디를 갖는다.
  매핑되지 않은 소스 번호는 동일한 대상 번호에 해당한다.
  가령, 씨앗 10은 토양 10에 심어야 한다.

  (예제)
  seeds: 79 14 55 13

  seed-to-soil map:
  50 98 2
  52 50 48

  soil-to-fertilizer map:
  0 15 37
  37 52 2
  39 0 15

  fertilizer-to-water map:
  49 53 8
  0 11 42
  42 0 7
  57 7 4

  water-to-light map:
  88 18 7
  18 25 70

  light-to-temperature map:
  45 77 23
  81 45 19
  68 64 13

  temperature-to-humidity map:
  0 69 1
  1 0 69

  humidity-to-location map:
  60 56 37
  56 93 4

  ---

  이 예제를 살펴보자.
  seeds: 79 14 55 13  => 심어야 하는 씨앗은 4개

  seed-to-soil map:
  50 98 2           => 토양 시작: 50, 씨앗 시작: 98, 길이: 2
  52 50 48          => 토양 시작: 52, 씨앗 시작: 50, 길이: 48

  50 98 2 를 통해 다음을 알 수 있다.
    => (seed 98, seed 50), (seed 99, soil 51)

  52 50 48 을 통해 다음을 알 수 있다. (48개의 연결정보)
    => (seed 50, soil 52), (seed 51, soil 53), ... , (seed 97, soil 99)

  이런 식으로 매핑해 나가면 된다.
  그런데 매핑 규칙에서 누락된 것은 자신의 번호를 사용하면 된다.

  예를 들어 seed 0 은 soil 0 과 매핑된다.

  따라서 다음과 같은 매핑이 나온다.

  seed  soil
  0     0     => 매핑이 지정되지 않아 0 으로 자동 매핑됨
  1     1     => 자동매핑
  ...   ...
  48    48    => 자동매핑
  49    49    => 자동매핑
  50    52    => 52 50 48 매핑에 의해 결정됨
  51    53    ...
  ...   ...
  96    98    ...
  97    99    => 52 50 48 매핑에 의해 결정됨
  98    50    => 50 98 2 매핑에 의해 결정됨
  99    51    => 50 98 2 매핑에 의해 결정됨

  매핑이 끝났으면 첫째줄의 seed 를 봐야 한다.
  seeds: 79 14 55 13

  매핑을 통해 이 seeds에 필요한 soil을 찾아보면 다음과 같다.

  - Seed number 79 corresponds to soil number 81.
  - Seed number 14 corresponds to soil number 14.
  - Seed number 55 corresponds to soil number 57.
  - Seed number 13 corresponds to soil number 13.

  이런 식으로 매핑이 이어진다.

  씨앗 - 토양 - 비료 - 물 - 빛 - 온도 - 습도 - 위치
  seed - soil - fertilizer - water - light - temperature - humidity - location

  예제의 seeds의 경우 다음과 같이 매핑이 완성된다. 마지막의 location에 주목.

  Seed 79, soil 81, fertilizer 81, water 81, light 74, temperature 78, humidity 78, location 82.
  Seed 14, soil 14, fertilizer 53, water 49, light 42, temperature 42, humidity 43, location 43.
  Seed 55, soil 57, fertilizer 57, water 53, light 46, temperature 82, humidity 82, location 86.
  Seed 13, soil 13, fertilizer 52, water 41, light 34, temperature 34, humidity 35, location 35.

  이렇게 얻어낸 location 중에서 가장 작은 값을 가진 35 가 답이다. (여기부터 씨앗을 심기 시작하기 위해서이다)
  ")

(def data-file (-> "aoc2023/input5.txt" io/resource slurp))

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
              (let [key-first b
                    key-last (+ b c -1)
                    val-first a]
                {:key-head key-first
                 :key-tail key-last
                 :val-head val-first})))
       (sort-by :key-head)))

(defn checker-fn
  "keynum이 checker를 통해 매핑된 숫자라면 매핑된 값을 리턴합니다.
  매핑된 숫자가 아니라면 nil 을 리턴합니다."
  [keynum
   {:keys [key-head key-tail val-head]}]
  (when (<= key-head keynum key-tail)
    (+ val-head (- keynum key-head))))

(defn keynum->val
  "여러 checker를 통해 keynum과 매핑된 값을 구합니다.
  만약 매핑된 값을 찾지 못한다면, 같은 키값을 리턴하는 규칙에 따라 keynum을 리턴합니다."
  [keynum checkers]
  (let [value (->> checkers
                   (map #(checker-fn keynum %))
                   (filter identity)
                   first)]
    (if (nil? value)
      keynum
      value)))

(comment
  (parse-numbers->checker " 12 345 63 34 63 34"))
; (def input-lines (s/split-lines data-file))

(def process
  "파종 단계의 순서를 정의합니다."
  [:seed->soil
   :soil->fertilizer
   :fertilizer->water
   :water->light
   :light->temperature
   :temperature->humidity
   :humidity->location])

(defn data-file->almanac
  "데이터파일을 읽어서 연감을 생성해 리턴합니다."
  [data-file]
  (let [splits (->>
                (s/replace data-file "\n" " ")
                (re-seq #"([a-z\-]+)(?: map)?: ([0-9 ]+)"))
        numbers (map rest splits)
        input-data (->> numbers
                        flatten
                        (apply hash-map))
        seeds (->> (input-data "seeds")
                   (re-seq #"\d+")
                   (map parse-long)
                   set)
        mapper #(parse-numbers->checker (input-data %))]
    {:seeds                 seeds
     :seed->soil            (mapper "seed-to-soil")
     :soil->fertilizer      (mapper "soil-to-fertilizer")
     :fertilizer->water     (mapper "fertilizer-to-water")
     :water->light          (mapper "water-to-light")
     :light->temperature    (mapper "light-to-temperature")
     :temperature->humidity (mapper "temperature-to-humidity")
     :humidity->location    (mapper "humidity-to-location")}))


(defn walk:seed->location
  "주어진 연감과 씨앗을 사용해 seed 부터 location 까지의 종합체를 생성해 리턴합니다."
  [almanac seed]
  (->> process
       (reduce (fn [acc curr]
                 (let [checkers (almanac curr)
                       keynum (:curr acc)
                       value (keynum->val keynum checkers)]
                   (assoc acc, curr value, :curr value)))
               {:curr seed
                :seed seed})))

(defn solve [data-file]
  (let [almanac (data-file->almanac data-file)]
    (->> almanac
         :seeds
         (map #(walk:seed->location almanac %))
         (map :humidity->location)
         (apply min))))

(comment
  (solve data-file)  ;; 227653707
  ;;
  )
