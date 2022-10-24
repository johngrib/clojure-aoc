(ns aoc2018.problem-5-1
  (:require [clojure.java.io :as io]))

(comment "
https://adventofcode.com/2018/day/5
https://github.com/green-labs/bootcamp-aoc/blob/main/translations/aoc2018/day5.md

- 같은 문자의 소문자와 대문자는 바로 옆에 붙어있다면 서로 반응하여 사라지게 된다.
- 소문자와 소문자, 대문자와 대문자는 서로 반응하지 않는다.

예를 보자.
    vv
dabAcCaCBAcCcaDA  The first 'cC' is removed.
   vv
dabAaCBAcCcaDA    This creates 'Aa', which is removed.
      vv
dabCBAcCcaDA      Either 'cC' or 'Cc' are removed (the result is the same).

dabCBAcaDA        No further actions can be taken.

따라서 dabAcCaCBAcCcaDA 는 dabCBAcaDA로 변환된다. 따라서 예제의 답은 10 이다.

입력된 문자열이 반응을 마쳤을 때 남는 글자의 수가 정답이다.
")

(def sample-input-string "dabAcCaCBAcCcaDA")
(def input-string (-> "aoc2018/input5.txt"
                      io/resource
                      slurp))

(defn pair-alphabet?
  "주어진 두 알파벳이 대문자와 소문자 쌍이라면 true를 리턴합니다.
  예) [A a] => true, [a a] => nil, [a b] => nil"
  [char1 char2]
  (when (every? some? [char1 char2])
    (->> (- (int char1) (int char2))
         Math/abs
         (= 32))))

;; https://hypirion.com/musings/understanding-persistent-vector-pt-1
;; https://clojure.org/reference/data_structures#Lists
;; element of clojure

(defn react-polymer-reducer
  "polymer unit을 연쇄반응하는데 사용할 reducer 함수를 리턴합니다.
  stack을 사용해 문제를 풀이합니다."
  ([]
   ; polymer unit 리스트의 끝에 새로운 polymer unit을 반응시키고, 반응의 결과 리스트를 리턴합니다.
   (react-polymer-reducer #{nil}))
  ([무시할-문자-set]
   ; polymer unit 리스트의 끝에 새로운 polymer unit을 반응시키고(특정 문자들은 무시합니다), 반응의 결과 리스트를 리턴합니다.
   (fn [stack 다음-문자]
     (let [stack의-마지막-문자 (peek stack)]
       (cond
         (무시할-문자-set 다음-문자) stack
         (pair-alphabet? stack의-마지막-문자 다음-문자) (pop stack)
         :else (conj stack 다음-문자))))))

(defn solve-5-1
  "https://adventofcode.com/2018/day/5 문제의 답을 풀이한 결과를 리턴합니다.
  주어진 문자열을 polymer units list로 인식하여, 연쇄 반응을 시킨 결과를 리턴합니다."
  [polymer-string]
  (let [결과-문자-리스트 (reduce (react-polymer-reducer)
                          []
                          polymer-string)]
    (println (apply str 결과-문자-리스트))
    (count 결과-문자-리스트)))

(comment
  (solve-5-1 sample-input-string)                           ; 10
  (solve-5-1 input-string)                                  ; 9686
  ;;
  )

