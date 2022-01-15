(ns codewars.7kyu.descending-order)

(comment "
https://www.codewars.com/kata/5467e4d82edf8bbf40000155/solutions/clojure

주어진 수에 포함된 모든 숫자를 내림차순으로 정렬하여 생성된 숫자를 리턴하는 함수를 작성하세요.
")

(defn desc-order [n]
  (->> n
       str
       sort
       reverse
       (apply str)
       Integer/parseInt))

(comment
  (desc-order 42145)                                        ; 54421
  (desc-order 145263)                                       ; 654321
  (desc-order 123456789))                                   ; 987654321
