(ns brave-clojure.chapter11
  "https://www.braveclojure.com/core-async/"
  (:require [clojure.core.async :as a :refer [>! <! >!! <!! go chan buffer close! thread alts! alts!! timeout]]
            [clojure.string :as str]))

; 275쪽
(comment
  (def echo-chan (chan))

  (go
    ; 메시지를 echo-chan 에서 가져오면 println 으로 출력해라 ( <! : 가져오면...)
    (println (<! echo-chan)))

; 채널에 메시지가 올라가면 프로세스는 다른 프로세스가 그 메시지를 취할 때까지 차단한다.

; 문자열 ketchup을 echo-chan 채널에 입력하고 true를 리턴한다.
  (>!! echo-chan "ketchup"))
; ketchup
; true

; 276쪽 - 버퍼링
(comment
  (def echo-buffer (chan 2))
  (>!! echo-buffer "ketchup")
  ; true

  (>!! echo-buffer "ketchup"))
; true

(comment
  (>!! echo-buffer "ketchup"))
; 채널이 꽉 차서 블록된다. (REPL이 멈추니 인터럽트를 보낼 것)

; 277쪽 - blocking과 parking
"가져오는 함수는 <! 으로 느낌표가 하나.
집어넣는 함수는 >!! 로 느낌표가 두 개라는 점에 유의.
-> go 블록 안에서는 느낌표를 하나만 쓰고, go 블록 밖에서는 느낌표를 두 개 쓰면 된다.

>!  : put. go block 안에서만.
>!! : put.
<!  : take. go block 안에서만.
<!! : take.

go 블록은 정해진 크기의 스레드 풀을 쓰기 때문에 1000 개의 go 프로세스를 만들어도 풀 내의 스레드만 쓸 수 있다."

(comment
  (def hi-chan (chan))
  (doseq [n (range 1000)]
    (go (>! hi-chan (str "hi " n))))

  (thread (println (<!! echo-chan)))
  (>!! echo-chan "mustard")

  (let [t (thread "chili")]
    (<!! t)))

; 280쪽 - 핫도그 자판기 프로세스

(defn hot-dog-machine
  []
  (let [in (chan)
        out (chan)]
    (go (<! in)
        (>! out "hot dog"))
    [in out]))

(comment
  (let [[in out] (hot-dog-machine)]
    (>!! in "pocket lint")
    (<!! out)))
; "hot dog"

(defn hot-dog-machine-v2
  "숫자 3이 입력될 때만 핫도그를 배출하는 핫도그 자판기"
  [hot-dog-count]
  ; in, out 채널을 준비한다.
  (let [in (chan)
        out (chan)]
    (go (loop [hc hot-dog-count]
          (if (> hc 0)
            ; in 채널에서 값을 뽑는다
            (let [input (<! in)]
              (if (= 3 input)
                ; 3 달러가 들어왔다면 out 채널에 hot dog 를 집어넣는다
                (do (>! out "hot dog")
                    (recur (dec hc)))
                ; in 채널의 값이 3이 아니라면 out 채널에 시든 상추를 집어넣는다
                (do (>! out "wilted lettuce")
                    (recur hc))))
            ; hc 핫도그 숫자가 0이 되면 in 과 out 을 닫는다.
            (do (close! in)
                (close! out)))))
    [in out]))
(comment
  ; 핫도그가 2개 들어있는 핫도그 자판기
  (let [[in out] (hot-dog-machine-v2 2)]
    ; 호주머니 부스러기 넣어본다
    (>!! in "pocket lint")
    (println (<!! out)) ; wilted lettuce ; 상추가 나온다

    ; 3 달러를 넣는다
    (>!! in 3)
    (println (<!! out)) ; hot dog 핫도그가 나온다.

    ; 3 달러를 넣는다
    (>!! in 3)
    (println (<!! out)) ; hot dog 핫도그가 나온다.

    ; 3 달러를 넣는다
    (>!! in 3)
    (<!! out))) ; 채널이 닫혔으므로 무시되었으며, 숫자 3은 채널로 들어오지 못했다.

; 282쪽 - alts!!
"core.async 의 alts!! 함수를 쓰면 일련의 작업 중에서 첫 번째로 끝난 채널 작업 결과를 쓸 수 있다.
alts!! 를 쓰면 주어진 밀리 초 단위 시간 동안 대기하다가 close 하는 timeout channel을 만들 수 있다."

; 286쪽 - 프로세스 파이프라인으로 콜백 지옥 탈출
(defn upper-caser
  [in]
  (let [out (chan)]
    (go (while true (>! out (str/upper-case (<! in)))))
    out))

(defn reverser
  [in]
  (let [out (chan)]
    (go (while true (>! out (str/reverse (<! in)))))
    out))

(defn printer
  [in]
  (go (while true (println (<! in)))))

(def in-chan (chan))
(def upper-caser-out (upper-caser in-chan))
(def reverser-out (reverser upper-caser-out))

(comment
  (printer reverser-out)

  (>!! in-chan "redrum")
  ; MURDER
  (>!! in-chan "repaid")
  ; DIAPER
  )
