(ns aoc2023.problem-4-2
  (:require [aoc2023.problem-4-1 :as part1]))

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
