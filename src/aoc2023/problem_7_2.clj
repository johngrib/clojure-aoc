(ns aoc2023.problem-7-2
  (:require [aoc2023.problem-7-1 :as part1]
            [clojure.string :as s]))

(comment
  " https://adventofcode.com/2023/day/7#part2

  Part1에 Joker 카드가 추가된다.
  이제부터 J 는 가장 약한 개별 카드로 규칙이 바뀐다.
  이제 각 카드의 강함의 순서는 A, K, Q, T, 9, 8, 7, 6, 5, 4, 3, 2, J 가 된다.

  J는 핸드를 평가할 때 가장 '적합한 카드'인 것처럼 취급할 수 있다.
  예를 들어 QJJQ2 는 QQQQ2 로 평가할 수 있다.

  그러나 같은 종류의 두 핸드를 비교할 때 J는 '적합한 카드'가 아니라 J로 취급된다.
  따라서 JKKK2 와 QQQQ2 는 둘 다 FourCard 이지만 QQQQ2 가 더 강하다.

  (예시)
  32T3K 765  => OnePair,  1위(최약), 765 * 1 =  765
  T55J5 684  => FourCard, 3위,       684 * 3 = 2052
  KK677 28   => TwoPair,  2위,       28  * 2 =   56
  KTJJT 220  => FourCard, 5위(최강), 220 * 5 = 1100
  QQQJA 483  => FourCard, 4위,       483 * 4 = 1932

  총 상금의 합은 765 + 2052 + 56 + 1100 + 1932 = 5905 이다.
  ")

(def input-lines
  (->> part1/data-file
       s/split-lines
       (map (fn [line]
              (let [[hand bid] (s/split line #"\s+")]
                {:hand hand
                 :bid  (parse-long bid)})))))

(def card-rank
  {"A" 24, "K" 23, "Q" 22, #_#_"J" 21, "T" 20,
   "9" 19, "8" 18, "7" 17, "6" 16, "5" 15, "4" 14, "3" 13, "2" 12, "J" 11})

(defn hand->point [hand]
  (let [cards (s/split hand #"")
        point (reduce (fn [acc card]
                        (+ (* acc 100)
                           (get card-rank card)))
                      0
                      cards)]
    point))

(defn count-j [hand]
  (->> (seq hand)
       (filter #(= % \J))
       count))

(comment
  (count-j "QJJQ2") ; => 2
  (count-j "QJJQJ") ; => 3
  )

(defn evaluate-hand
  [^String hand]
  (let [j-count (count-j hand)
        card-packs (->> (s/replace hand #"J" "")
                        (group-by identity)
                        vals
                        (sort-by count >))
        chunk-first (first card-packs)
        chunk-second (second card-packs)]

    (cond
      (<= 5 (+ j-count (count chunk-first)))
      {:type  :five-card
       :point (+ 60000000000 (hand->point hand))}

      (<= 4 (+ j-count (count chunk-first)))
      {:type  :four-card
       :point (+ 50000000000 (hand->point hand))}

      (and (= 3 (+ j-count (count chunk-first)))
           (= 2 (count chunk-second)))
      {:type  :full-house
       :point (+ 40000000000 (hand->point hand))}

      (= 3 (+ j-count (count chunk-first)))
      {:type  :three-card
       :point (+ 30000000000 (hand->point hand))}

      (and (= 2 (+ j-count (count chunk-first)))
           (= 2 (count chunk-second)))
      {:type  :two-pair
       :point (+ 20000000000 (hand->point hand))}

      (= 2 (+ j-count (count chunk-first)))
      {:type  :one-pair
       :point (+ 10000000000 (hand->point hand))}

      :else
      {:type  :high-card
       :point (hand->point hand)})))

(comment
  (evaluate-hand "JJJJJ") ; five-card
  (evaluate-hand "JJJJ2") ; five-card
  (evaluate-hand "JJJ22") ; five-card
  (evaluate-hand "JJ222") ; five-card
  (evaluate-hand "J2222") ; five-card
  (evaluate-hand "JJJ23") ; four-card
  (evaluate-hand "JJ223") ; four-card
  (evaluate-hand "J2233") ; full-house
  (evaluate-hand "J2345") ; one-pair
  (evaluate-hand "J3345") ; three-card
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
       (apply +)
       ))

(comment
  (solve input-lines) ; 250665248
  )
