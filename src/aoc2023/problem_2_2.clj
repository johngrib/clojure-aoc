(ns aoc2023.problem-2-2
  (:require [aoc2023.problem-2-1 :as part1]))

(comment
  "이번에는 각 Game의 각 큐브들의 최대값들의 곱을 구한다.
  
  (예제)
  Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
  Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue
  Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red
  Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red
  Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green

  예제를 보자.

  Game 1은 4 red, 2 green, 6 blue 만 있으면 Game 1이 가능해진다. 따라서 4 * 2 * 6 = 48.
  Game 2는 4 blue, 3 green, 1 red 만 있으면 Game 2가 가능해진다. 따라서 4 * 3 * 1 = 12.
  Game 3은 20 red, 13 green, 6 blue 만 있으면 Game 3이 가능해진다. 따라서 20 * 13 * 6 = 1560.
  Game 4는 14 red, 15 blue, 3 green 만 있으면 Game 4가 가능해진다. 따라서 14 * 15 * 3 = 630.
  Game 5는 6 red, 2 blue, 3 green 만 있으면 Game 5가 가능해진다. 따라서 6 * 2 * 3 = 36.

  따라서 48 + 12 + 1560 + 630 + 36 = 2286 이다.
  " )

(comment
  ;; 해답
  (->> part1/input-lines
       (map part1/parse-a-line)
       (map :max-games)
       (map vals)
       (map #(apply * %))
       (apply +))
  ;; 76008
  )
