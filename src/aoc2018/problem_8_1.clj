(ns aoc2018.problem-8-1
  "https://adventofcode.com/2018/day/8"
  (:require [clojure.java.io :as io]
            [clojure.string :as s]))

#_
"숫자의 리스트로 Tree 자료구조를 표현한다.

- 모든 노드는 헤더, 자식 노드들, 메타데이터들로 구성된다
- 헤더는 2개의 숫자로 이루어진다
  - 첫번째 숫자는 자식 노드의 개수
  - 두번째 숫자는 메타데이터의 개수
- 자식 노드는 없을 수도 있고, 여러 개 있을 수도 있다
- 메타데이터는 1개 이상이다

예)
2 3 0 3 10 11 12 1 1 0 1 99 2 1 1 2

위의 예제에서 다음과 같이 루트 노드 A를 구분해 인식할 수 있다.

   Header Child nodes                 Metadata
A {[2 3]  [. . . . . . . . . . . . .] [1 1 2]}

- Header가 [2 3] 이므로
  - 자식 노드는 2개 [0 3 10 11 12 1 1 0 1 99 2]
  - 메타데이터는 3개 [1 1 2]

이번엔 A의 자식 노드들을 살펴보자.

일단 앞의 두 숫자 [0 3]은 첫 번째 자식 노드의 Header 이다.

   Header Child nodes                 Metadata
A {[2 3]                              [1 1 2]}
          [0 3] 10 11 12 1 1 0 1 99 2

자식 노드 수가 0개이므로 Metadata 3개만 표시하면 B 노드가 확정된다.

   Header Child nodes                       Metadata
A {[2 3]                                    [1 1 2]}
          B {[0 3] [10 11 12]} 1 1 0 1 99 2

A의 자식 노드는 2개이므로 나머지는 C 노드가 된다.

   Header Child nodes                              Metadata
A {[2 3]                                           [1 1 2]}
          B {[0 3] [10 11 12]} C {[1 1] 0 1 99 [2]}

C는 1개의 자식 노드를 갖고 있다. 이 노드를 D 라 하자.

   Header Child nodes                                     Metadata
A {[2 3]                                                  [1 1 2]}
          B {[0 3] [10 11 12]} C {[1 1]               [2]}
                                        D {[0 1] [99]}

요약하자면 다음과 같다.

2 3 0 3 10 11 12 1 1 0 1 99 2 1 1 2
A----------------------------------
    B----------- C-----------
                     D-----

문제: 주어진 숫자열에서 모든 Mestadata의 합을 구하라."

(defn metadata-sum
  "stack에 frame을 담으며 meta data를 수집해 나간다.
   자식 노드가 있다면 새로운 frame을 만들어 push한다.
   특정 노드의 메타데이터 수집을 끝냈다면 pop을 하고, stack의 최상단 frame의 노드 카운트를 1 감소시킨다.
   이 과정을 반복하면 된다."
  [numbers]
  (loop [sum-list []
         stack [{:node-count 1 :meta-count 0}]
         input-numbers numbers]
    (cond
      ; stack에 frame이 하나도 없거나 입력 숫자가 없다면 끝. 보고서를 리턴한다.
      (or (empty? stack)
          (empty? input-numbers))
      {:metadata        sum-list
       :sum-of-metadata (reduce + sum-list)}

      ; stack 꼭대기 frame의 자식 노드 카운트가 0 이 되었다면,
      ; stack을 pop 하고 다음 frame의 자식 노드 카운트를 1 감소시킨다.
      (-> (peek stack) :node-count zero?)
      (let [{:keys [meta-count] :as frame} (peek stack)
            rest-stack (pop stack)
            next-frame (-> rest-stack
                           peek
                           (update :node-count dec))
            new-sum-list (->> input-numbers
                              (take meta-count)
                              (concat sum-list)
                              (into []))]
        (recur new-sum-list
               (conj (pop rest-stack) next-frame)
               (drop meta-count input-numbers)))

      ; node-count 가 0 이 아니면 자식 노드를 읽고, 새로 frame을 만들어 stack에 push 한다.
      :else
      (let [[node-count meta-count] (take 2 input-numbers)
            new-frame {:node-count node-count
                       :meta-count meta-count}
            new-stack (conj stack new-frame)
            rest-numbers (drop 2 input-numbers)]
        (recur sum-list
               new-stack
               rest-numbers)))))

(defn data-file->number-list
  []
  (map parse-long
       (-> "aoc2018/input8.txt"
           io/resource
           slurp
           (s/split #"\s+"))))


(comment
  (metadata-sum [2 3 0 3 10 11 12 1 1 0 1 99 2 1 1 2])
  (-> (data-file->number-list)
      metadata-sum
      :sum-of-metadata)
  ;;
 )
