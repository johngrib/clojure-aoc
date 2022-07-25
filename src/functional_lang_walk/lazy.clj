(ns functional-lang-walk.lazy
  (:require [clojure.string :as string]
            [clojure.java.io :as io]))

(def numbers '(1 2 3 4 5 6 7 8 9 10))

(defn slowly [x] (Thread/sleep 3600000) (+ x 1))

(comment
  ; 84쪽 게으른 평가
  (do
    (println "let's go")
    (map slowly numbers)
    (println "finish"))
  ;;
  )

(defn infinite-num
  [n]
  (cons n
        (lazy-seq (infinite-num (+ n 1)))))

(def num-list
  "lazy-seq를 사용했으므로 무한의 수 리스트는 게으르게 작동한다."
  (take 10 (infinite-num 0)))

(comment
  (type num-list) ;; => clojure.lang.LazySeq
  )

(defn do-it-slow
  [x]
  (Thread/sleep 1000)
  (println x)
  (+ 1 x))



(comment
  "for 는 lazy 하게 동작하고, doseq 는 eager 하게 동작한다."

  (def lazy-list
    (for [elem '(1 2 3)]
      (do-it-slow elem)))
  (realized? lazy-list) ;; => false

  lazy-list ;; => (2 3 4)

  (def not-lazy-list
    (doseq [elem '(1 2 3)]
      (do-it-slow elem)))
  ;;
  )

(comment
  "dorun 과 doall.
  doall 은 lazeseq를 eager 하게 평가한다."

  "이렇게 하면 lazy 하게 돌아간다."
  (def foo (map slowly [1 2 3]))
  (realized? foo) ;; => false

  "그러나 doall 을 쓰면 eager 가 된다. (너무 오래 걸리므로 주석처리함)"
  ; (def bar (doall (map slowly [1 2 3])))
  ; (realized? bar)

  "dorun 도 doall 처럼 eager 하게 사용하지만, 결과로 nil 을 리턴한다.
  즉 dorun 은 lazy sequence 에 대해 사이드 이펙트를 일으키기 위해 사용한다."
  (def foobar (dorun (map println [1 2 3])))
  (nil? foobar) ;; => true
  )

(comment
  "다음과 같이 파일을 읽으면 한 번에 메모리에 다 올리므로 엄청 큰 파일이 주어지면 곤란하다."
  (def string-line-list
    (-> "/textfile"
        slurp
        (string/split #"\n")))

  "따라서 한 줄씩 처리하고 싶다면 slurp 말고 clojure.java.io.reader 와 line-seq 를 사용한다."
  (with-open [reader (io/reader "/textfile")]   ; with-open 은 리소스를 자동으로(finally) 닫아준다.
    (dorun
     (map println (line-seq reader))))
  ; line-seq 는 BufferedReader를 통해 lazy 하게 한 줄 한 줄 읽는다.
  ; map 도 lazy 하게 작동한다.
  ; dorun 은 eager.
  ; reduce 는 eager.
  )


"큰 파일에 대한 단어 카운트하기"

(defn split-by-space
  [line]
  (string/split line #" "))

(defn merge-sum-map
  [map1 map2]
  (merge-with + map1 map2))

(defn split-and-merge
  [map line]
  (merge-sum-map map
                 (frequencies (split-by-space line))))

(defn word-count
  "아주 큰 파일도 무사히 단어 카운트를 해내는 함수."
  [filename]
  (with-open [reader (io/reader filename)]
    (let [big-lazy-list (line-seq reader)]
      (doall
       (reduce (fn [acc-map line] (split-and-merge acc-map line))
               {}
               big-lazy-list)))))
