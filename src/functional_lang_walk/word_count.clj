(ns functional-lang-walk.word-count
  (:require [clojure.string :as str]
            [clojure.java.io :as io]))

(comment
  "map 과 mapcat의 비교"

  (map #(str/split % #" ")
       ["a b" "c d"])
  ;; => (["a" "b"] ["c" "d"])

  (mapcat #(str/split % #" ")
          ["a b" "c d"])
  ;; => ("a" "b" "c" "d")
  ;;
  )

(def content
  (-> "functional_lang_walk/textfile"
      io/resource
      slurp))

(def line-list
  (str/split content #"\n"))

(def word-list
  (mapcat #(str/split % #" ") line-list))


(comment
  (reduce (fn [acc_map element]
            (if (nil? (get acc_map element))
              (assoc acc_map element 1)
              (update acc_map element inc)))
          {}
          word-list))
; {"I" 1,
;  "like" 2,
;  "functional" 2,
;  "programming." 1,
;  "Do" 1,
;  "you" 1,
;  "programming" 1,
;  "too?" 1}
