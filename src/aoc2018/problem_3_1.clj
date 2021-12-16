(ns aoc2018.problem-3-1
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(comment "
## 문제
https://adventofcode.com/2018/day/3

다음과 같은 입력이 주어짐.

#1 @ 1,3: 4x4
#2 @ 3,1: 4x4
#3 @ 5,5: 2x2
# 뒤에 오는 숫자는 ID, @ 뒤에 오는 숫자 쌍 (a, b)는 시작 좌표, : 뒤에 오는 (c x d)는 격자를 나타냄.
입력의 정보대로 격자 공간을 채우면 아래와 같이 됨.

........
...2222.
...2222.
.11XX22.
.11XX22.
.111133.
.111133.
........
여기서 XX는 ID 1, 2, 3의 영역이 두번 이상 겹치는 지역.
겹치는 지역의 갯수를 출력하시오. (위의 예시에서는 4)
")

(def data-file (-> "aoc2018/input3.txt" io/resource slurp))
(def input-strings (str/split-lines data-file))

(defn to-location-code
  "문제에서 정의한 형식의 코드 한 줄을 받아 위치정보로 변환해 리턴합니다."
  [raw-code]
  ;                        id      x     y      width height
  (let [codes (re-find #"#(\d+) @ (\d+),(\d+): (\d+)x(\d+)" raw-code)]
    {
     :id     (-> codes (nth 1) read-string)
     :x      (-> codes (nth 2) read-string)
     :y      (-> codes (nth 3) read-string)
     :width  (-> codes (nth 4) read-string)
     :height (-> codes (nth 5) read-string)}))


(defn expand-code-map [code-map]
  "주어진 위치정보를 확장해 좌표들의 리스트로 만들어 리턴합니다."
  (let [
        c code-map
        start-x (c :x)
        end-x (+ start-x (c :width))
        column (range start-x end-x)

        start-y (c :y)
        end-y (+ start-y (c :height))
        row (range start-y end-y)]

    (for [x column, y row]
      {:x x :y y})))


(defn solve-3-1 [input-strings]
  (->> input-strings
       (map to-location-code)
       (map expand-code-map)
       (reduce into)
       frequencies
       vals
       (filter #(> % 1))
       count))


