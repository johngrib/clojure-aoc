(ns brave-clojure.chapter7
  "https://www.braveclojure.com/read-and-eval/"
  (:require [clojure.java.io :as io]))

(defmacro backwards
  [form]
  (reverse form))

#_:clj-kondo/ignore
(backwards (" backwards" " am" "I" str))
; "I am backwards"

(def addition-list (list + 1 2))
(eval addition-list) ;; => 3
(eval (concat addition-list [10])) ;; => 13
(eval (list 'def 'lucky-number (concat addition-list [10]))) ;; => #'brave-clojure.chapter7/lucky-number

(read-string "(+ 1 2)") ;; => (+ 1 2)
(list? (read-string "(+ 1 2)")) ;; => true
(conj (read-string "(+ 1 2)") :zagglewag) ;; => (:zagglewag + 1 2)
(eval (read-string "(+ 1 2)")) ;; => 3

(#(+ 1 %) 3) ;; => 4
(read-string "#(+ 1 %)") ;; => (fn* [p1__14712#] (+ 1 p1__14712#))

(read-string "'(a b c)") ;; => (quote (a b c))
(read-string "@var") ;; => (clojure.core/deref var)
(read-string "; ignore!\n(+ 1 2)") ;; => (+ 1 2)

(read-string "+") ;; => +
(type (read-string "+")) ;; => clojure.lang.Symbol
(list (read-string "+") 1 2) ;; => (+ 1 2)

(eval (list (read-string "+") 1 2)) ;; => 3

(eval (read-string "()")) ;; => ()

; 190쪽 - 매크로
(read-string "(1 + 1)")
(let [infix (read-string "(1 + 1)")]
  (list (second infix) (first infix) (last infix))) ;; => (+ 1 1)

; 잘 동작하긴 하지만 세련되어 보이지는 않는다.
(eval
 (let [infix (read-string "(1 + 1)")]
   (list (second infix) (first infix) (last infix)))) ;; => 2

; 이런 상황이니 매크로를 사용해 보자.
(defmacro ignore-last-operand
  [function-call]
  (butlast function-call))

(ignore-last-operand (+ 1 2 10)) ;; => 3
(ignore-last-operand (+ 1 2 (println "look at me!!!"))) ;; => 3

; 192쪽 - macroexpand
(macroexpand '(ignore-last-operand (+ 1 2 10))) ;; => (+ 1 2)
(macroexpand '(ignore-last-operand (+ 1 2 (println "look at me!!!")))) ;; => (+ 1 2)

; 재미 삼아 만드는 중위 표기법 처리 매크로
(defmacro infix
  [infixed]
  (list (second infixed)
        (first infixed)
        (last infixed)))

#_:clj-kondo/ignore
(infix (1 + 2)) ;; => 3

; 193쪽
(defn read-resource
  "Read a resource into a string"
  [path]
  (read-string (slurp (io/resource path))))

; 위의 함수를 thread-macro 로 변환하면..
(defn read-resource
  "Read a resource into a string"
  [path]
  (-> path
      io/resource
      slurp
      read-string))


