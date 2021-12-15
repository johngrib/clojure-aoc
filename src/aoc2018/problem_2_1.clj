(ns aoc2018.problem-2-1
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(def data-file (-> "aoc2018/input2.txt" (io/resource) (slurp)))
(def input-strings (str/split-lines data-file))

(defn solve-2-1
  [string-list]
  (let [
        count-candidates (->> string-list (map frequencies) (map vals) (map set))
        two-times (->> count-candidates (filter #(% 2)) (count))
        three-times (->> count-candidates (filter #(% 3)) (count))
        ]
    (* two-times three-times)))


