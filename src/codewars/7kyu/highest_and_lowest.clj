(ns codewars.7kyu.highest-and-lowest
  (:require [clojure.string :as string]))

(comment "
https://www.codewars.com/kata/554b4ac871d6813a03000035/train/clojure

- 스페이스로 구분된 여러 숫자들을 표현한 String 하나가 주어진다.
- 가장 큰 숫자와 가장 작은 숫자를 찾아서, 가장 큰 숫자와 가장 작은 숫자를 스페이스로 구분해 표현한 문자열을 리턴하는 함수를 만들면 된다.

예
- 입력: '6 -3 1 -7'
- 답: '6 -7'

- 입력: '1 2 -3 4 5'
- 답: '5 -3'
")

(defn high-and-low [s]
  (let [numbers (->> (string/split s #"\s+")
                     (map #(Integer/parseInt %))
                     sort)]
    (str (last numbers) " " (first numbers))))

(comment
  (high-and-low "1 2 3 4 5")                                ; "5 1"
  (high-and-low "1 2 -3 4 5")                               ; "5 -3"
  (high-and-low "1 9 3 4 -5"))                              ; "9 -5"

