(ns aoc2018.problem-4-1
  (:require [clojure.string :as str]
            [clojure.java.io :as io]))

(comment "
## 문제
https://adventofcode.com/2018/day/4

- 주인공이 침투하려고 하는 곳에는 경비원이 교대 근무를 한다.
- 경비원의 활동에 대한 로그가 주어진다. 이 로그를 분석해 답을 찾아야 한다.
  - 로그의 형식은 '[년-월-일 시:분] 로그 내용' 이다.
  - 경비원의 근무 시간은 00:00 ~ 00:59 이다.
  - 이 시간 동안 경비원은 잠을 자거나, 깨어 있다. (둘 중 하나)
- 로그는 시간순으로 정렬되어 있지 않다.
  - 예제의 로그는 시간순으로 되어 있지만, 문제를 풀기 위한 로그는 시간순으로 되어 있지 않으므로 주의해야 한다.

문제는 다음과 같다.

- 잠든시간의 총합을 구했을 때 가장 많이 잠들었던 경비원의 아이디를 찾는다.
- 그 경비원이 가장 많이 잠들어 있었던 분(minute)과 경비원의 아이디를 곱셈한 결과가 답이다.

아래의 `sample-input-string`은 경비원의 근무 기록 예제이다.

파트 1은 '주어진 입력에 대해서, 가장 오랜시간 잠들어있었던 가드의 ID와, 그 가드가 가장 빈번하게 잠들어 있었던 분(minute)의 곱을 구하라'
만약 20번 가드가 0시 10분~36분, 2시 5분~11분, 3시 11분~13분 이렇게 잠들어 있었다면, “11분“이 가장 빈번하게 잠들어 있던 ‘분’. 그럼 답은 20 * 11 = 220.
")

(def sample-input-string
  ["[1518-11-01 00:00] Guard #10 begins shift"
   "[1518-11-01 00:05] falls asleep"
   "[1518-11-01 00:25] wakes up"
   "[1518-11-01 00:30] falls asleep"
   "[1518-11-01 00:55] wakes up"
   "[1518-11-01 23:58] Guard #99 begins shift"
   "[1518-11-02 00:40] falls asleep"
   "[1518-11-02 00:50] wakes up"
   "[1518-11-03 00:05] Guard #10 begins shift"
   "[1518-11-03 00:24] falls asleep"
   "[1518-11-03 00:29] wakes up"
   "[1518-11-04 00:02] Guard #99 begins shift"
   "[1518-11-04 00:36] falls asleep"
   "[1518-11-04 00:46] wakes up"
   "[1518-11-05 00:03] Guard #99 begins shift"
   "[1518-11-05 00:45] falls asleep"
   "[1518-11-05 00:55] wakes up"])

; https://clojuredocs.org/clojure.core/*warn-on-reflection*

(def data-file (-> "aoc2018/input4.txt" io/resource slurp))
(def input-strings (str/split-lines data-file))

(defn to-sleep-datum
  "한 번 잠들었다 일어난 기록의 시간을 hash-map에 담아 리턴합니다.
  시간은 분(minute)만 기록합니다.

  예) [1518-11-21 00:30] falls asleep [1518-11-21 00:38] wakes up
     => { :sleep 30, :wake 38, :sleeping-time 8 } ; 30분에 잠들어서, 38분에 깨어났으므로, 8분간 잠을 잤다는 내용.
  "
  [a-sleep-log]
  (let [[_ sleep-str wake-str] (re-find #"(\d+)\]\s*falls asleep\s*\[.+?(\d+)\]\s*wakes up" a-sleep-log)
        sleep (Integer/parseInt sleep-str)
        wake (Integer/parseInt wake-str)]
    {:sleep         sleep
     :wake          wake
     :sleeping-time (- wake sleep)}))

(comment
  (to-sleep-datum " [1518-11-21 00:30] falls asleep [1518-11-21 00:38] wakes up ")
  (to-sleep-datum " [1518-11-01 00:05] falls asleep [1518-11-05 00:55] wakes up "))

(defn to-day-log
  "한 줄로 이루어진 하루의 로그를 분석해 map으로 생성해 리턴합니다."
  [log-string]
  (let [[_ guard-id date] (re-find #"^[^#]*?#(\d+)\s*\[(\d+-\d+-\d+)" log-string)
        removed-guard-id (str/replace log-string #"^#\d+\s*" "")
        split-logs (-> removed-guard-id 
                       (str/replace #"(wakes up)\s*" "$1\n")
                       str/split-lines)
        sleep-data (map to-sleep-datum split-logs)]
    (map #(conj {:guard-id (Integer/parseInt guard-id), :date date} %)
         sleep-data)))

(comment
  (to-day-log
    (str "[1518-11-01 00:00] Guard #10 "
         "[1518-11-01 00:05] falls asleep [1518-11-01 00:25] wakes up "
         "[1518-11-01 00:30] falls asleep [1518-11-01 00:55] wakes up ")))

(defn simplify-logs
  "입력된 raw log를 날짜-시간 기준으로 정렬하고, 여러줄로 나뉘었던 로그를 하루 기준으로 분류한 리스트로 리턴합니다."
  [input-strings]
  (let [whole-log (str/join " " (sort input-strings))
        split-logs (-> whole-log 
                       (str/replace #"\[[^]]+\] Guard (#\d+) begins shift" "\n$1")
                       str/split-lines)]
    (->> split-logs
         (filter #(not (re-matches #"^(#\d+)?\s*" %)))
         (map to-day-log)
         (reduce into))))

(comment
  (simplify-logs sample-input-string))

(defn expand-sleep-minutes
  "수면 시작/종료 시간을 토대로 잠들었던 분 목록을 리턴합니다."
  [sleep-data-list]
  (->> sleep-data-list
       (map #(range (:sleep %) (:wake %)))
       (reduce into)))

(comment
  (expand-sleep-minutes
    [{:sleep 3 :wake 8}
     {:sleep 1 :wake 5}]))

(defn summarize-sleep-data
  [[guard-id sleep-data]]
  {:id          guard-id
   :total-slept (apply + (map #(:sleeping-time %) sleep-data))})

(defn solve-4-1
  "https://adventofcode.com/2018/day/4 문제를 풀이합니다."
  [raw-strings]
  (let [경비원-기준으로-묶은-로그데이터 (group-by :guard-id (simplify-logs raw-strings))

        경비원별-총-수면시간 (map summarize-sleep-data 경비원-기준으로-묶은-로그데이터)
        best-잠꾸러기 (last (sort-by :total-slept 경비원별-총-수면시간))
        best-잠꾸러기-id (:id best-잠꾸러기)

        best-잠꾸러기가-가장-많이-잠들었던-순간 (->> (get 경비원-기준으로-묶은-로그데이터 best-잠꾸러기-id)
                                      expand-sleep-minutes
                                      frequencies
                                      (sort-by val >)
                                      ffirst)]

    {:sleepyhead-id         best-잠꾸러기-id,
     :frequent-slept-minute best-잠꾸러기가-가장-많이-잠들었던-순간,
     :total-slept           (:total-slept best-잠꾸러기)
     :solution              (* best-잠꾸러기-id
                               best-잠꾸러기가-가장-많이-잠들었던-순간)}))

; 26281
(solve-4-1 input-strings)

