(ns brave-clojure.chapter10
  "https://www.braveclojure.com/zombie-metaphysics/"
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

; 246쪽 - atom
(def fred (atom {:cuddle-hunger-level  0
                 :percent-deteriorated 0}))

"역참조를 사용해 fred의 현재 상태를 얻는다."
@fred ;; => {:cuddle-hunger-level 0, :percent-deteriorated 0}

(let [zombie-state @fred]
  (when (>= (:percent-deteriorated zombie-state) 50)
    (future (println (:percent-deteriorated zombie-state)))))

(swap! fred
       (fn [current-state]
         (merge-with + current-state {:cuddle-hunger-level 1})))

@fred
; {:cuddle-hunger-level 1, :percent-deteriorated 0}

(swap! fred
       (fn [current-state]
         (merge-with + current-state {:cuddle-hunger-level  1
                                      :percent-deteriorated 1})))

@fred
; {:cuddle-hunger-level 2, :percent-deteriorated 1}

(defn increase-cuddle-hunger-level
  "좀비 상태와 달라붙는 레벨을 지정하는 함수"
  [zombie-state increase-by]
  (merge-with + zombie-state {:cuddle-hunger-level increase-by}))

(increase-cuddle-hunger-level @fred 10)
; {:cuddle-hunger-level 12, :percent-deteriorated 1}

"위의 함수 호출은 fred의 값을 실제로 바꾸지는 않았다."
@fred
; {:cuddle-hunger-level 2, :percent-deteriorated 1}

(swap! fred increase-cuddle-hunger-level 10)
; {:cuddle-hunger-level 12, :percent-deteriorated 1}

"swap!을 호출했으므로 fred의 값은 되었다."
@fred
; {:cuddle-hunger-level 12, :percent-deteriorated 1}

(comment
  "update-in 함수 연습"
  (update-in {:a {:b 3}} [:a :b] inc) ;; => {:a {:b 4}}
  (update-in {:a {:b 3}} [:a :b] + 10)) ;; => {:a {:b 13}}

"update-in을 사용해 fred를 업데이트"
(swap! fred update-in [:cuddle-hunger-level] + 10)
; {:cuddle-hunger-level 22, :percent-deteriorated 1}

@fred
; {:cuddle-hunger-level 22, :percent-deteriorated 1}

; 249쪽
"원자를 쓰면 과거 상태를 유지할 수 있다."
(let [num (atom 1)
      s1 @num]
  (swap! num inc)
  (println "State 1:" s1)
  (println "Current state:" @num))
; State 1: 1
; Current state: 2

"만약 두 개의 스레드가 swap! 을 호출하면 어떻게 될까?
swap!은 atom 업데이트가 동기적으로 발생한다(스레드를 블록한다)"

"만약 현재 상태를 검사하  않고 업데이트를 강제로 하려 한다면 reset! 을 쓴다"
(reset! fred {:cuddle-hunger-level  0
              :percent-deteriorated 0})
; {:cuddle-hunger-level 0, :percent-deteriorated 0}

; 251쪽 - watch 와 validators
"watch는 참조 유형의 모든 움직임을 검토한다.
validators는 어떤 상태가 허용되는지 엄격하게 통제한다."

(defn shuffle-speed
  "좀비가 발을 질질 끌며 걷는 속도(SPH)를 리턴합니다."
  [zombie]
  (* (:cuddle-hunger-level zombie)
     (- 100 (:percent-deteriorated zombie))))

"다음 코드는 SPH가 위험 수준인 5000 SPH 이상이면 경고를 출력한다."

(defn shuffle-alert
  "관찰 함수
  key - 보고할 때 쓰는 키 값
  watched - 관찰하는 atom
  old-state - 업데이트 되기 전의 atom 상태
  new-state - 업데이트 된 이후의 atom 상태"
  [key watched old-state new-state]
  (let [sph (shuffle-speed new-state)]
    (if (> sph 5000)
      (do
        (println "Run, you fool!")
        (println "The zombie's SPH is now " sph)
        (println "This message brought to your courtesy of " key))
      (do
        (println "All's well with " key)
        (println "Cuddle hunger: " (:cuddle-hunger-level new-state))
        (println "Percent deteriorated: " (:percent-deteriorated new-state))
        (println "SPH: " sph)))))

(reset! fred {:cuddle-hunger-level  22
              :percent-deteriorated 2})
(add-watch fred :fred-shuffle-alert shuffle-alert)
(swap! fred update-in [:percent-deteriorated] + 1)
; All's well with  :fred-shuffle-alert
; Cuddle hunger:  22
; Percent deteriorated:  3
; SPH:  2266
; {:cuddle-hunger-level 22, :percent-deteriorated 3}

(swap! fred update-in [:cuddle-hunger-level] + 30)
; Run, you fool!
; The zombie's SPH is now  5356
; This message brought to your courtesy of  :fred-shuffle-alert
; {:cuddle-hunger-level 52, :percent-deteriorated 3}

(defn percent-deteriorated-validator
  [{:keys [percent-deteriorated]}]
  (and (>= percent-deteriorated 0)
       (<= percent-deteriorated 100)))

"percent-deteriorated-validator가 false를 리턴해서 원자 업데이트는 실패하는 예제"
(def bobby
  (atom
   {:cuddle-hunger-level  0
    :percent-deteriorated 0}
   :validator percent-deteriorated-validator))
(swap! bobby update-in [:percent-deteriorated] + 200)
; Invalid reference state


"에러 메시지 내용을 얻기 위해서 고의로 예외를 발생시킴"
(defn percent-deteriorated-validator
  [{:keys [percent-deteriorated]}]
  (or (and (>= percent-deteriorated 0)
           (<= percent-deteriorated 100))
      (throw (IllegalStateException. "That's not mathy!"))))
(def bobby2
  (atom
   {:cuddle-hunger-level 0
    :percent-deteriorated 0}
   :validator percent-deteriorated-validator))
(swap! bobby2 update-in [:percent-deteriorated] + 200)
; That's not mathy!

; 253 - refs
(def sock-varieties
  #{"darned" "argyle" "wool" "horsehair" "mulleted"
    "passive-aggressive" "striped" "polka-dotted"
    "athletic" "business" "power" "invisible" "gollumed"})

(defn sock-count
  [sock-variety count]
  {:variety sock-variety
   :count   count})

(defn generate-sock-gnome
  "Create an initial sock gnome state with no socks"
  [name]
  {:name  name
   :socks #{}})

(def sock-gnome
  (ref (generate-sock-gnome "Barumpharumph")))
(def dryer
  (ref {:name "LG 1337"
        :socks (set (map #(sock-count % 2) sock-varieties))}))

(defn steal-sock
  [gnome dryer]
  (dosync
   (when-let [pair (some #(when (= (:count %) 2)
                            %)
                         (:socks @dryer))]
     (let [updated-count (sock-count (:variety pair) 1)]
       (alter gnome update-in [:socks] conj updated-count)
       (alter dryer update-in [:socks] disj pair)
       (alter dryer update-in [:socks] conj updated-count)))))
(steal-sock sock-gnome dryer)
; {:name "LG 1337",
;  :socks
;  #{{:variety "striped", :count 2}
;    {:variety "wool", :count 2}
;    {:variety "passive-aggressive", :count 2}
;    {:variety "argyle", :count 2}
;    {:variety "business", :count 2}
;    {:variety "darned", :count 2}
;    {:variety "polka-dotted", :count 2}
;    {:variety "horsehair", :count 2}
;    {:variety "power", :count 2}
;    {:variety "athletic", :count 2}
;    {:variety "gollumed", :count 1}  <-----
;    {:variety "mulleted", :count 2}
;    {:variety "invisible", :count 2}}}

(:socks @sock-gnome)
; #{{:variety "gollumed", :count 1}}

(defn similar-socks
  [target-sock sock-set]
  (filter #(= (:variety %)
              (:variety target-sock))
          sock-set))
(similar-socks (first (:socks @sock-gnome))
               (:socks @dryer))
; ({:variety "gollumed", :count 1})

(do
  (def counter (ref 0))
  (future
    (dosync
     (alter counter inc)
     (println @counter)   ; 1
     (Thread/sleep 500)
     (alter counter inc)
     (println @counter))) ; 2
  (Thread/sleep 250)
  (println @counter))     ; 0
; 1
; 0
; 2

; 257쪽 - 교환 ( commute )
"commute는 alter와 마찬가지로 transaction 중에 참조 상태를 업데이트한다.
그렇지만 반영하는 시점의 행동은 완전히 다르다.

alter - 현재 상태와 transaction 상태를 비교해서,
  둘이 다르면 transaction을 재시도.
  둘이 같으면 변경된 참조 상태 반영.

commute - 현재 상태를 기반으로 commute 함수를 실행,
  실행 결과를 반영한다.

commute는 transaction을 강제로 재시도하지 않으므로 성능은 더 빠르지만 위험성이 좀 있다."

; 안전한 사용 예
(defn sleep-print-update
  [sleep-time thread-name update-fn]
  (fn [state]
    (Thread/sleep sleep-time)
    (println (str thread-name ": " state))
    (update-fn state)))

(do
  #_:clj-kondo/ignore
  (def counter (ref 0))
  (future
    (dosync
     (commute counter (sleep-print-update 100 "Thread A" inc))))
  (future
    (dosync
     (commute counter (sleep-print-update 150 "Thread B" inc)))))
; Thread A: 0
; Thread B: 0
; Thread A: 0
; Thread B: 1

; 안전하지 않은 예
(do
  (def receiver-a (ref #{}))
  (def receiver-b (ref #{}))
  (def giver (ref #{1}))
  (do
    (future (dosync (let [gift (first @giver)]
                      (Thread/sleep 10)
                      (commute receiver-a conj gift)
                      (commute giver disj gift))))
    (future (dosync (let [gift (first @giver)]
                      (Thread/sleep 50)
                      (commute receiver-b conj gift)
                      (commute giver disj gift))))))

"commute는 빠르지만 신중하게 사용해야 한다.
giver가 선물을 하나 갖고 있었는데, a 와 b 둘 다 선물을 하나씩 받았다.
giver가 선물을 두 개 준 결과가 되었다."
@receiver-a ;; => #{1}
@receiver-b ;; => #{1}
@giver ;; => #{}

; 260쪽 - vars
"동적 변수"
; ^:dynamic 으로 동적 변수라고 표시하고..
; * * 로 귀마개(earmuffs)를 씌운다. Clojure에서는 동적 변수 이름을 귀마개로 감싸야 한다.
(def ^:dynamic *notification-address* "dobby@elf.org")

"일반적인 변수와 달리 동적 변수는 binding 을 써서 일시적으로 스코프 내에서 값을 바꿀 수 있다."
(binding [*notification-address* "test@elf.org"]
  *notification-address*)
; "test@elf.org"

"let처럼 binding을 쌓을 수도 있다."
(binding [*notification-address* "tester-1@elf.org"]
  (println *notification-address*)
  (binding [*notification-address* "tester-2@elf.org"]
    (println *notification-address*))
  (println *notification-address*))
; tester-1@elf.org
; tester-2@elf.org
; tester-1@elf.org

(defn notify
  [message]
  (str "TO: " *notification-address* "\n"
       "MESSAGE: " message))

(notify "I fell.")
; "TO: dobby@elf.org\nMESSAGE: I fell."

; 이 함수를 테스트할 때마다 dobby에게 스팸 메일을 보내고 싶지 않다면 binding을 쓰는 방법이 있다.
(binding [*notification-address* "test@elf.org"]
  (notify "test!"))
; "TO: test@elf.org\nMESSAGE: test!"

; 표준 출력 *out* 을 사용해 파일에 문장을 출력하는 방법
(binding [*out* (io/writer "print-output")]
  (println "A man who carries a cat by the tail learns
something he can learn in no other way.
-- Mark Twain"))
(slurp "print-output")

; 동적 변수는 설정할 때도 쓰인다.
"*print-length*는 출력하는 아이템의 수를 지정한다"
(println ["Print" "all" "the" "things!"])
; [Print all the things!]
(binding [*print-length* 1]
  (println ["Print" "just" "one!"]))
; [Print ...]

"set! 의 사용"
(def ^:dynamic *troll-thought* nil)
(defn troll-riddle
  [your-answer]
  (let [number "man meat"]
    ; 변수가 bound를 갖는다면 troll의 생각을 set! 으로 설정한다.
    (when (thread-bound? #'*troll-thought*)
      (set! *troll-thought* number))
    (if (= number your-answer)
      "TROLL: You can cross the bridge!"
      "TROLL: Time to eat you, succulent human!")))

(binding [*troll-thought* nil]
  (println (troll-riddle 2))
  (println "SUCCULENT HUMAN: Oooooh! The answer was " *troll-thought*))
; TROLL: Time to eat you, succulent human!
; SUCCULENT HUMAN: Oooooh! The answer was  man meat

(.write *out* "prints to repl")
; 출력: prints to repl

(.start (Thread. #(.write *out* "prints to standard out")))
; 출력 없음. *out*이 REPL 프린터에 걸리지 않기 때문.

(let [out *out*]
  (.start
   (Thread. #(binding [*out* out]
               (.write *out* "prints to repl from thread")))))
; 출력: "prints to repl from thread"

(.start (Thread. (bound-fn [] (.write *out* "prints to repl from thread"))))
; 출력: "prints to repl from thread"

; 265쪽
"변수 루트 바꾸기"

"hair 가 power-source의 루트 값."
(def power-source "hair")

"alter-var-root를 쓰면 루트 값을 영구히 바꾼다."
(alter-var-root #'power-source (fn [_] "7-eleven parking lot"))
power-source

"with-redefs로 변수의 루트를 잠시 바꿀 수 있다."
(with-redefs [*out* *out*]
  (doto (Thread. #(println "with redefs allows me to show up in the REPL"))
    .start
    .join))

; 266쪽 - 상태없는 동시성과 병렬성
"클로저는 pmap으로 쉽게 병렬 map을 구사할 수 있게 한다."

(defn always-1 [] 1)
(take 5 (repeatedly always-1))
; (1 1 1 1 1)

"0~9 사이의 난수로 lazy seq 만들기"
(take 5 (repeatedly (partial rand-int 10)))

(def alphabet-length 26)

;; A-Z 알파벳 문자들의 벡터
(def letters (mapv (comp str char (partial + 65))
                   (range alphabet-length)))
(defn random-string
  "Returns a random string of specified length"
  [length]
  (apply str (take length
                   (repeatedly #(rand-nth letters)))))
(defn random-string-list
  [list-legnth string-length]
  (doall
   (take list-legnth
         (repeatedly (partial random-string string-length)))))

(def orc-names (random-string-list 3000 7000))

(time (dorun (map str/lower-case orc-names)))

; 병렬로 실행. 병렬이 순차실행보다 더 느릴 수도 있다.
(time (dorun (pmap str/lower-case orc-names)))

(def orc-name-abbrevs (random-string-list 20000 300))

(time (dorun (map str/lower-case orc-name-abbrevs)))
; "Elapsed time: 24.146959 msecs"

(time (dorun (pmap str/lower-case orc-name-abbrevs)))
; "Elapsed time: 99.466625 msecs"

(def numbers [1 2 3 4 5 6 7 8 9 10])
(partition-all 3 numbers)
; ((1 2 3) (4 5 6) (7 8 9) (10))

(pmap inc numbers)
; (2 3 4 5 6 7 8 9 10 11)

(pmap (fn [number-group] (doall (map inc number-group)))
      (partition-all 3 numbers))
; ((2 3 4) (5 6 7) (8 9 10) (11))

(apply concat
       (pmap (fn [number-group]
               (doall (map inc number-group)))
             (partition-all 3 numbers)))
; (2 3 4 5 6 7 8 9 10 11)

(time
 (dorun
  (apply concat
         (pmap (fn [name]
                 (doall (map str/lower-case name)))
               (partition-all 1000 orc-name-abbrevs)))))
; "Elapsed time: 54.9525 msecs"

(defn ppmap
  [grain-size f & colls]
  (apply concat
         (apply pmap
                (fn [& pgroups] (doall (apply map f pgroups)))
                (map (partial partition-all grain-size) colls))))
(time (dorun
       (ppmap 1000 str/lower-case orc-name-abbrevs)))
; "Elapsed time: 39.268542 msecs"
