(ns joy-of-clojure.ch1.chess-board
  "클로저 프로그래밍의 즐거움 Part 1. 26쪽의 체스판 예제.")

(defn initial-board
  "초기화된 체스판을 리턴합니다.
  흰색 체스말은 대문자, 검은색 체스말은 소문자이며, 빈 칸은 - 입니다.
  좌표는 가장 왼쪽 아래가 a1, 가장 오른쪽 위가 h8 입니다."
  []
  [\r \n \b \q \k \b \n \r
   \p \p \p \p \p \p \p \p
   \- \- \- \- \- \- \- \-
   \- \- \- \- \- \- \- \-
   \- \- \- \- \- \- \- \-
   \- \- \- \- \- \- \- \-
   \P \P \P \P \P \P \P \P
   \R \N \B \Q \K \B \N \R])

(def ^:dynamic *file-key* "x 좌표 기준" \a)
(def ^:dynamic *rank-key* "y 좌표 기준" \0)

(defn- file-component
  "주어진 file을 x 좌표로 변환한 숫자를 리턴합니다
  예: a -> 0, b -> 1, ..., h -> 7"
  [file]
  (- (int file) (int *file-key*)))

(defn- rank-component
  "주어진 rank를 y 좌표로 변환한 숫자를 리턴합니다
  예: 0 -> 64, 1 -> 56, ..., 7 -> 8"
  [rank]
  (->> (int *rank-key*)
       (- (int rank))
       (- 8)
       (* 8)))

(defn- index
  "주어진 file, rank에 해당하는 체스판 인덱스 넘버를 리턴합니다.
  체스판이 1차원 배열이므로 인덱스는 하나의 int 입니다."
  [file rank]
  (+ (file-component file)
     (rank-component rank)))

(defn lookup
  "board에서 position에 놓여있는 체스 말의 기호를 리턴합니다.
  position은 체스판의 좌표 표기법(a1 ~ h7)으로 입력합니다."
  [board position]
  (let [[file rank] position]
    (board (index file rank))))

(comment
  (lookup (initial-board) "a1") ;; => \R
  (lookup (initial-board) "e1") ;; => \K
  (lookup (initial-board) "b8") ;; => \n
  )

