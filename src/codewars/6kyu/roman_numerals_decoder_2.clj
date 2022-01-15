(ns codewars.6kyu.roman-numerals-decoder-2)

(comment "
https://www.codewars.com/kata/51b6249c4612257ac0000005/train/clojure

로마 숫자 형식의 문자열을 입력받아 그 값을 숫자로 해석해 리턴하는 함수를 작성하세요.
")

(def roman-number {"M"  1000
                   "CM" 900
                   "D"  500
                   "CD" 400
                   "C"  100
                   "XC" 90
                   "L"  50
                   "XL" 40
                   "X"  10
                   "IX" 9
                   "V"  5
                   "IV" 4
                   "I"  1})

(defn translate-roman-numerals [roman-number-string]
  (->> (re-seq #"CM|CD|XC|XL|IX|IV|[MDCLXVI]" roman-number-string)
       (map roman-number)
       (apply +)))

(comment
  (translate-roman-numerals "I")                            ; 1
  (translate-roman-numerals "IV")                           ; 4
  (translate-roman-numerals "MMVIII")                       ; 2008
  (translate-roman-numerals "MDCLXVI"))                     ; 1666
