(ns brave-clojure.chapter4
  "Chapter 4를 학습
  https://www.braveclojure.com/core-functions-in-depth/"
  (:require [clojure.string :as str]))

; 84쪽
; 클로저는 map 과 reduce 함수를 자료구조 관점이 아닌 시퀀스 추상화 (sequence abstraction) 관점에서 정의한다.

; 86쪽
(defn titleize
  [topic]
  (str topic " for the Brave and True"))

(map titleize ["Hamsters" "Ragnarok"])
(map titleize '("Empathy" "Decorating"))
(map titleize #{"Elbows" "Soap Carving"})
(map #(titleize (second %)) {:uncomfortable-thing "Winking"})

; 90쪽
(seq '(1 2 3))
(seq [1 2 3])
(seq #{1 2 3})
(seq {:name "Bill Compton" :occupation "Dead mopey guy"})

(into {} (seq {:a 1 :b 2 :c 3}))

; 92쪽
(map inc [1 2 3])

(map str ["a" "b" "c"] ["A" "B" "C"])
(list (str "a" "A") (str "b" "B") (str "c" "C"))

; 흡혈귀가 사람 피와 동물의 피를 나흘간 어떻게 흡입했는지를 보여주는 문제
(def human-consumption [8.1 7.3 6.6 5.0])
(def critter-consumption [0.0 0.2 0.3 1.1])
(defn unify-diet-data
  [human critter]
  {:human   human
   :critter critter})

(comment
  (map unify-diet-data human-consumption critter-consumption))

; 93쪽 - 함수 집합을 map 함수에 인자로 넘기기
(def sum #(reduce + %))
(def avg #(/ (sum %) (count %)))
(defn stats
  [numbers]
  (map #(% numbers) [sum count avg]))

(comment
  (stats [3 4 10])
  (stats [80 1 44 13 6]))

; 94쪽 - map 활용
(def identities
  [{:alias "Batman" :real "Bruce Wayne"}
   {:alias "Superman" :real "Clark Kent"}
   {:alias "Spiderman" :real "Peter Parker"}])
(comment
  (map :real identities))

; reduce
(reduce (fn [new-map [key val]]
          (assoc new-map key (inc val)))
        {}
        {:max 30 :min 10})
(reduce (fn [new-map [key val]]
          (if (> val 4)
            (assoc new-map key val)
            new-map))
        {}
        {:human   4.1
         :critter 3.9})

; 95쪽 - take, drop, take-while, drop-while
(take 3 [11 22 33 44 55 66 77 88 99])
(drop 3 [11 22 33 44 55 66 77 88 99])

(def food-journal
  [{:month 1 :day 1 :human 5.3 :critter 2.3}
   {:month 1 :day 2 :human 5.1 :critter 2.0}
   {:month 2 :day 1 :human 4.9 :critter 2.1}
   {:month 2 :day 2 :human 5.0 :critter 2.5}
   {:month 3 :day 1 :human 4.2 :critter 3.3}
   {:month 3 :day 2 :human 4.0 :critter 3.8}
   {:month 4 :day 1 :human 3.7 :critter 3.9}
   {:month 4 :day 2 :human 3.7 :critter 3.6}])

(comment
  ; 1월과 2월 자료만 얻기
  (take-while #(< (:month %) 3) food-journal)
  ; 3, 4월 자료 얻기
  (drop-while #(< (:month %) 3) food-journal))

; 97쪽 - filter, some
(comment
  ; filter - take-while, drop-while과는 달리 컬렉션 전체를 평가한다
  (filter #(< (:human %) 5) food-journal)
  (filter #(< (:month %) 3) food-journal)

  ; some - 단정 함수가 참으로 판정한 첫 번째 값을 발견하면 true를 리턴한다
  (some #(> (:critter %) 5) food-journal)
  (some #(> (:critter %) 3) food-journal)

  ; 이렇게 하면 위와 비슷하게 작동하지만 true로 평가된 값을 리턴한다.
  (some #(and (> (:critter %) 3) %) food-journal))

; 99쪽 - lazy seq
(def vampire-database
  {0 {:makes-blood-puns? false, :has-pulse? true  :name "McFishwich"}
   1 {:makes-blood-puns? false, :has-pulse? true  :name "McMackson"}
   2 {:makes-blood-puns? true,  :has-pulse? false :name "Damon Salvatore"}
   3 {:makes-blood-puns? true,  :has-pulse? true  :name "Mickey Mouse"}})

(defn vampire-related-details
  [social-security-number]
  (Thread/sleep 1000)
  (get vampire-database social-security-number))

(defn vampire?
  [record]
  (and (:makes-blood-puns? record)
       (not (:has-pulse? record))
       record))

(defn identify-vampire
  [social-security-numbers]
  (first (filter vampire?
                 (map vampire-related-details
                      social-security-numbers))))

(time (vampire-related-details 0))

(comment
  (time (vampire-related-details 0))
  (time (def mapped-details (map vampire-related-details (range 0 1000000))))
  ;; 32초 정도 걸림. 다운된 거 아님.
  (time (first mapped-details))
  ;; 32초 걸림.
  (time (identify-vampire (range 0 1000000))))

; 102쪽 - 무한 시퀀스
(comment
  (concat (take 8 (repeat "na"))
          ["Batman!"])
  ; 0 ~ 9 사이의 랜덤 정수 3개
  (take 3 (repeatedly (fn [] (rand-int 10)))))

; 직접 만든 lazy-seq 함수
(defn even-numbers
  ([] (even-numbers 0))
  ([n] (cons n (lazy-seq (even-numbers (+ n 2))))))
(comment
  (take 10 (even-numbers)))

; 103쪽 - 집합 추상화
(comment
  (empty? [])
  (empty? ["no!"]))

; 104쪽 - into
(comment
  (map identity {:sunlight-reaction "Glitter!"})
  (into {} (map identity {:sunlight-reaction "Glitter!"}))

  (map identity [:garlic :sesame-oil :fried-eggs])
  (into [] (map identity [:garlic :sesame-oil :fried-eggs]))

  (map identity [:garlic-clove :garlic-clove])
  (into #{} (map identity [:garlic-clove :garlic-clove])))

; 105쪽 - conj
(conj [0] [1]) ;; => [0 [1]]
(into [0] [1]) ;; => [0 1]
(conj [0] 1) ;; => [0 1]
(conj [0] 1 2 3 4) ;; => [0 1 2 3 4]
(conj {:time "midnight"} [:place "ye olde cemetarium"]) ;; => {:time "midnight", :place "ye olde cemetarium"}

; conj와 into는 매우 닮았다.
(defn my-conj
  [target & additions]
  (into target additions))
(my-conj [0] 1 2 3) ;; => [0 1 2 3]

; 106쪽 - apply
(max 0 1 2)
(apply max [0 1 2])

(defn my-into
  [target additions]
  (apply conj target additions))

; 107쪽 - partial
(def add10 (partial + 10))
(add10 3) ;; => 13
(add10 5) ;; => 15

(def add-missing-elements
  (partial conj ["water" "earth" "air"]))
(add-missing-elements "unobtainium" "adamantium") ;; => ["water" "earth" "air" "unobtainium" "adamantium"]

(defn my-partial
  [partialized-fn & args]
  (fn [& more-args]
    (apply partialized-fn (into args more-args))))
(def add20 (my-partial + 20))
(add20 3) ;; => 23

(defn lousy-logger
  [log-level message]
  (condp = log-level
    :warn (str/lower-case message)
    :emergency (str/upper-case message)))
(def warn (partial lousy-logger :warn))
(warn "Red light ahead") ;; => "red light ahead"

; 109쪽 - complement
(def not-vampire? (complement vampire?))
(defn identify-humans
  [social-security-numbers]
  (filter not-vampire?
          (map vampire-related-details
               social-security-numbers)))

(defn my-complement
  [fun]
  (fn [& args]
    (not (apply fun args))))
(def my-pos? (complement neg?))
(my-pos? 1) ;; => true
(my-pos? -1) ;; => false


