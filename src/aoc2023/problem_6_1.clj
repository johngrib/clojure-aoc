(ns aoc2023.problem-6-1
  (:require [clojure.java.io :as io]
            [clojure.string :as s]))

(comment
  " https://adventofcode.com/2023/day/6

  장난감 보트 경주

  정해진 시간 동안 보트를 타고 최대한 멀리 이동해야 한다.
  가장 멀리 간 보트가 우승하는 방식.

  참가신청시 각 레이스에 허용되는 제한시간과 역대 최고 거리 기록을 받게 된다.
  상을 받으려면 각 레이스에서 최고 기록을 갱신해야 한다.

  보트에는 버튼이 달려 있다.
  버튼을 누르고 있으면 보트가 충전되고, 버튼을 놓으면 보트가 이동한다.
  버튼을 오래 누르고 있다가 놓으면 더 빨리 움직인다.
  버튼을 누르고 있는 시간도 총 레이스 시간에 포함된다.
  레이스가 시작되기 전에는 버튼을 누를 수 없다.

  (예제)
  Time:      7  15   30
  Distance:  9  40  200

  1번 경기는 7ms 동안 진행됐다. 기록은 9 밀리미터.
  2번 경기는 15ms 동안 진행됐다. 기록은 40 밀리미터.
  3번 경기는 30ms 동안 진행됐다. 기록은 200 밀리미터.

  보트의 시작 속도는 0mm/ms 이다.
  버튼을 누르는 시간이 1ms 가 될 때마다 보트의 속도는 1mm/ms 씩 증가한다.

  따라서 1번 경기의 경우 선택지가 8가지 있다.

  1. 버튼을 누르지 않는다. 보트는 움직이지 않고 경기 끝까지 0mm 를 이동한다.
  2. 1ms 동안 버튼을 누른다. 보트는 남은 6ms 동안 1mm/ms 의 속도로 이동한다. 결국 6mm 를 이동한다.
  3. 2ms 동안 버튼을 누른다. 보트는 남은 5ms 동안 2mm/ms 의 속도로 이동한다. 결국 10mm 를 이동한다.
  4. 3ms 동안 버튼을 누른다. 보트는 남은 4ms 동안 3mm/ms 의 속도로 이동한다. 결국 12mm 를 이동한다.
  5. 4ms 동안 버튼을 누른다. 보트는 남은 3ms 동안 4mm/ms 의 속도로 이동한다. 결국 12mm 를 이동한다.
  6. 5ms 동안 버튼을 누른다. 보트는 남은 2ms 동안 5mm/ms 의 속도로 이동한다. 결국 10mm 를 이동한다.
  7. 6ms 동안 버튼을 누른다. 보트는 남은 1ms 동안 6mm/ms 의 속도로 이동한다. 결국 6mm 를 이동한다.
  8. 7ms 동안 버튼을 누른다. 결국 0mm 를 이동한다.

  그런데 1번 경기의 기록은 9mm 이다.
  따라서 2ms, 3ms, 4ms, 5ms 동안 버튼을 누르는 방법 4가지로 기록을 갱신할 수 있다.

  2번 경기는 4ms ~ 11ms 동안 버튼을 누르고 기록을 갱신하는 방법이 8가지가 있다.
  3번 경기는 11ms ~ 19ms 동안 버튼을 누르고 기록을 갱신하는 방법이 9가지가 있다.

  문제:
    각 경기에서 기록을 깰 수 있는 방법의 수를 모두 구한다.
    그리고 그 수를 모두 곱한 값이 답이다.
  ")


(def data-file (-> "aoc2023/input6.txt" io/resource slurp))
(def input-lines (->> data-file
                     s/split-lines
                     (map #(re-seq #"\d+" %))
                     (map #(map parse-long %))
                     ((fn [[times distances]]
                        (map vector times distances)))
                     (map (fn [[time distance]]

                            {:time     time
                             :distance distance}))))

(defn a-b-c
  "time distance를 받아서 근의공식을 계산하기 위한 a, b, c 값 맵으로 리턴합니다."
  [{time     :time
    distance :distance
    :as      input}]
  (merge input
         {:a -1
          :b time
          :c (- distance)}))

(defn 판별식
  "2차 방정식의 판별식을 계산한 값을 리턴합니다."
  [{:keys [a b c]}]
  (- (* b b)
     (* 4 a c)))

(defn 근의-공식
  "2차 방정식의 근의 공식을 계산한 값을 리턴합니다."
  [{:keys [a b c] :as input}]
  (let [discriminant (판별식 input)
        dis          (Math/sqrt discriminant)]
    (cond
      (< discriminant 0) []
      (= discriminant 0) [(/ (- b) (* 2 a))]
      :else [(+ (/ (- b) (* 2 a))
                (/ dis (* 2 a)))
             (- (/ (- b) (* 2 a))
                (/ dis (* 2 a)))])))

(defn extract-fence
  "근의 공식을 통해 얻어낸 해당 게임의 승리할 수 있는 버튼 누르는 시간을 정수로 변환합니다."
  [[x1 x2]]
  (let [xx1 (Math/ceil x1)
        xx2 (Math/floor x2)]
    [(long (if (== xx1 x1) (inc xx1) xx1))
     (long (if (== xx2 x2) (dec xx2) xx2))]))

(defn solve-count
  [[x1 x2]]
  (inc (- x2 x1)))

(defn solve
  [input-lines]
  (->> input-lines
       (map a-b-c)
       (map 근의-공식)
       (map extract-fence)
       (map solve-count)
       (apply *)))
  
(def sample-input
  [{:time 7 :distance 9}
   {:time 15 :distance 40}
   {:time 30 :distance 200}])

(comment
  (solve sample-input) ; => 288
  (solve input-lines) ; => 440000
  ;;
  )

