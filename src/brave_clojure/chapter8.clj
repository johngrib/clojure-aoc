(ns brave-clojure.chapter8
  "https://www.braveclojure.com/writing-macros/")

; 199쪽
(defmacro my-print-whoopsie
  [expression]
  (list 'let ['result expression]
        (list 'println 'result)
        'result))

(comment
  (my-print-whoopsie 3))

; 202쪽 - 구문 인용
'+              ;; => +
'clojure.core/+ ;; => clojure.core/+
`+              ;; => clojure.core/+

'(+ 1 2) ;; => (+ 1 2)
`(+ 1 2) ;; => (clojure.core/+ 1 2)

`(+ 1 ~(inc 1)) ;; => (clojure.core/+ 1 2)
`(+ 1 (inc 1)) ;; => (clojure.core/+ 1 (clojure.core/inc 1))

(list '+ 1 (inc 1)) ;; => (+ 1 2)
`(+ 1 ~(inc 1))     ;; => (clojure.core/+ 1 2)

; 204쪽 - 매크로에서 구문 인용
(defmacro code-critic
  "Pharases are courtesy Hermes Conrad from Futurama"
  [bad good]
  (list 'do
        (list 'println
              "Great squid of Madrid, this is bad code:"
              (list 'quote bad))
        (list 'println
              "Sweet gorilla of Manila, this is good code:"
              (list 'quote good))))
#_:clj-kondo/ignore
(comment
  (code-critic (1 + 1) (+ 1 1)))
; Great squid of Madrid, this is bad code: (1 + 1)
; Sweet gorilla of Manila, this is good code: (+ 1 1)

(defmacro code-critic2
  "code-critic에 ` 를 사용해 개선"
  [bad good]
  `(do (println "Great squid of Madrid, this is bad code:"
                (quote ~bad))
       (println "Sweet gorilla of Manila, this is good code:"
                (quote ~good))))
#_:clj-kondo/ignore
(comment
  (code-critic2 (1 + 1) (+ 1 1)))
; Great squid of Madrid, this is bad code: (1 + 1)
; Sweet gorilla of Manila, this is good code: (+ 1 1)

(defn criticize-code
  [criticism code]
  `(println ~criticism (quote ~code)))

(defmacro code-critic3
  [bad good]
  `(do ~(criticize-code "Cursed bacteria of Liberia, this is bad code:" bad)
       ~(criticize-code "Sweet sacred boa of Western and Eastern Samoa, this is good code:" good)))

#_:clj-kondo/ignore
(comment
  (code-critic3 (1 + 1) (+ 1 1)))
; Cursed bacteria of Liberia, this is bad code: (1 + 1)
; Sweet sacred boa of Western and Eastern Samoa, this is good code: (+ 1 1)

(defmacro code-critic4
  "제대로 동작하지 않고 에러가 나는 매크로"
  [bad good]
  `(do ~(map #(apply criticize-code %)
             [["Great squid of Madrid, this is bad code:" bad]
              ["Sweet gorilla of Manila, this is good code:" good]])))
#_:clj-kondo/ignore
(comment
  (code-critic4 (1 + 1) (+ 1 1))) ; Error

; 207쪽 - unquote splicing ~@
`(+ ~(list 1 2 3))   ;; => (clojure.core/+ (1 2 3))
`(+ ~@(list 1 2 3))  ;; => (clojure.core/+ 1 2 3)

(defmacro code-critic4
  [good bad]
  `(do ~@(map #(apply criticize-code %)
              [["Sweet lion of Zion, this is bad code:" bad]
               ["Great cow of Moscow, this is good code:" good]])))
#_:clj-kondo/ignore
(comment
  (code-critic4 (1 + 1) (+ 1 1)))
; Sweet lion of Zion, this is bad code: (+ 1 1)
; Great cow of Moscow, this is good code: (1 + 1)

; 208쪽 - 변수 수집
(def message "Good job!")
(defmacro with-mischief
  "윗줄의 message를 사용하고 싶었지만 안쪽의 let에 바인딩된 message를 사용하게 되는 매크로"
  [& stuff-to-do]
  (concat (list 'let ['message "Oh, big deal!"])
          stuff-to-do))
(comment
  "Good job! 을 쓰고 싶었는데 매크로 내부에서 let message를 쓰는 바람에
  의도와 달리 Oh, big deal! 이 출력된다"
  (with-mischief
    (println "Here's how I feel about that thing you did: " message)))
"Here's how I feel about that thing you did:  Oh, big deal!"

; 209쪽
(defmacro without-mischief2
  "구문 인용 `를 사용해서 user/message를 사용하게 되는 매크로"
  [& stuff-to-do]
  `(let [message "Oh, big deal!"]
    ~@stuff-to-do))

(comment
  " 구문 인용 ` 를 사용했기 때문에 Can't let qualified name: user/message 예외가 발생"
  (without-mischief2
   (println "Here's how I feel about that thing you did: " message)))

; 만약 매크로 내부에서 let 으로 연결하고자 하면 gensym을 쓰면 된다.
; gensym은 호출할 때마다 고유의 기호를 만든다
(gensym) ;; => G__16359
(gensym 'message) ;; => message16456

(defmacro without-mischief3
  "gensym과 `를 사용해 매크로 내부에서 let 바인딩을 활용하는 예제"
  [& stuff-to-do]
  (let [macro-message (gensym 'message)]
    `(let [~macro-message "Oh, big deal!"]
       ~@stuff-to-do
       (println "I still need to say: " ~macro-message))))

(comment
  (without-mischief3
   (println "Here's how I feel about that thing you did: " message)))
"I still need to say:  Oh, big deal!"

; 210쪽 - auto-gensym : 뒤에 # 을 붙이면 자동으로 gensym이 된다.
`(blarg# blarg#) ;; =>(blarg__17215__auto__ blarg__17215__auto__)

`(let [name# "Larry Potter"] name#) ;; => (clojure.core/let [name__17315__auto__ "Larry Potter"] name__17315__auto__)

; 210쪽 - 중복 처리
(defmacro report
  [to-try]
  `(if ~to-try
     (println (quote ~to-try) "was successful:" ~to-try)
     (println (quote ~to-try) "was not successful:" ~to-try)))

;; Thread/sleep takes a number of milliseconds to sleep for
(comment
  (report (do (Thread/sleep 1000) (+ 1 1))))
; 출력은 아래와 같다. 두 번 실행되는 것이다.
; (do (Thread/sleep 1000) (+ 1 1)) was successful: 2
; (do (Thread/sleep 1000) (+ 1 1)) was successful: 2

(defmacro report2
  "report2의 문제(Thread/sleep이 두 번 호출되는 문제)를 해결한 매크로"
  [to-try]
  `(let [result# ~to-try]
     (if result#
       (println (quote ~to-try) "was successful:" result#)
       (println (quote ~to-try) "was not successful:" result#))))

(comment
  (report2 (do (Thread/sleep 1000) (+ 1 1))))
; 출력은 아래와 같다. 한 번만 실행된다.
; (do (Thread/sleep 1000) (+ 1 1)) was successful: 2

(report2 (= 1 1))
; (= 1 1) was successful: true
(report2 (= 1 2))
; (= 1 2) was not successful: false

"생각대로 작동하지 않는 doseq"
(doseq [code ['(= 1 1) (= 1 2)]]
  (report2 code))
; code was successful: (= 1 1)
; code was not successful: false

(defmacro doseq-macro
  [macroname & args]
  `(do
     ~@(map (fn [arg] (list macroname arg)) args)))
(doseq-macro report (= 1 1) (= 1 2))
; (= 1 1) was successful: true
; (= 1 2) was not successful: false

; 213쪽
(def order-details
  {:name  "Mitchard Blimmons"
   :email "mitchard.blimmonsgmail.com"})

(def order-details-validations
  {:name
   ["Please enter a name"
    not-empty]

   :email
   ["Please enter an email address"
    not-empty

    "Your email address doesn't look like an email address"
    #(or (empty? %) (re-seq #"@" %))]})

(defn error-messages-for
  "Return a seq of error messages"
  [to-validate message-validator-pairs]
  (map first (filter #(not ((second %) to-validate))
                     (partition 2 message-validator-pairs))))

(comment
  (error-messages-for "" ["Please enter a name" not-empty]))

(defn validate
  [to-validate validations]
  (reduce (fn [errors validation]
            (let [[fieldname validation-check-groups] validation
                  value (get to-validate fieldname)
                  error-messages (error-messages-for value validation-check-groups)]
              (if (empty? error-messages)
                errors
                (assoc errors fieldname error-messages))))
          {}
          validations))

(validate order-details order-details-validations)

; 216쪽 - if-valid
(let [errors (validate order-details order-details-validations)]
  (if (empty? errors)
    (println :success)
    (println :failure errors)))

(defmacro if-valid
  [to-validate validations errors-name & then-else]
  `(let [~errors-name (validate ~to-validate ~validations)]
     (if (empty? ~errors-name)
       ~@then-else)))

