(ns aoc2018.problem-4-1-test
  (:require [clojure.test :refer :all])
  (:require [aoc2018.problem-4-1 :refer :all]))

(deftest describe:to-sleep-datum
  "to-sleep-datum 함수는"
  (testing "잠들고 깨어난 로그를 받아 잠든 시간에 대한 map을 리턴합니다."
    (are [input-log result]
      (= (to-sleep-datum input-log) result)
      "[1518-11-21 00:30] falls asleep [1518-11-21 00:38] wakes up"
      {:sleep 30, :wake 38, :sleeping-time 8}

      "#10 [1518-11-03 00:24] falls asleep [1518-11-03 00:29] wakes up "
      {:sleep 24, :wake 29, :sleeping-time 5})))

(deftest describe:to-day-log
  "to-day-log 함수는"
  (testing "하루의 로그를 분석해 map들의 리스트로 만들어 리턴합니다."
    (are [input-log result]
      (= (to-day-log input-log) result)
      "#10 [1518-11-01 00:05] falls asleep [1518-11-01 00:25] wakes up [1518-11-01 00:30] falls asleep [1518-11-01 00:55] wakes up "
      [{:guard-id 10, :date "1518-11-01", :sleep 5, :wake 25, :sleeping-time 20}
       {:guard-id 10, :date "1518-11-01", :sleep 30, :wake 55, :sleeping-time 25}])))

(deftest describe:simplify-logs
  "simplify-logs 함수는"
  (testing "주어진 로그 문자열을 시간순으로 정렬한 다음, 개별 로그로 재생성해 리턴합니다."
    (is (=
          (simplify-logs sample-input-string)
          [{:guard-id 99, :date "1518-11-05", :sleep 45, :wake 55, :sleeping-time 10}
           {:guard-id 99, :date "1518-11-04", :sleep 36, :wake 46, :sleeping-time 10}
           {:guard-id 10, :date "1518-11-03", :sleep 24, :wake 29, :sleeping-time 5}
           {:guard-id 99, :date "1518-11-02", :sleep 40, :wake 50, :sleeping-time 10}
           {:guard-id 10, :date "1518-11-01", :sleep 5, :wake 25, :sleeping-time 20}
           {:guard-id 10, :date "1518-11-01", :sleep 30, :wake 55, :sleeping-time 25}]))))

(comment
  (simplify-logs sample-input-string))

(deftest describe:solve-4-1
  "solve-4-1 함수는"
  (testing "https://adventofcode.com/2018/day/4 에 올라온 part1 예제를 제공하면 정답인 240이 포함된 결과를 리턴한다"
    (is (= 240 (:solution (solve-4-1 sample-input-string)))))

  (testing "https://adventofcode.com/2018/day/4 에 올라온 part1 입력을 제공하면 정답인 26281이 포함된 결과를 리턴한다"
    (is (= 26281 (:solution (solve-4-1 input-strings))))))

(comment
  (solve-4-1 sample-input-string)
  (solve-4-1 input-strings))

(comment
  (run-tests))
