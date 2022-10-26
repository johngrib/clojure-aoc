(ns aoc2020.problem-8-2
  (:require [aoc2020.problem-8-1 :refer [strings->codes run-operation-list sample-input-strings input-strings]]
            [clojure.java.io :as io]
            [clojure.string :as str]))

(comment "
https://adventofcode.com/2020/day/8  part 2

입력 명령들 중 딱 하나의 명령이 잘못된 명령입니다.
그 명령은 jmp 또는 nop 이며, jmp를 nop로 수정하거나 nop를 jmp로 수정하면 프로그램이 끝까지 실행될 수 있습니다.
명령 하나를 수정해서 프로그램이 끝까지 실행되었을 시점의 누산기의 값이 정답입니다.
")

(defn swaper
  "swap 매핑 데이터를 받아, 명령 리스트에서 특정 명령의 op code를 스왑해주는 함수를 생성해 리턴합니다.
  생성된 함수는
  명령 리스트를 입력받아, 하나의 op code를 변경합니다."
  [swap-dictionary]
  (fn [operation-id operation-list]
    (let [head (take operation-id operation-list)
          target (nth operation-list operation-id)
          target-op (:op target)
          to-op (get swap-dictionary target-op)
          result (merge target {:op to-op})
          tail (drop (inc operation-id) operation-list)]
      (concat head [result] tail))))

(comment
  ((swaper {:jmp :nop, :nop :jmp}) 0
   [{:id 0, :op :nop, :arg-number 0}])

  ((swaper {:jmp :nop, :nop :jmp}) 1
   [{:id 0, :op :nop, :arg-number 0}
    {:id 1, :op :nop, :arg-number 0}]))

(defn solve-8-2
  [input-string]
  (let [명령-list (strings->codes input-string)
        명령교체-후보-list (->> 명령-list
                          (filter #((:op %) #{:nop :jmp}))
                          (map :id))
        swap-함수 (swaper {:nop :jmp, :jmp :nop})]

    (loop [swap-candidates 명령교체-후보-list]

      (if (empty? swap-candidates)
        {:result :error
         :cause  "모든 교체 후보를 검토했지만 정답을 찾지 못했습니다."}
        ;; else
        (let [swap-대상 (first swap-candidates)
              swap된-명령-list (swap-함수 swap-대상 명령-list)
              실행결과 (run-operation-list swap된-명령-list)]
          (if (= :error (:result 실행결과))
            (recur (rest swap-candidates))
            (merge 실행결과 {:swap-id        swap-대상
                         :swap-operation (nth swap된-명령-list swap-대상)})))))))

(comment
  (solve-8-2 sample-input-strings)                            ; 8
  (solve-8-2 input-strings)                                   ; 2001
  ;;
  )
