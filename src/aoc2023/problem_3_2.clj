(ns aoc2023.problem-3-2
  (:require [aoc2023.problem-3-1 :as part1]))

(defn part->gear-ratio
  [{fence :fence}]
  (if (= 2 (count fence))
    (->> fence
         (map :number)
         (apply *))
    0))

(defn solve-part2 []
  (let [x-max (count (get part1/input-lines 0))
        y-max (count part1/input-lines)
        xy-all (for [y (range 0 y-max)
                     x (range 0 x-max)]
                 {:x x :y y})]
    (->> xy-all
         (map (fn [node]
                (assoc node :char (part1/get-char-at node))))
         ;; * 만 필터링한다
         (filter #(= \* (:char %)))
         ;; * 주위의 모든 숫자를 구한다
         (map #(assoc % :fence (part1/get-fence-numbers-at %)))
         (map part->gear-ratio)
         (apply +)
         )))

(comment
  (solve-part2)  ;; 84289137
  )
