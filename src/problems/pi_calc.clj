(ns problems.pi-calc
  "pi값 계산")

 #_"pi = 4/1 - 4/3 + 4/5 - 4/7 + 4/9 - 4/11 + ..."

(def leibniz-seq
  (iterate
   (fn [x]
     (let [sign (pos? x)
           next-sign (if sign -1 1)
           next-num (+ 2 (abs x))]
       (* next-sign next-num)))
   1))

(comment
  (->> (take 1000 leibniz-seq)
       (map #(/ 4 %))
       (apply +)
       double
       (format "%.5f")
       )
  ;;
  )
