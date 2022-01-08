(ns codewars.6kyu.counting-duplicates)

(comment "
https://www.codewars.com/kata/54bf1c2cd5b56cc47f0007a1/train/clojure

Count the number of Duplicates

- 중복된 문자의 수를 리턴하는 함수를 작성하면 된다.
- 대소문자는 고려하지 않는다.
")

(defn duplicate-count [text]
  (->> text
       clojure.string/lower-case
       frequencies
       (filter (fn [[_ v]] (> v 1)))
       count))

(comment
  (duplicate-count "abcde")                                 ; 0
  (duplicate-count "aabbcde")                               ; 2
  (duplicate-count "aabBcde")                               ; 2
  (duplicate-count "indivisibility")                        ; 1
  (duplicate-count "Indivisibilities")                      ; 2
  (duplicate-count "aA11")                                  ; 2
  (duplicate-count "ABBA"))                                 ; 2
