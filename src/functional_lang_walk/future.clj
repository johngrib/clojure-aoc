(ns functional-lang-walk.future)

(defn i-am-grut
  []
  (Thread/sleep 3000)
  (println "Hi")
  "I'm grut")

(comment
  "future의 사용"

  "3초 뒤에 Hi 를 출력한 다음, I'm grut 문자열을 리턴한다."
  (i-am-grut)

  "(별도의 스레드로 실행)
  3초 뒤에 Hi 를 출력한다.
  별도 스레드이므로 리턴값은 확인할 수 없다."
  (future (i-am-grut))

  "별도의 스레드로 수행한 함수의 리턴값을 받고 싶다면 @ 를 사용한다.
  @ 를 사용하면 future의 결과가 리턴될 때까지 block 된다."
  @(future (i-am-grut))
  )

(defn i-will-promise-you
  []
  (promise))

(def his-promise (i-will-promise-you))

(comment
  "promise의 사용"

  (type his-promise) ;; => clojure.core$promise$reify__8591

  "20초가 지난 후, his-promise 에 값이 채워진다."
  (future (Thread/sleep 20000)
          (deliver his-promise 7))

  "20초가 지나기 전의 his-promise 상태는 :not-delivered"
; #<Promise@c7d500d: :not-delivered>
  "20초가 지난 후의 his-promise 는 7"
; #<Promise@c7d500d: 7>
  )

(defn set-interval
  "callback 함수를 ms 시간이 흐를때마다 수행한다. 수행 횟수는 n-times로 제한된다.
  (future를 사용해 별도의 스레드에서 실행된다.)"
  [callback ms n-times]
  (future (dotimes [i n-times]
            (Thread/sleep ms)
            (callback))))

(comment
  "함수를 주기적으로 수행하기"

  "1초마다 hi 를 출력하는 것을 다섯 번 반복한다.
  결과는 hi hi hi hi hi"
  (set-interval #(println "hi") 1000 5)
  )
