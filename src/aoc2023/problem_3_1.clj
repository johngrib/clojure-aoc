(ns aoc2023.problem-3-1
  (:require [clojure.java.io :as io]
            [clojure.string :as s]))

(comment
  " https://adventofcode.com/2023/day/3

  엔진 회로도에 있는 모든 부품 번호를 더하면 어떤 부품이 누락되었는지 알 수 있을 것이다.

  엔진 회로도는 input 파일로 주어질 것이다.
  엔진 회로도는 엔진 구성을 시각적으로 표현한 것이다.

  - 마침표(.)는 기호로 간주하지 않는다.
  - 기호에 인접한 숫자는 부품 번호이다. 기호에 대각선으로 인접한 숫자도 해당된다.

  (예시)
  467..114..  -> 114는 부품 번호가 아니다. 기호에 인접해 있지 않다.
  ...*......
  ..35..633.
  ......#...
  617*......
  .....+.58.  -> 58은 부품 번호가 아니다. 기호에 인접해 있지 않다.
  ..592.....
  ......755.
  ...$.*....
  .664.598..

  이 예시의 모든 부품 번호를 더하면 467 + 35 + 633 + 617 + 592 + 755 + 664 + 598 = 4361 이다.
  ")

(def data-file (-> "aoc2023/input3.txt" io/resource slurp))
(def input-lines (s/split-lines data-file))

(defn digit?
  "문자 c가 숫자이면 참, 숫자가 아니면 거짓을 리턴합니다."
  [c]
  (#{\0 \1 \2 \3 \4 \5 \6 \7 \8 \9} c))

(defn part?
  "문자 c가 부품이면 참, 부품이 아니면 거짓을 리턴합니다."
  [c]
  (and (not (digit? c))
       (not= \. c)))

(defn get-char-at
  "좌표 x, y에 있는 문자를 리턴합니다.
  좌표가 범위 바깥이면 nil을 리턴합니다."
  ([node]
   (get-char-at node input-lines))
  ([{:keys [x y]} input-data]
   (let [line (get input-data y)]
     (when line (get line x)))))

(comment
  (get-char-at {:x 0 :y 10000} ["..."]) ;; => nil
  (get-char-at {:x 1 :y 0} ["..."]) ;; => \.
  (get-char-at {:x 1 :y 1} ["..." ".#."]) ;; => \#
  ;;
  )

(defn get-number-at
  "좌표 x, y에 있는 여러자리의 숫자를 리턴합니다."
  ([node]
   (get-number-at node input-lines))
  ;
  ([{y :y x :x :as node} input-data]
   (when (digit? (get-char-at node
                              input-data))
     (let [line (get input-data y)
           left (or (subs line 0 x) "")
           right (or (subs line x) "")
           left-number (re-find #"\d+$" left)
           right-number (re-find #"^\d+" right)
           num-str (str left-number right-number)]
       (when (not= "" num-str)
         {:y      y
          :x      (- x (count left-number))
          :number (parse-long num-str)})))))

(comment
  (get-number-at {:y 0 :x 0} ["123"]) ;; => {:y 0, :x 0, :number "123"}
  (get-number-at {:y 0 :x 1} ["..3..."]) ;; => nil
  (get-number-at {:y 0 :x 1} ["123"]) ;; => {:y 0, :x 0, :number "123"}
  (get-number-at {:y 0 :x 2} ["123"]) ;; => {:y 0, :x 0, :number "123"}
  (get-number-at {:y 0 :x 3} ["......"]) ;; => nil
  (get-number-at {:y 0 :x 3} ["..3..."]) ;; => nil
  (get-number-at {:y 0 :x 3} [".123.."]) ;; => {:y 0, :x 1, :number "123"}
  )

(defn get-fence-numbers-at
  "좌표 x, y 주위의 모든 숫자를 수집한 결과를 리턴합니다."
  [{:keys [x y]}]
  (let [all-fence (for [yy (range (- y 1) (+ y 2))
                        xx (range (- x 1) (+ x 2))]
                    {:y yy :x xx})]
    (->> all-fence
         (map get-number-at)
         (filter identity)
         (into #{}))))

(comment
  (get-fence-numbers-at {:y 1 :x 42})
  (get-fence-numbers-at {:y 9 :x 52})
  )


(defn solve []
  (let [x-max (count (get input-lines 0))
        y-max (count input-lines)
        xy-all (for [y (range 0 y-max)
                     x (range 0 x-max)]
                 {:x x :y y})]
    (->> xy-all
         (map (fn [node]
                (assoc node :char (get-char-at node))))
         ;; 특수기호를 가진 좌표만 필터링한다
         (filter #(part? (:char %)))
         ;; 해당 특수기호 주위의 모든 숫자를 구한다
         (map get-fence-numbers-at)
         (apply concat)
         (into #{})
         (map :number)
         (apply +))))

(comment
  (solve) ;; => 525181
  ;;
  )
