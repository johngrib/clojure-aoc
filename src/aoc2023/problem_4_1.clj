(ns aoc2023.problem-4-1
  (:require [clojure.java.io :as io]
            [clojure.string :as s]))

(comment
  " https://adventofcode.com/2023/day/4

  당첨카드 문제

  | 왼쪽은 당첨 번호들이고, | 오른쪽은 응모자가 갖고 있는 번호들이다.
  각 게임당 당첨 번호의 수만큼 2를 곱해가며 점수를 얻는다.
  1개가 맞으면 1점, 2개가 맞으면 2점, 3개가 맞으면 4점, 4개가 맞으면 8점... 이런 식.

  (예시)
  Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53  => 4개 맞음(48, 83, 86, 17) => 8점
  Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19  => 2개 맞음(32, 61) => 2점
  Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1  => 2개 맞음(1, 21) => 2점
  Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83  => 1개 맞음(84) => 1점
  Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36  => 0개 맞음 => 0점
  Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11  => 0개 맞음 => 0점

  따라서 예시의 총점은 8 + 2 + 2 + 1 + 0 + 0 = 13 점이다.

  문제: 입력 파일의 모든 게임의 총점을 구하라.
  ")

(def data-file (-> "aoc2023/input4.txt" io/resource slurp))
(def input-lines (s/split-lines data-file))

(defn parse-line
  [line]
  (let [parsed (re-seq #"^Card\s+(\d+):([^|]+)\|([^|]+)$" line)
        [_
         game-number
         win-numbers
         my-numbers] (first parsed)
        wins (->> win-numbers (re-seq #"\d+") (map parse-long) set)
        mine (->> my-numbers (re-seq #"\d+") (map parse-long))
        matched (->> mine (filter wins))]
    {:game    (parse-long game-number)
     :win     wins
     :mine    mine
     :matched matched
     :matched-count (count matched)}))

(defn solve [input-lines]
  (->> input-lines
       (map parse-line)
       (map :matched-count)
       (filter #(> % 0))
       (map #(Math/pow 2 (dec %)))
       (apply +)))

(comment
  ;; 샘플
  (solve ["Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53"
          "Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19"
          "Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1"
          "Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83"
          "Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36"
          "Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11"])  ;; 13

  (solve input-lines) ;; 18653
  ;;
  )

