(ns experiments.pipeline)

; 함수 목록을 제공해서 파이프라인을 구성한다.
; 만약 :done 참으로 평가받으면 파이프라인을 중지하고, :value 를 리턴한다.

(defn pipeline [input f-list]
  (reduce (fn [prev f]
            (if (:done prev)
              (reduced (:value prev))
              (f prev)))
          input
          f-list))

;; 스레딩 매크로처럼 사용하는 방법
(pipeline
  100
  [(partial + 10)
   (partial * 2)
   (partial * -1)])

;; 중간에 중지가 가능한 방법
(pipeline
  100
  [(fn [input] (+ 10 input))
   (fn [input] (* 2 input))
   (fn [input] (* -1 input))
   (fn [input]
     (if (< input 0)
       {:done  true
        :value input}
       (* -1 input)))
   (fn [input] (+ 2000 input))])
