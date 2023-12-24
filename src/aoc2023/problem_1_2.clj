(ns aoc2023.problem-1-2
  (:require
   [clojure.java.io :as io]
   [clojure.string :as str]))

(comment
  "
  ## 문제
  https://adventofcode.com/2023/day/1#part2

  Part1 에 조건이 추가된다.
  one, two, three, four, five, six, seven, eight, nine 도 숫자로 취급한다.
  각 라인에 등장하는 첫번째 숫자와 마지막 숫자를 조합해 두자리 숫자를 만들고, 모두 합한 결과가 답이다.

  예:
  two1nine          -> 29
  eightwothree      -> 83
  abcone2threexyz   -> 13
  xtwone3four       -> 24
  4nineeightseven2  -> 42
  zoneight234       -> 14
  7pqrstsixteen     -> 76

  합계는 29 + 83 + 13 + 24 + 42 + 14 + 76 = 281 이다.
  ")

(def data-file (-> "aoc2023/input1.txt" io/resource slurp))
(def input-lines (str/split-lines data-file))

(def numbers-map
  {"one"   "1"
   "two"   "2"
   "three" "3"
   "four"  "4"
   "five"  "5"
   "six"   "6"
   "seven" "7"
   "eight" "8"
   "nine"  "9"})

(defn replace-number-fn-maker
  "numbers-map에 정의된 첫번째 숫자를 찾아서 치환한 결과를 리턴해주는 함수를 생성해 리턴합니다."
  [numbers-map]
  (let [num-regex (->> (keys numbers-map)
                       (str/join "|")
                       re-pattern)
        num-regex-anchored (->> (str "^(" num-regex ")")
                                re-pattern)]
    (fn [line]
      (cond
        (= 1 (count line))
        line

        (re-find #"^[1-9]" line)
        line

        (re-find num-regex-anchored line)
        (str/replace-first line num-regex numbers-map)

        :else
        (recur (subs line 1))))))

(def replace-first-number-fn (replace-number-fn-maker
                              numbers-map))
(def replace-last-number-fn (replace-number-fn-maker
                             (zipmap (map str/reverse (keys numbers-map))
                                     (vals numbers-map))))

(comment
  (->> input-lines
       ;; 알파벳 숫자 전처리.
       (map replace-first-number-fn)
       (map str/reverse)
       (map replace-last-number-fn)
       (map str/reverse)
       ;; 이후는 Part 1 과 같음.
       ;; 각 행에서 숫자 char만 추출한다
       (map (fn [line] (filter #(Character/isDigit %) line)))
       ;; 첫째 숫자와 마지막 숫자를 얻는다
       (map (fn [numbers] [(first numbers) (last numbers)]))
       ;; 두 숫자를 조합해 두 자리 숫자 하나를 만든다
       (map (fn [[a b]] (Integer/parseInt (str a b))))
       ;; 합계를 구한다
       (apply +))
  ;; => 54418
  ;;
  )
