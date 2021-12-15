(ns problem-1-2)

(defn solve-1-2
  "주어진 숫자열을 순서대로 덧셈하다 처음으로 두 번째로 나오는 수를 리턴합니다.
  https://adventofcode.com/2018/day/1 "
  ([numbers last-sum number-set]
   (let [next-sum (+ last-sum (first numbers))]
     (if (number-set next-sum)
       ; return
       next-sum
       (recur
         (rest numbers)
         next-sum
         (conj number-set next-sum))
       )))
  ([numbers] (solve-1-2 numbers 0 #{0}))
  )
