(ns aoc2018.problem-7-1
  (:require [clojure.java.io :as io]
            [clojure.set :as cset]
            ))

(comment "
https://adventofcode.com/2018/day/7 문제 part1

퀘스트 진행 순서 정렬 문제. 문제 입력은 다음과 같다.

Step C must be finished before step A can begin.
Step C must be finished before step F can begin.
Step A must be finished before step B can begin.
Step A must be finished before step D can begin.
Step B must be finished before step E can begin.
Step D must be finished before step E can begin.
Step F must be finished before step E can begin.

이 입력은 다음과 같은 뜻을 갖는다.

C 를 마쳐야 A를 할 수 있다.
C 를 마쳐야 F를 할 수 있다.
...

아래는 이 입력의 작업 의존도를 표현하는 그림이다.

   -->A--->B--
 /    \\      \\
C      -->D----->E
 \\            /
   ---->F-----

C 부터 시작해서 E 까지 작업을 완료하는 단계를 문자열로 표현하면 된다.
같은 단계에서 할 수 있는 작업이라면 우선순위는 알파벳순으로 따지면 된다.
")

(defn connect-works
  "문제 조건을 파싱해 얻은 작업 리스트를 읽고 관계를 형성해 hashmap으로 리턴합니다.
   예 [[A B] [B C] [B D]] => {\\A #{B}, \\B #{\\C \\D}} "
  [input-list]
  (->> input-list
       (reduce (fn [acc [head tail]]
                 (assoc acc head (if (contains? acc head)
                                   (conj (get acc head) tail)
                                   #{tail})))
               {})
       (map (fn [[key value-list]] {key value-list}))
       (reduce into {})))

(defn collect-root-works
  "선행 필수 작업이 없어 가장 먼저 실행 가능한 작업들의 리스트를 리턴합니다."
  [input-works]
  (->> (cset/difference
         (->> input-works (map first) (into #{}))
         (->> input-works (map last) (into #{})))
       (into [])))

(defn string->work-context
  "문제의 입력을 읽고 작업에 필요한 컨텍스트 데이터를 생성해 리턴합니다."
  [input-string]
  (let [
        입력-list (->> input-string
                     (re-seq #"Step ([A-Z]) must .+? before step ([A-Z]) can begin.")
                     (map rest)
                     (map #(seq (apply str %))))
        가장-먼저-실행할-수-있는-작업들 (collect-root-works 입력-list)
        다음-작업-tree (connect-works 입력-list)
        선행-필수-작업-tree (connect-works (map reverse 입력-list))]
    {:root-works     가장-먼저-실행할-수-있는-작업들
     :required-works 선행-필수-작업-tree
     :next-works     다음-작업-tree
     :all-works      (->> 입력-list
                          (reduce into #{}))}))



(defn next-available-works
  "조건을 파악해 잔여 작업들 중 다음 작업을 하나 선택해 리턴합니다.
    remain-work-list : 잔여 작업 목록
    finished-works : 완료된 작업들
    required-works : 각 작업별 필수 선행 작업이 담긴 데이터"
  [remain-work-list finished-works required-work-data]
  (->> remain-work-list
       (filter (fn [x] (and
                         ; 이 작업이 이미 끝난 작업이면 안된다.
                         (not (finished-works x))
                         ; 이 작업의 선행 필수 작업들이 이미 끝나있어야 한다.
                         (empty? (cset/difference (required-work-data x) finished-works)))))
       sort))

(comment
  (next-available-works [\C]                                ; remain work
                        #{}                                 ; finished work
                        {\A #{\C}, \F #{\C}, \B #{\A}, \D #{\A}, \E #{\B \D \F}}) ; required-works
  (next-available-works [\A \F]
                        #{\C}
                        {\A #{\C}, \F #{\C}, \B #{\A}, \D #{\A}, \E #{\B \D \F}})
  (next-available-works [\B \D \F]
                        #{\C \A}
                        {\A #{\C}, \F #{\C}, \B #{\A}, \D #{\A}, \E #{\B \D \F}})
  (next-available-works [\D \F \E]
                        #{\C \A \B}
                        {\A #{\C}, \F #{\C}, \B #{\A}, \D #{\A}, \E #{\B \D \F}})
  (next-available-works [\F \E]
                        #{\C \A \B \D}
                        {\A #{\C}, \F #{\C}, \B #{\A}, \D #{\A}, \E #{\B \D \F}})
  (next-available-works [\E]
                        #{\C \A \B \D \F}
                        {\A #{\C}, \F #{\C}, \B #{\A}, \D #{\A}, \E #{\B \D \F}}))

(defn find-work-route
  "올바른 작업 순서를 리턴합니다.
   재귀를 사용합니다.
    remain-work-list : 잔여 작업 목록
    work-context : 작업에 필요한 각종 데이터
    finished-set : 완료된 작업들
    route : 순서가 판명된 작업 리스트"
  [remain-work-list work-context finished-set route]
  (if (empty? remain-work-list)
    route
    (let [
          다음에-가능한-작업-list (next-available-works
                            remain-work-list
                            finished-set
                            (:required-works work-context))
          다음-작업 (first 다음에-가능한-작업-list)
          result-route (conj route 다음-작업)
          끝난-작업 (conj finished-set 다음-작업)
          다음-작업-후보 (into (rest 다음에-가능한-작업-list)
                         (get (:next-works work-context) 다음-작업))]

      (recur 다음-작업-후보 work-context 끝난-작업 result-route))))

(defn solve-7-1
  "https://adventofcode.com/2018/day/7 문제의 정답을 구해 리턴합니다.
  주어진 문자열을 읽고, 올바른 작업순서를 생성해 문자열 형태로 리턴합니다."
  [input-string]
  (let [작업-context (string->work-context input-string)
        결과-작업순서 (find-work-route (:root-works 작업-context) 작업-context #{} [])]
    (apply str 결과-작업순서)))

(def sample-input-string (-> "aoc2018/input7-sample.txt" io/resource slurp))
(def input-string (-> "aoc2018/input7.txt" io/resource slurp))

(comment
  (solve-7-1 sample-input-string)                           ; CABDFE
  (solve-7-1 input-string))                                  ; GLMVWXZDKOUCEJRHFAPITSBQNY

(comment
  (string->work-context sample-input-string))

