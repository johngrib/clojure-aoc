(ns aoc2018.problem-5-2
  (:require [aoc2018.problem-5-1 :refer [react-polymer-reducer sample-input-string input-string]]))

(comment "
https://adventofcode.com/2018/day/5 part2
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

특정 문자 쌍(a와 A 처럼)을 입력에서 제거한 다음 반응시켰을 때 생성가능한 가장 짧은 문자열의 길이가 part2의 정답이 된다.
")

(defn solve-5-2
  "https://adventofcode.com/2018/day/5 문제의 답을 풀이한 결과를 리턴합니다.
  주어진 문자열을 polymer units list로 인식하여, 연쇄 반응을 시킨 결과를 리턴합니다."
  [polymer-string]
  (let [입력-문자리스트 (lazy-seq polymer-string)
        무시할-대소문자pair-리스트 (->> (range 65 91)
                              (map (fn [num] #{(char num) (char (+ num 32))})))
        반응함수-리스트 (map #(react-polymer-reducer %) 무시할-대소문자pair-리스트)
        결과-문자리스트 (->> 반응함수-리스트
                      (map #(reduce % [] 입력-문자리스트))
                      (sort-by count))
        정답-후보-문자리스트 (first 결과-문자리스트)]
    {:size   (count 정답-후보-문자리스트)
     :string (apply str 정답-후보-문자리스트)}))

(comment
  (solve-5-2 sample-input-string)                           ; 4
  (solve-5-2 input-string)                                  ; 5524
  ;;
  )

