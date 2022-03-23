(ns brave-clojure.chapter3
  (:require [clojure.string :as str]))

;; 42쪽
(+ 1 2 3)
(str "It was the panda " "in the library " "with a dust buster")

;; 43쪽
(if true
  "By Zeus's hammer !"
  "By Aquaman's trident!")

;; 44쪽
(if false
  "By Zeus's hammer !"
  "By Aquaman's trident!")

(if false                                                   ; => nil
  "By Odin's Elbow")

(if true
  (do (println "Success")
      "By Zeus's hammer !")
  (do (println "Failure!")
      "By Aquaman's trident!"))

;; 45쪽
(when true
  (println "Success")
  "abra cadabra")

(nil? 1)
(nil? nil)

(if "bears eat beets"
  "bears beets Battlestar Galactica")

(if nil
  "This won't be the result because nil if falsey"
  "nil is falsey")

;; 46쪽
(= 1 1)
(= nil nil)                                                 ; => true
(= 1 2)

(or false nil :large_I_mean_venti :why_cant_I_just_say_large) ; => :large_I_mean_venti
(or (= 0 1) (= "yes" "no"))                                 ; => false
(or nil)                                                    ; => nil

;; 47쪽
(and :free_wifi :hot_coffee)                                ; => :hot_coffee
(and :feelin_super_cool nil false)                          ; => nil
(def failed-protagonist-names
  ["Larry Potter", "doreen the Explorer", "The Incredible Bulk"])

;; 48쪽
(defn error-message
  [severity]
  (str "OH GOD! IT'S A DISASTER! WE'RE "
       (if (= severity :mild)
         "MILDLY INCONVENIENCED!"
         "DOOOOOOOMED!")))

(error-message :mild)

;; 57쪽
(or + -)
((or + -) 1 2 3)
((and (= 1 1) +) 1 2 3)
((first [+ 0]) 1 2 3)

;; 58쪽
(inc 1.1)                                                   ; => 2.1
(map inc [0 1 2 3])                                         ; => (1 2 3 4)

;; 62쪽
(defn x-chop
  "상대를 가격할 때 손으로 내려치는 종류를 기술함."
  ([name chop-type]
   (str "I " chop-type " chop " name "! Take that!"))
  ([name]
   (x-chop name "karate")))

(x-chop "Kanye West" "slap")
(x-chop "Kanye East")

;; 63쪽
(defn codger-communication
  [whippersnapper]
  (str "Get off my lawn, " whippersnapper "!!!"))

(defn codger
  [& whippersnappers]
  (map codger-communication whippersnappers))

(codger "Billy" "Anne-Marie" "The Incredible Bulk")

;; 69쪽
(defn inc-maker
  "Create a custom incrementor"
  [inc-by]
  #(+ % inc-by))

(def inc3 (inc-maker 3))
(inc3 7)

;; 70쪽
(def asym-hobbit-body-parts
  [{:name "head" :size 3}
   {:name "left-eye" :size 1}
   {:name "left-ear" :size 1}
   {:name "mouth" :size 1}
   {:name "nose" :size 1}
   {:name "neck" :size 2}
   {:name "left-shoulder" :size 3}
   {:name "left-upper-arm" :size 3}
   {:name "chest" :size 10}
   {:name "back" :size 10}
   {:name "left-forearm" :size 3}
   {:name "abdomen" :size 6}
   {:name "left-kidney" :size 1}
   {:name "left-hand" :size 2}
   {:name "left-knee" :size 2}
   {:name "left-thigh" :size 4}
   {:name "left-lower-leg" :size 3}
   {:name "left-achilles" :size 1}
   {:name "left-foot" :size 2}])

; 왼쪽 파트를 참고해서 오른쪽 파트를 만드는 함수
(defn matching-part
  [part]
  {:name (str/replace (:name part) #"^left-" "right-")
   :size (:size part)})

(defn symmetrize-body-parts
  ":name과 :size를 갖는 map의 시퀀스를 받아서 빠진 오른쪽 파트를 채워넣은 컬렉션을 리턴합니다."
  [asym-body-parts]
  (loop [remaining-asym-parts asym-body-parts
         result []]
    (if (empty? remaining-asym-parts)
      ; 남아있는 파츠가 없다면 결과를 리턴한다
      result
      ; 남아있는 파츠가 있다면 재귀한다
      (let [[part & remaining] remaining-asym-parts]
        (recur remaining
               (into result
                     (set [part (matching-part part)])))))))

; 오른쪽 파츠까지 채운 호빗을 리턴한다.
(symmetrize-body-parts asym-hobbit-body-parts)

; 79쪽
(defn my-reduce
  ([f initial coll]
   (loop [result initial
          remaining coll]
     (if (empty? remaining)
       result
       (recur (f result (first remaining)) (rest remaining)))))
  ([f [head & tail]]
   (my-reduce f head tail)))

(defn better-symmetrize-body-parts
  [asym-body-parts]
  (reduce (fn [final-body-parts part]
            (into final-body-parts (set [part (matching-part part)])))
          []
          asym-body-parts))

; 80쪽
(defn hit
  [asym-body-parts]
  (let [sym-parts (better-symmetrize-body-parts asym-body-parts) ; 대칭 파츠를 맞춘 호빗
        body-parts-size-sum (reduce + (map :size sym-parts)) ; 몸 전체 사이즈
        target (rand body-parts-size-sum)]                  ; 랜덤 숫자
    ; 몸의 모든 부분을 순서대로 돌면서 랜덤 숫자에 해당하는 파츠(맞은 파츠)를 리턴한다.
    (loop [[part & remaining] sym-parts
           accumulated-size (:size part)]
      (if (> accumulated-size target)
        part
        (recur remaining (+ accumulated-size (:size (first remaining))))))))

(comment
  (hit asym-hobbit-body-parts))                             ; 랜덤으로 호빗의 몸 부위 중 하나를 때린다.

;; 연습문제 2 - 100 더하는 함수 만들기
(defn plus100
  [n]
  (+ n 100))

(plus100 2)

;; 연습문제 3 - dec-maker 만들기
(defn dec-maker
  [n]
  #(- % n))

((dec-maker 9) 10)

;; 연습문제 4 - map과 똑같이 동작하는 mapset 함수 만들기
(defn mapset
  [f coll]
  (into #{}
        (map f coll)))

(comment
  (mapset inc [1 1 2 2]))

(defn mapset1
  ([f coll]
   (mapset1 f coll #{}))
  ([f coll acc]
   (if (empty? coll)
     acc
     (mapset1 f
              (rest coll)
              (conj acc
                    (f (first coll)))))))

(comment
  (mapset1 inc [1 2 3]))

(defn mapset2
  [f coll]
  (reduce (fn [acc v]
            (conj acc (f v)))
          #{}
          coll))

(comment
  (mapset2 inc [1 2 3]))

;; 연습문제 5 - 눈,팔,다리 등이 모두 다섯개인 우주괴물
(defn symmetrize-body-parts-alien
  [asym-body-parts config]
  (loop [[part & remain] asym-body-parts
         result []]
    (cond
      ; 파트가 없을 경우 result를 리턴한다.
      (nil? part)
      result
      ; 설정에 해당 파츠의 숫자가 있다면 숫자를 늘려서 추가한다
      (config (:name part))
      (recur remain (into result (for [x (range (config (:name part)))]
                                   (merge part {:id x}))))
      ; 설정에 없다면 그냥 추가한다.
      :else
      (recur remain (conj result (merge part {:id 0}))))))

(defn hit-alien
  [asym-body-parts]
  (let [sym-parts asym-body-parts
        body-parts-size-sum (reduce + (map :size sym-parts)) ; 몸 전체 사이즈
        target (rand body-parts-size-sum)]                  ; 랜덤 숫자
    ; 몸의 모든 부분을 순서대로 돌면서 랜덤 숫자에 해당하는 파츠(맞은 파츠)를 리턴한다.
    (loop [[part & remaining] sym-parts
           accumulated-size (:size part)]
      (if (> accumulated-size target)
        part
        (recur remaining (+ accumulated-size (:size (first remaining))))))))

(comment
  (hit-alien (symmetrize-body-parts-alien [{:name :head :size 3}
                                           {:name :eye :size 1}
                                           {:name :ear :size 1}
                                           {:name :mouth :size 1}
                                           {:name :nose :size 1}
                                           {:name :neck :size 1}
                                           {:name :arm :size 1}
                                           {:name :leg :size 1}]
                                          {:eye 5
                                           :arm 5
                                           :leg 5})))

