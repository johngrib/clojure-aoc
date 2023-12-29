(ns aoc2023.problem-4-2
  (:require [aoc2023.problem-4-1 :as part1]))

(comment
  " https://adventofcode.com/2023/day/4#part2

  Part 2에서는 포인트 개념이 없다.
  대신, 당첨된 숫자들의 수만큼 이어지는 게임 카드들을 획득할 수 있다.

  Part1의 예제를 사용해 Part2의 규칙을 설명해보자.

  Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53  => 4개 맞음(48, 83, 86, 17) => Card 2, 3, 4, 5 의 복제본을 추가로 획득
  Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19  => 2개 맞음(32, 61) => Card 3, 4 의 복제본을 추가로 획득
  Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1  => 2개 맞음(1, 21) => Card 4, 5 의 복제본을 추가로 획득
  Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83  => 1개 맞음(84) => Card 5 의 복제본을 추가로 획득
  Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36  => 0개 맞음 => 추가획득 카드 없음
  Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11  => 0개 맞음 => 추가획득 카드 없음

  이렇게 추가로 획득한 복제본 카드들에 대해서도 같은 규칙을 적용한다.
  단, 마지막 카드 번호를 초과하는 번호의 카드는 복제본을 얻을 수 없다.
  문제는 마지막에 도달할 때까지 처리한 카드들의 수가 몇인지를 구하는 것이다.
  ")

(defn solve [input-lines]
  (let [games (->> input-lines
                   (map part1/parse-line))
        games-map (group-by :game games)]
    (loop [remain-games games
           game-count 0]
      (let [this-game (first remain-games)
            remains (rest remain-games)
            match-count (:matched-count this-game)]
        (cond
          (nil? this-game)
          game-count

          (= 0 match-count)
          (recur remains (inc game-count))

          (< 0 match-count)
          (let [game-number (:game this-game)
                next-game-range (range (inc game-number) (+ game-number match-count 1))
                next-games (->> next-game-range
                                (map #(get games-map %))
                                (filter #(not (nil? %)))
                                flatten)]
            (recur (concat next-games remains) (inc game-count))))))))

(comment
  (solve ["Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53"
          "Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19"
          "Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1"
          "Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83"
          "Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36"
          "Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11"])  ;; 30
  (solve part1/input-lines)  ;; 5921508
  )
