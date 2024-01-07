(ns aoc2023.problem-8-1
  (:require [clojure.java.io :as io]
            [clojure.string :as s]))

(comment
  " https://adventofcode.com/2023/day/8
  낙타를 타고 황무지를 빠져나가야 한다.

  네트워크 구조의 지도가 주어져 있다.
  현재 위치는 AAA 이고, 목적지는 ZZZ 이다.
  왼쪽 또는 오른쪽 방향을 선택해 이동할 수 있다.

  (예시)
  RL

  AAA = (BBB, CCC)   => AAA 의 왼쪽은 BBB, 오른쪽은 CCC와 연결되어 있다.
  BBB = (DDD, EEE)
  CCC = (ZZZ, GGG)
  DDD = (DDD, DDD)
  EEE = (EEE, EEE)
  GGG = (GGG, GGG)
  ZZZ = (ZZZ, ZZZ)

  RL 은 AAA 에서 오른쪽(CCC)으로 간 다음, CCC에서 왼쪽(ZZZ)으로 간다는 의미이다.
  이렇게 2단계로 목적지에 도달할 수 있다.

  ZZZ를 바로 찾지 못할 수도 있다. 이런 경우엔 RL 을 반복하도록 한다.
  다음은 반복을 통해 6단계로 목적지에 도달하는 예시이다.

  (예시)
  LLR

  AAA = (BBB, BBB)
  BBB = (AAA, ZZZ)
  ZZZ = (ZZZ, ZZZ)

  LLR을 두 번 반복해 ZZZ로 도착한다: AAA, L(BBB), L(AAA), R(BBB), L(AAA), L(BBB), R(ZZZ)

  주어진 입력의 지시를 사용하면 몇 단계를 거쳐야 ZZZ에 도착할 수 있는지 계산하라.
  ")

(def data-file (-> "aoc2023/input8.txt" io/resource slurp))
(def input-lines (->> data-file
                      s/split-lines))

(defn input-lines->context
  [input-lines]
  (let [instructions (first input-lines)
        nodes-raw (rest (rest input-lines))
        nodes (->> nodes-raw
                   (map #(re-seq #"[A-Z]+" %))
                   (map (fn [[node left right]]
                          [node {\L left \R right}]
                          ))
                   (into {}))]
    {:instructions instructions
     :nodes        nodes}))

(defn solve
  [input-lines]
  (let [{:keys [instructions nodes]} (input-lines->context input-lines)]
    (loop [steps 0
           node "AAA"
           instrs (cycle instructions)]
      (let [curr-instr (first instrs)
            next-node (get-in nodes [node curr-instr])]
        (if
          (= node "ZZZ") steps
          (recur (inc steps) next-node (rest instrs)))))))

(comment
  (input-lines->context input-lines)
  (solve input-lines) ; => 19951
  ;;
  )
