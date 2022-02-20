(ns problems.collatz-problem-with-recur)

(defn next-collatz [n]
  (if (even? n)
    (/ n 2)
    (-> n (* 3) (+ 1))))

(defn collatz-sequence [vec]
  (let [num (last vec)]
    (if (= 1 num)
      vec
      (recur (conj vec (next-collatz num))))))

(comment
  (collatz-sequence [123])
  (collatz-sequence [7]))
