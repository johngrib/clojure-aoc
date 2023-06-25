(ns problems.palindrome)

(defn palindrome? [s]
  (= (seq s) (reverse s)))

(comment
  (palindrome? "raceca")
  (palindrome? "madam")
  (palindrome? "소주만병만주소")
  ;;
  )
