(ns brave-clojure.chapter5
  "chapter 5 - 함수형 프로그래밍
  https://www.braveclojure.com/functional-programming/"
  (:require [clojure.string :as str]))

; 121쪽
(defn sum
  ([vals] (sum vals 0))
  ([vals accumulating-total]
   (if (empty? vals)
     accumulating-total
     (sum (rest vals)
          (+ (first vals) accumulating-total)))))
(sum [39 5 1]) ;; => 45
(sum [39 5 1] 0) ;; => 45
(sum [5 1] 39) ;; => 45
(sum [1] 44) ;; => 45
(sum [] 45) ;; => 45

(defn sum-with-recur
  ([vals]
   (sum-with-recur vals 0))
  ([vals accumulating-total]
   (if (empty? vals)
     accumulating-total
     (recur (rest vals)
            (+ (first vals) accumulating-total)))))

; 123쪽 - 속성 변화를 대신하는 함수 합성
(defn clean
  [text]
  (str/replace (str/trim text) #"lol" "LOL"))
(clean "My boa constrictor is so sassy lol!   ")

; 함수형 프로그래밍은 간단한 함수를 합성해서 더 복잡한 함수를 쉽게 만들도록 돕는다

; 124쪽 - comp
((comp inc *) 2 3) ;; => 7
(def character
  {:name "Smooches McCutes"
   :attributes {:intelligence 10
                :strength     4
                :dexterity    5}})
(def c-int (comp :intelligence :attributes))
(def c-str (comp :strength :attributes))
(def c-dex (comp :dexterity :attributes))

(comment
  (c-int character) ;; =>10
  (c-str character) ;; =>4
  (c-dex character)) ;; =>5

(defn spell-slots
  [char]
  ; 2로 나누고 1을 더한 다음, int로 소수점 이하를 버린다
  (int (inc (/ (c-int char) 2))))
(spell-slots character) ;; =>6

(def spell-slots-with-comp
  "comp를 사용해서 spell-slots 와 똑같이 작동하게 만든 함수"
  (comp int inc #(/ % 2) c-int))
(spell-slots-with-comp character)

(defn two-comp
  [f g]
  (fn [& args]
    (f (apply g args))))

; 127쪽 - memoize
(defn sleepy-identity
  "Returns the given value after 1 second"
  [x]
  (Thread/sleep 1000)
  x)
(comment
  ; 1초 후에 출력
  (sleepy-identity "Mr. Fantastico"))

(def memo-sleepy-identity (memoize sleepy-identity))
(comment
  ; 한 번 실행했다면 그 다음부터는 즉시 리턴한다
  (memo-sleepy-identity "Mr. Fantastico"))

; 128쪽 - 말뚝놀이 게임 (Peg Thing Game)
(defn tri*
  "삼각수 lazy-seq"
  ([] (tri* 0 1))
  ([sum n]
   (let [new-sum (+ sum n)]
     (cons new-sum (lazy-seq (tri* new-sum (inc n)))))))

(def tri (tri*))

(comment
  (take 5 tri))

(defn triangular?
  "주어진 숫자가 1,3,5,6,10,15 등과 같은 삼각수인지 판정하기"
  [n]
  (= n (last (take-while #(>= n %) tri))))
(comment
  (triangular? 5)
  (triangular? 6))

(defn row-tri
  "n번 row 가장 오른쪽의 삼각수 찾기"
  [n]
  (last (take n tri)))
(comment
  (row-tri 1) ;; =>1
  (row-tri 2) ;; =>3
  (row-tri 3)) ;; =>6

(defn row-num
  "Returns row number the position belongs to:
  pos 1 in row 1,
  positions 2 and 3 in row 2, etc"
  [pos]
  (inc (count (take-while #(> pos %) tri))))
(comment
  (row-num 1) ;; =>1
  (row-num 5)) ;; =>3

; 135쪽
(defn connect
  "두 위치 간 서로 연결맺기"
  [board max-pos pos neighbor destination]
  (if (<= destination max-pos)
    (reduce (fn [new-board [p1 p2]]
              (assoc-in new-board [p1 :connections p2] neighbor))
            board
            [[pos destination] [destination pos]])
    board))
(comment
  (connect {} 15 1 2 4)) ;; =>{1 {:connections {4 2}}, 4 {:connections {1 2}}}

(comment
  (assoc-in {} [:cookie :monster :vocals] "Finntroll")
  ;; =>{:cookie {:monster {:vocals "Finntroll"}}}
  (get-in {:cookie {:monster {:vocals "Finntroll"}}} [:cookie :monster])
  (assoc-in {} [1 :connections 4] 2))

; 136쪽
(defn connect-right
  [board max-pos pos]
  (let [neighbor (inc pos)
        destination (inc neighbor)]
    (if-not (or (triangular? neighbor)
                (triangular? pos))
      (connect board max-pos pos neighbor destination)
      board)))

(defn connect-down-left
  [board max-pos pos]
  (let [row (row-num pos)
        neighbor (+ row pos)
        destination (+ 1 row neighbor)]
    (connect board max-pos pos neighbor destination)))

(defn connect-down-right
  [board max-pos pos]
  (let [row (row-num pos)
        neighbor (+ 1 row pos)
        destination (+ 2 row neighbor)]
    (connect board max-pos pos neighbor destination)))

(comment
  (connect-down-left {} 15 1)
 ;; =>{1 {:connections {6 3}}, 6 {:connections {1 3}}}
  (connect-down-left {} 15 3))

(defn add-pos
  "위치에 말뚝을 옮기고 연결짓기"
  [board max-pos pos]
  (let [pegged-board (assoc-in board [pos :pegged] true)]
    (reduce (fn [new-board connection-creation-fn]
              (connection-creation-fn new-board max-pos pos))
            pegged-board
            [connect-right connect-down-left connect-down-right])))

(comment
  (add-pos {} 15 1))

(defn clean
  [text]
  (reduce (fn [string string-fn] (string-fn string))
          text
          [str/trim #(str/replace % #"lol" "LOL")]))

(defn new-board
  [rows]
  (let [initial-board {:rows rows}
        max-pos (row-tri rows)]
    (reduce (fn [board pos] (add-pos board max-pos pos))
            initial-board
            (range 1 (inc max-pos)))))

; 138쪽
(defn pegged?
  "해당 위치에 말뚝이 있는지 확인하기"
  [board pos]
  (get-in board [pos :pegged]))

(defn remove-peg
  "주어진 위치의 말뚝 제거하기"
  [board pos]
  (assoc-in board [pos :pegged] false))

(defn place-peg
  "주어진 위치에 말뚝 넣기"
  [board pos]
  (assoc-in board [pos :pegged] true))

(defn move-peg
  [board p1 p2]
  (place-peg (remove-peg board p1) p2))

(defn valid-moves
  "키가 위치이고 뛰어넘은 위치가 값인 pos의 모든 유효한 값이 맵을 반환하기"
  [board pos]
  (into {}
        (filter (fn [[destination jumped]]
                  (and (not (pegged? board destination))
                       (pegged? board jumped)))
                (get-in board [pos :connections]))))
(def my-board (assoc-in (new-board 5) [4 :pegged] false))

(comment
  (valid-moves my-board 1) ;; =>{4 2}
  (valid-moves my-board 6) ;; => {4 5}
  (valid-moves my-board 11) ;; => {4 7}
  (valid-moves my-board 5) ;; => {}
  (valid-moves my-board 8) ;; => {}
  )

(defn valid-move?
  "p1에서 p2로 옮긴 것이 유효하면 건너뛴 위치를, 그렇지 않다면 nil를 리턴합니다."
  [board p1 p2]
  (get (valid-moves board p1) p2))

(comment
  (valid-move? my-board 8 4) ;; => nil
  (valid-move? my-board 1 4)) ;; => 2

(defn make-move
  [board p1 p2]
  (if-let [jumped (valid-move? board p1 p2)]
    (move-peg (remove-peg board jumped) p1 p2)))

(defn can-move?
  "말뚝이 박힌 위치 중 유효하게 움직일 수 있는 것이 있는가?"
  [board]
  (some (comp not-empty (partial valid-moves board))
        (map first (filter #(get (second %) :pegged) board))))

(def alpha-start 97)
(def alpha-end 123)
(def letters (map (comp str char) (range alpha-start alpha-end)))
(def pos-chars 3)

(def colorize list)

(defn render-pos
  [board pos]
  (str (nth letters (dec pos))
       (if (get-in board [pos :pegged])
         (colorize "0" :blue)
         (colorize "-" :red))))

(defn row-positions
  "주어진 가로줄의 모든 위치를 반환하기"
  [row-num]
  (range (inc (or (row-tri (dec row-num)) 0))
         (inc (row-tri row-num))))

(defn row-padding
  "가로 줄의 앞부분에 일련의 공백문자를 덧붙여서 가운데에 출력되게 하기"
  [row-num rows]
  (let [pad-length (/ (* (- rows row-num) pos-chars) 2)]
    (apply str (take pad-length (repeat " ")))))

(defn render-row
  [board row-num]
  (str (row-padding row-num (:rows board))
       (clojure.string/join " " (map (partial render-pos board)
                                     (row-positions row-num)))))

(defn print-board
  [board]
  (doseq [row-num (range 1 (inc (:rows board)))]
    (println (render-row board row-num))))

(defn letter->pos
  "Converts a letter string to the corresponding position number"
  [letter]
  (inc (- (int (first letter)) alpha-start)))

(defn get-input
  "Waits for user to enter text and hit enter, then cleans the input"
  ([] (get-input nil))
  ([default]
   (let [input (clojure.string/trim (read-line))]
     (if (empty? input)
       default
       (clojure.string/lower-case input)))))

