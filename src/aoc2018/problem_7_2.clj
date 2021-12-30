(ns aoc2018.problem-7-2
  (:require [aoc2018.problem-7-1 :refer :all]
            [clojure.string :as str]))

(comment "
- part1 문제에서는 worker가 1개였다. 그러나 part2 부터는 여러 개의 worker가 있다고 생각하고 문제를 풀자.
- 각각의 작업은 각기 다른 수행 시간을 갖는다.
  - A: 61초, B: 62초, C: 63초, ..., Z: 86초.
- 모든 일을 처리하는 데 걸린 시간이 해답이다.")

(defn work-required-times
  "각 작업별 수행시간을 정의합니다.
  A-time: A 작업의 소요 시간. B, C, ... 작업의 소요시간은 A 작업의 소요시간을 토대로 계산됩니다.
          만약 A-time이 61이라면 {A 61, B 62, ... Z 86} 이 리턴됩니다."
  [A-time]
  (->> (range 65 91)
       (map char)
       (map-indexed (fn [idx char] {char (+ idx A-time)}))
       (reduce into {})))

(comment (work-required-times 60)                           ; 예제 풀이용
         (work-required-times 0))                           ; 본 문제 풀이용

(defn create-workers
  "워커 리스트를 생성해 리턴합니다."
  [num]
  (->> (range num)
       (map (fn [n] {:id n, :work-in-progress nil, :time-to-finish nil}))))

(comment (create-workers 3))

(defn idle-worker?
  "주어진 워커가 놀고 있는 워커라면 true를 리턴합니다."
  [worker]
  (or (nil? (:time-to-finish worker))
      (nil? (:work-in-progress worker))))

(comment (idle-worker? {})                                  ; true
         (idle-worker? {:work-in-progress nil})             ; true
         (idle-worker? {:time-to-finish 3})                 ; true
         (idle-worker? {:work-in-progress \A})              ; false
         (idle-worker? {:time-to-finish 3, :work-in-progress \A})) ; false

(defn finished-worker?
  "주어진 워커가 작업이 완료된 워커라면 true를 리턴합니다."
  [worker timestamp]
  (and (some? (:work-in-progress worker))
       (<= (:time-to-finish worker) timestamp)))

(comment (finished-worker? {:work-in-progress \A :time-to-finish 30} 30) ; true ; 30에 완료되는데, 현재 시간이 30.
         (finished-worker? {:work-in-progress \A :time-to-finish 30} 29) ; false ; 30에 완료되는데, 현재 시간은 29.
         (finished-worker? {:work-in-progress nil :time-to-finish 30} 29)) ; false ; 30에 완료되는데, 현재 시간은 29.

(defn collect-finished-works
  "워커 목록을 받아서, 완료된 작업들을 리턴합니다."
  [timestamp workers]
  (->> workers
       (filter #(finished-worker? % timestamp))
       (map #(:work-in-progress %))
       sort))

(comment (collect-finished-works 30 [{:work-in-progress \A, :time-to-finish 30},
                                     {:work-in-progress \B, :time-to-finish 31}]) ; A
         (collect-finished-works 30 [{:work-in-progress \A, :time-to-finish 31}
                                     {:work-in-progress \C, :time-to-finish 28}
                                     {:work-in-progress \B, :time-to-finish 29}])) ; B, C

(defn reset-finished-workers
  "워커 목록을 받아, 작업이 끝난 워커들을 리셋한 목록을 리턴합니다."
  [timestamp workers]
  (->> workers
       (map (fn [worker]
              (if (finished-worker? worker timestamp)
                (merge worker {:work-in-progress nil, :time-to-finish nil})
                worker)))))

(comment (reset-finished-workers 30 [{:id 0, :work-in-progress \A, :time-to-finish 30},
                                     {:id 1, :work-in-progress \B, :time-to-finish 31}])
         (reset-finished-workers 30 [{:id 2, :work-in-progress \A, :time-to-finish 31}
                                     {:id 3, :work-in-progress \B, :time-to-finish 29}
                                     {:id 4, :work-in-progress \C, :time-to-finish 28}]))

(defn find-available-work
  "시작할 수 있는 작업들의 리스트를 리턴합니다.
  all-works: 문제에서 정의된 모든 작업
  required-works: 각 작업별 선행 필수 작업 map
  finished-works: 현재 완료된 작업 set"
  [all-works required-works finished-works]
  (->> all-works
       (filter #(not (finished-works %)))                   ; 끝난 작업을 제외한다.
       (map (fn [work] {work (get required-works work)}))
       (reduce into {})
       (filter (fn [[_ required]]
                 (clojure.set/subset? required finished-works))) ; 선행 작업이 완료된 작업들만 선별한다.
       keys
       sort))

(comment
  " -->A--->B--
  /    \\      \\
  C      -->D----->E
  \\            /
    ---->F-----")

(comment (find-available-work #{\A \B \C \D \E \F}
                              {\A #{\C}, \F #{\C}, \B #{\A}, \D #{\A}, \E #{\B \D \F}}
                              #{\C})
         (find-available-work #{\A \B \C \D \E \F}
                              {\A #{\C}, \F #{\C}, \B #{\A}, \D #{\A}, \E #{\B \D \F}}
                              #{\C \A \B}))

(defn assign-work
  "작업 1개를 워커 집단에 할당하고, 워커 집단을 리턴합니다.
   어느 워커가 할당받을지는 알 수 없습니다."
  [workers work time-required timestamp]
  (let [놀고-있는-workers (->> workers
                           (filter (fn [worker]
                                     (nil? (:time-to-finish worker))))
                           first)
        일을-받은-worker (merge 놀고-있는-workers
                            {:work-in-progress work,
                             :time-to-finish   (+ time-required timestamp)})
        작업중인-workers (->> workers
                          (filter #(not= 놀고-있는-workers %)))]
    ;; 정렬이 필요한 것은 아니지만, 로그를 보기 편하게 하기 위해 정렬한다.
    (sort-by :id < (conj 작업중인-workers 일을-받은-worker))))

(comment (assign-work [{:id 0 :work-in-progress \A, :time-to-finish 31}
                       {:id 1 :work-in-progress nil, :time-to-finish nil}
                       {:id 2 :work-in-progress \C, :time-to-finish 28}]
                      \Z 6 70))

(defn assign-work-to-idle-workers
  "주어진 작업을 놀고 있는 워커들에게 할당해줍니다.

  timestamp: 현재 타임스탬프
  works: 워커들에게 할당해줄 작업들
  work-times: 각 작업별 소요시간
  workers: 모든 워커들"
  [timestamp works work-times workers]
  (let [idle-workers (filter idle-worker? workers)
        primary-work (first works)
        required-time (get work-times primary-work)
        already-in-progress? (->> workers
                                  (map #(:work-in-progress %))
                                  (filter #(= primary-work %))
                                  empty?
                                  not)]
    (cond
      ; 놀고 있는 워커들이 없다면 할당을 할 수 없다.
      (empty? idle-workers) workers

      ; 잔여 작업이 없으면 할당을 할 수 없다.
      (empty? works) workers

      ; 그 외의 경우 워커 하나에게 작업을 할당해준다.
      :else (recur
              timestamp
              (rest works)
              work-times
              (if already-in-progress?
                workers
                (assign-work workers primary-work required-time timestamp))))))

(comment (assign-work-to-idle-workers 60
                                      [\A \B]
                                      (work-required-times 60)
                                      (create-workers 2)))

(defn get-closest-finish-time
  "여러 워커들이 진행중인 작업들 중 가장 먼저 끝나는 작업의 완료 시각을 리턴합니다."
  [workers]
  (->> workers
       (map #(:time-to-finish %))
       (filter some?)
       sort
       first))

(defn solve-7-2
  "https://adventofcode.com/2018/day/7 part 2 문제의 정답을 구해 리턴합니다.
  주어진 문자열을 읽고, 올바른 작업순서를 생성해 문자열 형태로 리턴합니다."
  [input-string, worker-count, time-adjust]
  (let [work-times (work-required-times time-adjust)
        work-context (string->work-context input-string)
        {root-works     :root-works
         required-works :required-works
         all-works      :all-works} work-context]

    (loop [timestamp 0
           workers (create-workers worker-count)
           finished-works #{}]
      (let [
            ; 각 워커들에서 완료된 작업을 수집한다.
            finished (collect-finished-works timestamp workers)
            ; 완료 작업 집합을 갱신한다.
            new-finished-works (apply conj finished-works finished)
            ; 작업이 없는 워커들을 리셋한다.
            refreshed-workers (reset-finished-workers timestamp workers)
            ; 다음 잔여 작업들을 선정한다.
            remaining-works (find-available-work all-works required-works new-finished-works)
            ; 놀고 있는 워커들에게 작업을 할당한다.
            assigned-workers (assign-work-to-idle-workers timestamp remaining-works work-times refreshed-workers)
            ; 진행중인 작업들 중 가장 빨리 끝나는 시간으로 시간을 흘려보낸다.
            next-time (get-closest-finish-time assigned-workers)]

        #_#_#_#_(println)
        (println "timestamp: " timestamp)
        (println "완료된 작업: " new-finished-works)
        (println "workers: \n" (str/replace (str assigned-workers) #"\}" "}\n"))

        (if (= all-works new-finished-works)
          timestamp
          (recur next-time assigned-workers new-finished-works))))))

(comment (solve-7-2 sample-input-string 2 1)                ; 15
         (solve-7-2 input-string 5 61))                     ; 1105

