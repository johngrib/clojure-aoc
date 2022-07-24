(ns brave-clojure.chapter9
  "https://www.braveclojure.com/concurrency/")

(comment
(future (Thread/sleep 4000)
        (println "I'll print after 4 seconds"))
(println "I'll print immediately")

(let [result (future (println "this prints once")
                     (+ 1 1))]
  (println "deref: " (deref result))
  (println "@: " result))
; this prints once
; deref:  2
; @:  #future[{:status :ready, :val 2} 0x1fb684bc]

"@ deref 때문에 3초를 기다린다."
(let [result (future (Thread/sleep 3000)
                     (+ 1 1))]
  (println "The result is: " @result)
  (println "It will be at least 3 seconds before I print"))
; The result is:  2
; It will be at least 3 seconds before I print

"10ms 내에 future가 값을 리턴하지 않으면 5를 리턴하라고 deref에 지시한다."
(deref (future (Thread/sleep 1000) 0) 10 5)

"realized?를 쓰면 실행을 마쳤는지 물어볼 수 있다."
(realized? (future (Thread/sleep 1000)))

(let [f (future)]
  @f
  (realized? f))

; 231쪽 - delays
"이렇게 지연을 걸어둘 수 있다."
(def jackson-5-delay
  (delay (let [message "Just call my name and I'll be there"]
           (println "First deref:" message)
           message)))

"이렇게 지연을 중지하고 작업을 실행한 결과를 요구할 수 있다.
처음 부를 때에만 평가가 실행되며, 두 번째 호출부터는 저장된 결과값을 리턴한다."
(force jackson-5-delay)

@jackson-5-delay
"역참조는 아무리 불러도 아래의 결과만 출력한다"
; "Just call my name and I'll be there"

; 지연을 쓰는 방법 하나.
"사진 공유 사이트에 얼굴 사진을 여러장 올릴 때,
처음으로 사진이 올라오는 순간에 앱이 알림을 주는 상황"
(def gimli-headshots ["serious.jpg" "fun.jpg" "playful.jpg"])
(defn email-user
  [email-address]
  (println "Sending headshot notification to" email-address))
(defn upload-document
  "Needs to be implemented"
  [headshot]
  true)
;             delay가 있으므로 바로 처리되지 않는다. 아래의 force 구문에 가야 처리된다.
(let [notify (delay (email-user "and-my-axe@gmail.com"))]
  (doseq [headshot gimli-headshots]
    (future (upload-document headshot)  ; 얼굴 사진을 업로드하고...
            (force notify))))           ; 이메일로 알림을 보낸다.

; 233쪽 - promise
"promise를 쓰면 task가 실행될 시기를 특정하지 않은 채 결과를 기대할 수 있다.
약속은 promise를 써서 만들 수 있고,
결과는 deliver로 전달한다.
결과값은 역참조로 얻는다."

(def my-promise (promise))
(deliver my-promise (+ 1 2))
@my-promise ;; => 3

"promise는 단 한번만 결과를 전달할 수 있다."

; 234쪽 - promise 예제
(def yak-butter-international
  {:store      "Yak Butter International"
   :price      90
   :smoothness 90})
(def butter-than-nothing
  {:store "Butter Than Nothing"
   :price 150
   :smoothness 83})
;; This is the butter that meets our requirements
(def baby-got-yak
  {:store      "Baby Got Yak"
   :price      94
   :smoothness 99})
(defn mock-api-call
  [result]
  (Thread/sleep 1000)
  result)
(defn satisfactory?
  [butter]
  (and (<= (:price butter) 100)
       (>= (:smoothness butter) 97)
       butter))

(comment
  (time (some (comp satisfactory? mock-api-call)
              [yak-butter-international
               butter-than-nothing
               baby-got-yak])))
; "Elapsed time: 3009.122875 msecs"
; {:store "Baby Got Yak", :price 94, :smoothness 99}

"만약 컴퓨터가 코어를 여러 개 갖고 있다면..."
(comment
  (time (let [butter-promise (promise)]
          (doseq [butter [yak-butter-international
                          butter-than-nothing
                          baby-got-yak]]
            ; 요청 하나하나를 future로 사용하고, 결과는 deliver로 할당한다.
            (future (if-let [satisfactory-butter (satisfactory? (mock-api-call butter))]
                      (deliver butter-promise satisfactory-butter))))
          (println "And the winner is:" @butter-promise))))
" 1초 밖에 안 걸린다"
; And the winner is: {:store Baby Got Yak, :price 94, :smoothness 99}
; "Elapsed time: 1003.276042 msecs"

; 235쪽
"promise에 시간제한 걸기"
(let [p (promise)]
  ; 100 ms 동안 대기하고, 값이 없으면 (deliver된 값이 없으면) timeout.
  (deref p 100 "timed out"))

; 236쪽 - 콜백 등록
(let [ferengi-wisdom-promise (promise)]
  (future (println "Here's some Ferengi wisdom:" @ferengi-wisdom-promise))
  (Thread/sleep 100)
  (deliver ferengi-wisdom-promise
           "Whisper your way to success."))
; Here's some Ferengi wisdom: Whisper your way to success.
; #<Promise@5d4624db: "Whisper your way to success.">

; 237쪽
(defmacro wait
  "Sleep `timeout` seconds before evaluating body"
  [timeout & body]
  `(do (Thread/sleep ~timeout) ~@body))

(time
 (let [saying3 (promise)]
   (future (deliver saying3 (wait 100 "Cheerio!")))
   @(let [saying2 (promise)]
      (future (deliver saying2 (wait 400 "Pip pip!")))
      @(let [saying1 (promise)]
         (future (deliver saying1 (wait 200 "'Ello, gov'na!")))
         (println @saying1)
         saying1)
      (println @saying2)
      saying2)
   (println @saying3)
   saying3))
"결과가 나오는데 402ms 가 걸렸다.
각 단계가 200, 400, 100 ms가 걸리는데도, future를 사용해 비동기로 실행했기 때문"
; 'Ello, gov'na!
; Pip pip!
; Cheerio!
; "Elapsed time: 402.873416 msecs"
; #<Promise@74b2e475: "Cheerio!">

"위의 코드를 매크로로 개선해보자"
(defmacro enqueue
  ([q concurrent-promise-name concurrent serialized]
   `(let [~concurrent-promise-name (promise)]
      (future (deliver ~concurrent-promise-name ~concurrent))
      (deref ~q)
      ~serialized
      ~concurrent-promise-name))
  ([concurrent-promise-name concurrent serialized]
   `(enqueue (future)
             ~concurrent-promise-name
             ~concurrent
             ~serialized)))
#_:clj-kondo/ignore
(time @(-> (enqueue saying (wait 200 "'Ello, gov'na!") (println @saying))
           (enqueue saying (wait 400 "Pip pip!") (println @saying))
           (enqueue saying (wait 100 "Cheerio!") (println @saying))))
; 'Ello, gov'na!
; Pip pip!
; Cheerio!
; "Elapsed time: 402.322875 msecs"
; "Cheerio!"

)
