(ns aoc2023.problem-2-1
  (:require [clojure.java.io :as io]
            [clojure.string :as s]))

(comment
  "## 문제
  https://adventofcode.com/2023/day/2

  가방에서 엘프가 랜덤으로 큐브 몇 개를 꺼내서 보여주고 다시 가방에 넣는다. 게임당 이런 일을 몇 번씩 한다.
  각 게임 아이디와, 해당 게임에서 확인한 큐브의 색과 수가 나열된 정보가 주어진다.

  (예제)
  Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
  Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue
  Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red
  Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red
  Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green

  Game 1에서는 가방에서 세 세트를 꺼냈다가 다시 넣었다.
    첫 세트는 3 blue, 4 red
    두번째 세트는 1 red, 2 green, 6 blue
    세번째 세트는 2 green

  만약 red가 12개, green이 13개, blue가 14개밖에 없다면 위의 게임들 중 가능한 게임이 있고 불가능한 게임이 잇다.
  예를 들어 Game 1, Game 2, Game 5 는 가능하지만,
  Game 3, Game 4 는 불가능하다.

  Game 3은 20 red 가 있기 때문에 12개의 red로는 할 수 없는 게임이다.
  Game 4는 15 blue 가 있기 때문에 14개의 blue로는 할 수 없는 게임이다.

  문제는 다음과 같다. red 12, green 13, blue 14 의 제한이 있을 때에만 가능한 게임의 아이디의 총합을 구하라.

  예제에서는 가능한 게임이 Game 1, Game 2, Game 5 이므로, 1 + 2 + 5 = 8 이 답이다.
  ")

(def data-file (-> "aoc2023/input2.txt" io/resource slurp))
(def input-lines (s/split-lines data-file))

(defn parse-game
  "Game 하나를 파싱해서 map 으로 만들어 리턴합니다.
  예:
    '3 blue, 4 red' => {:blue 3, :red 4}"
  [one-game]
  (->> (s/split one-game #",\s*")
       (map #(s/split % #" "))
       (map (fn [[number color]]
              [(keyword color) (parse-long number)]))
       (into {})
       (merge {:red 0 :green 0 :blue 0})))

(comment
  (parse-game "3 blue, 4 red") ; {:blue 3, :red 4, :green 0}
  (parse-game "1 red, 2 green, 6 blue") ; {:red 1, :green 2, :blue 6}
  (parse-game "2 green") ; {:green 2, :red 0, :blue 0}
  ;;
  )

(defn parse-a-line
  "문제 input 라인 한 줄을 읽어서 풀기 좋게 map으로 만들어 리턴합니다."
  [line]
  (let [[[input game-id remains]] (re-seq #"^Game +(\d+): (.*)$" line)
        games-text (s/split remains #"; ")
        games (map parse-game games-text)
        max-games (reduce (fn [acc game]
                            (merge-with max acc game))
                          {}
                          games)]
    {:game-id   (parse-long game-id)
     :max-games max-games
     :games     games
     :input     input}))

(comment
  (parse-a-line "Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green")
  ;;
  )

(comment
  ;; 해답
  (->> input-lines
       (map parse-a-line)
       (map #(select-keys % [:game-id :max-games]))
       (filter (fn [{{red   :red
                      green :green
                      blue  :blue} :max-games}]
                 (and (<= red 12)
                      (<= green 13)
                      (<= blue 14))))
       (map :game-id)
       (apply +))
  ;; 2348
  )
