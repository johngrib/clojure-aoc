(ns codewars.6kyu.roman-numerals-decoder
  (:require [clojure.string :as str]))

(comment "
https://www.codewars.com/kata/51b6249c4612257ac0000005/train/clojure

로마 숫자 형식의 문자열을 입력받아 그 값을 숫자로 해석해 리턴하는 함수를 작성하세요.
")

(defn roman-head->number [roman-symbol arabic-number]
  (let [roman-symbol-pattern (re-pattern roman-symbol)]
    (fn [{roman-number :roman-number
          number       :number
          :as          acc}]
      #_(println acc)
      (if (str/starts-with? roman-number roman-symbol)
        (recur {:number       (+ number arabic-number)
                :roman-number (str/replace-first roman-number roman-symbol-pattern "")})
        acc))))

(def roman-number-convertor (map
                              (fn [[symbol number]] (roman-head->number symbol number))
                              [["M" 1000]
                               ["CM" 900]
                               ["D" 500]
                               ["CD" 400]
                               ["C" 100]
                               ["XC" 90]
                               ["L" 50]
                               ["XL" 40]
                               ["X" 10]
                               ["IX" 9]
                               ["V" 5]
                               ["IV" 4]
                               ["I" 1]]))

(defn translate-roman-numerals [roman]
  (:number
    (reduce (fn [acc converter]
              (converter acc))
            {:roman-number roman :number 0}
            roman-number-convertor)))

(comment
  (translate-roman-numerals "I")                            ; 1
  (translate-roman-numerals "IV")                           ; 4
  (translate-roman-numerals "MMVIII")                       ; 2008
  (translate-roman-numerals "MDCLXVI"))                     ; 1666

