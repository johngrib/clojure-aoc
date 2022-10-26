(ns aoc2020.problem-8-1-with-iterate
  (:require [aoc2020.problem-8-1 :refer [operation-functions strings->codes input-strings sample-input-strings]]))

(comment "
https://adventofcode.com/2020/day/8  part 1

중복 명령이 실행되기 직전의 acc 값을 출력하세요.
")

(defn execute-code
  "주어진 context를 읽고 상태를 변화시킨 다음 context를 리턴합니다."
  [context]
  (let [{:keys [operations
                accumulator
                pointer
                executed
                execute-log]} context

        실행할-명령 (nth operations pointer)

        {명령-id      :id
         명령-command :op
         인자-number  :arg-number} 실행할-명령

        실행할-함수 (get operation-functions 명령-command)

        {result-jump :jump
         갱신된-누산기     :accumulator} (실행할-함수 인자-number accumulator)

        갱신된-프로그램-카운터 (+ pointer result-jump)]
    {:operations  operations
     :executed    (conj executed 명령-id)
     :execute-log (conj execute-log 실행할-명령)
     :accumulator 갱신된-누산기
     :pointer     갱신된-프로그램-카운터}))

(defn until-error?
  "주어진 컨텍스트를 읽고 에러가 없다면 true를 리턴합니다."
  [{next-pointer :pointer
    operations   :operations
    executed     :executed}]
  (cond
    (>= next-pointer (count operations))
    (println "프로그램 마지막에 도달하였습니다.")

    (contains? executed (:id (nth operations next-pointer)))
    (println "중복 실행으로 인해 프로그램 실행을 중단합니다.")

    :else
    true))

(defn solve-8-1-with-iterate
  "https://adventofcode.com/2020/day/8 part1 문제를 풀이해 결과를 리턴합니다."
  [input-string]
  (let [context {:operations  (strings->codes input-string)
                 :accumulator 0
                 :pointer     0
                 :executed    #{}
                 :execute-log []}]
    (last
     (take-while until-error?
                 (iterate execute-code context)))))

(comment
  (solve-8-1-with-iterate sample-input-strings)               ;  5
  (solve-8-1-with-iterate input-strings)                      ; 2025
  ;;
  )
