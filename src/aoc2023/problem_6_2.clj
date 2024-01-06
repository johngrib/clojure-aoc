(ns aoc2023.problem-6-2
  (:require [aoc2023.problem-6-1 :as part1]
            [clojure.string :as s]))

(comment
  " https://adventofcode.com/2023/day/6#part2
  Part1 은 문제를 잘못 읽은 것이다.

  (Part1 예제)
  Time:      7  15   30
  Distance:  9  40  200

  실제로는 각 줄의 모든 숫자를 공백을 제거하고 하나의 숫자로 읽어야 한다.

  (Part2 예제)
  Time:       71530
  Distance:  940200

  입력 파일을 읽고 이기는 방법의 수를 구하시오.
  ")

(def input-lines
  (->> part1/data-file
       s/split-lines
       (map #(re-seq #"\d+" %))
       (map #(s/join "" %))
       (map parse-long)
       (apply (fn [time distance]
                {:time time :distance distance}))))

(comment
  (part1/solve [input-lines]) ; => 26187338
  ;;
  )
