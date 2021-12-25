(ns aoc2018.problem-6-1
  (:require [clojure.java.io :as io]))

(comment "
https://adventofcode.com/2018/day/6 part1

일종의 삼국지 게임.
- 문제에서 주어진 기본 점을을 군주-점(king-dot)이라고 부르자.
- 군주-점이 영토를 넓혀가는 게임.
- 모든 점은 각 턴마다 상하좌우로 1칸씩 영역을 넓혀갑니다.
- 각 점은 가장 가까운 맨하튼 거리를 가진 군주-점의 영토가 됩니다.
- 만약 어떤 점에 대해 가장 가까운 거리를 가진 군주-점이 2개 이상이면, 그 점은 누구의 소유도 아닙니다. ('.'으로 표시)
- 공간의 크기 제약은 없습니다.
- 임의의 턴이 지났을 때 유한한 넓이를 갖는다고 판별된 영역들 중 가장 넓은 영역의 넓이를 구하세요.
")

(defn absolute
  "주어진 수의 절대값을 리턴합니다."
  [x] (max x (- x)))

(defn manhattan-distance
  "두 점 사이의 맨하튼 거리(정수)를 리턴합니다."
  [dot1 dot2]
  (let [delta (merge-with - dot1 dot2)]
    (+ (absolute (:x delta)) (absolute (:y delta)))))

(comment
  (manhattan-distance {:x 1 :y 0} {:x 1 :y 1})              ; 1
  (manhattan-distance {:x 0 :y 0} {:x -1 :y -1})            ; 2
  (manhattan-distance {:x -1 :y -1} {:x 1 :y 1}))           ; 4

(defn input->dots
  "여러 좌표를 표현하는 입력 문자열을 읽고, 좌표 map의 리스트를 리턴합니다."
  [input-string]
  (->> input-string
       (re-seq #"(\d+),\s*(\d+)")
       (map (fn [[_ x y]]
              {:x (Integer/parseInt x), :y (Integer/parseInt y)}))))

(comment
  (input->dots "3,4  1,2"))

(defn a-dot->game-data
  "점 하나를 체크하여, 업데이트된 게임 데이터를 리턴합니다."
  [dot game-data]
  (let [
        manhattan-거리들 (->> (:king-dot-list game-data)
                           (map (fn [king-dot] {:dot      king-dot
                                                :distance (manhattan-distance dot king-dot)}))
                           (sort-by :distance <))
        closest-king (first manhattan-거리들)
        closest-king-좌표 (:dot closest-king)

        closest-king이-또-있다? (->> manhattan-거리들
                                 (filter #(= (:distance closest-king) (:distance %)))
                                 (filter #(not (= closest-king-좌표 (:dot %))))
                                 empty?
                                 not)

        넓이-저장소 (:area-of-each-kings game-data)]
    (cond
      ; closest 군주-점들이 또 있다면 이 점은 누구의 소유도 아니다. game-data에 아무것도 추가하지 않고 리턴한다.
      closest-king이-또-있다?
      game-data

      ; 이 점이 경계에 있다면, 이 점과 가장 가까운 군주-점은 무한한 영토를 갖는다는 뜻이다. 무한영토 군주 정보에 해당 군주-점을 등록한다.
      ((:dot-on-border? game-data) dot)
      (assoc game-data :no-limit-kings (merge (:no-limit-kings game-data) closest-king-좌표))

      ; 이 점은 가장 가까운 군주-점의 영토가 된다. 해당 군주-점의 영토 넓이에 +1 한다.
      (넓이-저장소 closest-king-좌표)
      (let [가장-가까운-군주-점의-영토-넓이 (get (:area-of-each-kings game-data) closest-king-좌표)]
        (assoc game-data
          :area-of-each-kings
          (assoc 넓이-저장소, closest-king-좌표, (inc 가장-가까운-군주-점의-영토-넓이))))

      ; 이 점과 가장 가까운 군주-점의 영토 넓이가 아직 등록되지 않았다면 해당 군주-점의 영토를 1로 지정한다.
      :else
      (assoc game-data
        :area-of-each-kings
        (assoc 넓이-저장소, closest-king-좌표, 1)))))

(defn create-border-checker
  "경계판별 함수를 생성해 리턴합니다.
  생성된 경게판별 함수는 주어진 점이 경계 바깥의 점이라면 true를 리턴합니다."
  [min-x max-x min-y max-y]
  (fn [{x :x, y :y}]
    (or (<= x min-x) (>= x max-x) (<= y min-y) (>= y max-y))))

(defn select
  "coll 컬렉션에서 key-word 값을 매핑한 다음, f1 함수를 적용한 결과를 리턴합니다."
  [f1 key-word coll]
  (->> coll
       (map #(key-word %))
       (apply f1)))

(defn solve-6-1
  "https://adventofcode.com/2018/day/6 문제 part1 을 풀이합니다.
  가장 큰 유한한 영토의 넓이를 리턴합니다."
  [input-string]
  (let [
        king-dots (input->dots input-string)

        min-x (select min :x king-dots)
        max-x (select max :x king-dots)
        min-y (select min :y king-dots)
        max-y (select max :y king-dots)
        x-좌표들 (range min-x (inc max-x))
        y-좌표들 (range min-y (+ 1 max-y))

        game-모든-점들 (for [y y-좌표들, x x-좌표들] {:x x, :y y})

        game-데이터 {:king-dot-list      king-dots
                  :no-limit-kings     #{}
                  :area-of-each-kings {}
                  :dot-on-border?     (create-border-checker min-x max-x min-y max-y)}
        game-결과 (reduce
                  (fn [g-data a-dot] (a-dot->game-data a-dot g-data))
                  game-데이터
                  game-모든-점들)
        무한한-영토를-갖는-kings (:no-limit-kings game-결과)
        유한한-영토를-갖는-kings (filter
                           (fn [[king _]] (nil? (get 무한한-영토를-갖는-kings king)))
                           (:area-of-each-kings game-결과))]
    (apply max (vals 유한한-영토를-갖는-kings))))

(def sample-input-string "1,1  1,6  8,3  3,4  5,5  8,9")
(def input-string (-> "aoc2018/input6.txt" io/resource slurp))

(solve-6-1 sample-input-string)                             ; 17
(solve-6-1 input-string)                                    ; 5187
