(ns aoc2023.problem-7-1
  (:require [clojure.java.io :as io]
            [clojure.string :as s]))

(comment
  " https://adventofcode.com/2023/day/7

  Camel Cards 게임

  hands: 손에 들고 있는 5장의 카드 목록
  각 카드는 A, K, Q, J, T, 9, 8, 7, 6, 5, 4, 3, 2 중 하나이다.
  각 카드의 strength는 A 가 가장 높고, 2가 가장 낮다.

  족보 목록

  - Five of a kind: 5장 모두 같은 카드. 예) AAAAA
  - Four of a kind: 4장이 같은 카드. 예) AA8AA
  - Full house: 3장이 같고, 나머지 2장이 같은 카드. 예) 23332
  - Three of a kind: 3장만 같은 카드. 예) TTT98
  - Two pair: 2장씩 두 쌍이 같은 카드. 예) 23432
  - One pair: 2장만 같은 카드. 예) A23A4
  - High card: 모든 카드가 다른 경우. 예) 23456

  hands는 족보에 따라 순위가 매겨진다.
  만약 두 hands가 같은 족보라면...
    첫번째 카드를 비교해서, 첫째 카드가 더 강하면 첫번째 hands가 이긴다.
    첫번째 카드의 라벨이 같다면 두 번째 카드를 비교한다.
    이런 식으로 모든 카드를 비교해서 승패를 결정한다.

  (예시)
  33332 와 2AAAA 는 모두 Four.. 이지만, 첫번째 카드인 3이 2보다 강하기 때문에 33332 가 이긴다.
  77888 과 77788 은 모두 Full house이지만, 세번째 카드 때문에 77888 이 이긴다.

  각 핸드에는 입찰가가 있다.

  (예시)
  32T3K 765
  T55J5 684
  KK677 28
  KTJJT 220
  QQQJA 483

  핸드의 강함에 대해 역으로 매긴 순위를 곱한 만큼의 금액을 받는다.
  위의 예시에서 핸드는 5개이므로 가장 강한 핸드는 5위가 되며, 입찰 금액 * 5 만큼의 금액을 받게 된다.

  따라서 다음과 같이 된다.
  32T3K 765 => OnePair,   1위(최약), 765 * 1 =  765
  T55J5 684 => ThreeCard, 4위,       684 * 4 = 2736
  KK677 28  => TwoPair,   3위,       28  * 3 =   84
  KTJJT 220 => TwoPair,   2위,       220 * 2 =  440
  QQQJA 483 => ThreeCard, 5위(최강), 483 * 5 = 2415

  총 상금의 합계를 구하면 765*1 + 684*4 + 28*3 + 220*2 + 483*5 = 6440 이다.
  ")

(comment
  "문제를 쉽게 풀기 위한 커스텀 점수표를 정의하자.

  Five...: 6 00 00 00 00 00
  Four...: 5 00 00 00 00 00
  Full...: 4 00 00 00 00 00
  Three..: 3 00 00 00 00 00
  TwoPair: 2 00 00 00 00 00
  OnePair: 1 00 00 00 00 00
  High...:   00 00 00 00 00

  각 00 에 각 카드의 번호를 채운다.
  각 카드는 A 24, K 23, Q 22, J 21, T 20, 9 19, 8 18, 7 17, 6 16, 5 15, 4 14, 3 13, 2 12 이다.
  예를 들어 2AAAA 는 5 12 24 24 24 24 => 51224242424 점이다.
  ")

(def data-file (-> "aoc2023/input7.txt" io/resource slurp))
(def input-lines
  (->> data-file
       s/split-lines
       (map (fn [line]
              (let [[hand bid] (s/split line #"\s+")]
                {:hand hand
                 :bid  (parse-long bid)})))))

(def card-rank
  {"A" 24, "K" 23, "Q" 22, "J" 21, "T" 20,
   "9" 19, "8" 18, "7" 17, "6" 16, "5" 15, "4" 14, "3" 13, "2" 12})

(defn hand->point [hand]
  (let [cards (s/split hand #"")
        point (reduce (fn [acc card]
                        (+ (* acc 100)
                           (get card-rank card)))
                      0
                      cards)]
    point))

(comment
  (hand->point "AAAAA")
  (hand->point "22222")
  )

(defn evaluate-hand
  [^String hand]
  (let [card-packs (->> hand
                        (group-by identity)
                        vals
                        (sort-by count >))
        chunk-first (first card-packs)
        chunk-second (second card-packs)]

    (cond
      (= 5 (count chunk-first))
      {:type  :five-card
       :point (+ 60000000000 (hand->point hand))}

      (= 4 (count chunk-first))
      {:type  :four-card
       :point (+ 50000000000 (hand->point hand))}

      (and (= 3 (count chunk-first))
           (= 2 (count chunk-second)))
      {:type  :full-house
       :point (+ 40000000000 (hand->point hand))}

      (= 3 (count chunk-first))
      {:type  :three-card
       :point (+ 30000000000 (hand->point hand))}

      (and (= 2 (count chunk-first))
           (= 2 (count chunk-second)))
      {:type  :two-pair
       :point (+ 20000000000 (hand->point hand))}

      (= 2 (count chunk-first))
      {:type  :one-pair
       :point (+ 10000000000 (hand->point hand))}

      :else
      {:type  :high-card
       :point (hand->point hand)})))

(comment
  (evaluate-hand "AAAAA")
  (evaluate-hand "KKKKK")
  (evaluate-hand "22222")
  (evaluate-hand "22333")
  (evaluate-hand "23233")
  (evaluate-hand "22345")
  ;;
  )

(defn solve
  [input-lines]
  (->> input-lines
       (map (fn [{:keys [hand] :as item}]
              (let [result (evaluate-hand hand)]
                (merge item result))))
       (sort-by :point <)
       (map-indexed (fn [index item]
                      (let [rank (inc index)
                            money (* rank (:bid item))]
                        (merge item {:rank  rank
                                     :money money}))))
       (map :money)
       (apply +)))

(comment
  (solve input-lines) ; 250120186
  ;;
  )
