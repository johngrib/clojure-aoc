(ns aoc2020.problem-8-1
  (:require [clojure.java.io :as io]))

(comment "
https://adventofcode.com/2020/day/8  part 1

중복 명령이 실행되기 직전의 acc 값을 출력하세요.
")

(defn file->string [file-address]
  (-> file-address
      io/resource
      slurp))

(def input-strings (file->string "aoc2020/input8.txt"))
(def sample-input-strings (file->string "aoc2020/input8-sample.txt"))

(def operation-functions
  "각 어셈블리 명령을 처리하는 함수를 정의합니다."
  {:nop (fn [_ accumulator]
          {:jump        1
           :accumulator accumulator})

   :acc (fn [arg-number accumulator]
          {:jump        1
           :accumulator (+ accumulator arg-number)})

   :jmp (fn [arg-number accumulator]
          {:jump        arg-number
           :accumulator accumulator})})

(defn strings->codes
  "주어진 문자열을 코드 리스트로 변환해 리턴합니다."
  [strings]
  (->> strings
       ;         op     arg-number
       (re-seq #"(\S+) +([\+\-]\d+)")
       (map-indexed (fn [index [_ operation arg-number]]
                      (let [op (keyword operation)
                            number (Integer/parseInt arg-number)]
                        {:id         index
                         :op         op
                         :arg-number number})))))

(defn solve-8-1
  "https://adventofcode.com/2020/day/8 part1 문제를 풀이해 결과를 리턴합니다."
  [input-string]
  (loop [명령-list (strings->codes input-string)
         누산기 0
         프로그램-카운터 0
         실행완료된-명령-집합 #{}
         실행-log []]
    (let [
          실행할-명령 (nth 명령-list 프로그램-카운터)

          {명령-id      :id
           명령-command :op
           인자-number  :arg-number} 실행할-명령

          실행할-함수 (get operation-functions 명령-command)

          {result-jump :jump
           갱신된-누산기     :accumulator} (실행할-함수 인자-number 누산기)

          갱신된-프로그램-카운터 (+ 프로그램-카운터 result-jump)
          갱신된-실행-log (conj 실행-log 실행할-명령)]

      (cond
        (>= 갱신된-프로그램-카운터 (count 명령-list))
        {:execute-log    갱신된-실행-log
         :execute-count  (count 갱신된-실행-log)
         :cause          "프로그램 마지막에 도달하여 실행 완료"
         :accumulator    갱신된-누산기
         :last-operation 실행할-명령}

        (contains? 실행완료된-명령-집합 명령-id)
        {:execute-log    갱신된-실행-log
         :execute-count  (count 갱신된-실행-log)
         :cause          "중복 실행으로 인한 중단"
         :accumulator    누산기
         :last-operation 실행할-명령}

        :else
        (recur 명령-list
               갱신된-누산기
               갱신된-프로그램-카운터
               (conj 실행완료된-명령-집합 명령-id)
               갱신된-실행-log)))))

(solve-8-1 input-strings)                                   ; 2025
(solve-8-1 sample-input-strings)                            ;  5
