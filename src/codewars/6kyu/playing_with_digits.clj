(ns codewars.6kyu.playing-with-digits)

(comment "
https://www.codewars.com/kata/5552101f47fc5178b1000050/clojure

다음과 같은 특징을 갖는 숫자들이 있습니다.

89  --> 8^1 + 9^2 = 89 = 89 * 1
695 --> 6^2 + 9^3 + 5^4 = 1390 = 695 * 2
46288 --> 4^3 + 6^4 + 2^5 + 8^6 + 8^7 = 2360688 = 46288 * 51

양의 정수 n과 양의 정수 p가 주어졌을 때,
숫자 n의 각 자리에 있는 수에 p 부터 시작하는 거듭제곱을 적용해 더했을 때 k*n 이 만들어지는 양의 정수 k를 찾고자 합니다.

이를 식으로 표현하자면 다음과 같습니다. 정수 n을 이루는 각 숫자를 a,b,c,d, ... 로 표현한다면..

(a^p + b^(p+1) + c^(p+2) + d^(p+3) + ...) = n * k

위의 식을 만족시키는 k를 리턴하면 됩니다. 만약 k 를 찾을 수 없다면 -1 을 리턴합니다.

예)
- n = 89, p = 1
  - 8^1 + 9^2 = 89 * 1.
  - 그러므로 k 는 1.

- n = 92, p = 1
  - 만족하는 값이 없으므로 리턴값은 -1.

- n = 46288, p = 3
  - 4^3 + 6^4 + 2^5 + 8^6 + 8^7 = 2360688
  - 2360688 = 46288 * 51 이므로 k 는 51.
")

(defn digits
  "주어진 수의 각 자리수를 분해한 리스트를 생성해 리턴합니다."
  [n]
  (->> n
       (iterate #(quot % 10))
       (take-while pos?)
       (map #(rem % 10))
       reverse))

(comment
  (digits 123)                                              ; (1 2 3)
  (digits 524)                                              ; (5 2 4)
  (digits 504))                                             ; (5 0 4)

(defn **
  "n의 p 제곱을 리턴합니다."
  [n p]
  (reduce * (repeat p n)))

(comment
  (** 2 10)                                                 ; 1024
  (** 3 2)                                                  ; 9
  (** 10 3))                                                ; 1000

(defn dig-pow [n p]
  (let [forms (->> (digits n)
                   (map-indexed (fn [i num]
                                  (let [power (+ i p)]
                                    {:number num
                                     :power  power
                                     :result (** num power)}))))
        sum (reduce + (map :result forms))]
    (if (zero? (rem sum n))
      (/ sum n)
      -1)))

(comment
  (dig-pow 89 1)                                            ; 1
  (dig-pow 92 1)                                            ; -1
  (dig-pow 695 2)                                           ; 2
  (dig-pow 46288 3))                                        ; 51

