(ns problems.collatz-problem)

(defn next-collatz [n]
  (if (even? n)
    (/ n 2)
    (-> n (* 3) (+ 1))))

(defn collatz-sequence [n]
  (-> (into [] (take-while #(not= % 1)
                           (iterate next-collatz n)))
      (conj 1)))

(comment
  (collatz-sequence 123)
  (collatz-sequence 7))
