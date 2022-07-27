(ns functional-lang-walk.atom
  "atom, agent 사용 방법")

(def x (atom 100))

(comment
  "atom의 값을 참조하는 방법은 두 가지"

  "첫번째 방법: deref를 사용한다."
  (deref x) ;; => 100

  "두 번째 방법: @ 를 사용한다."
  @x ;; => 100
  (+ 1 @x) ;; => 101
  )

(comment
  "atom의 값을 갱신하려면 swap! 을 사용한다.
  atom의 값 갱신은 동기적으로 이루어진다."
  
  (swap! x
         #(+ % 1)) ;; => 102
  )

(def y (agent 100))

(comment
  "agent는 atmm과 비슷한 느낌으로 사용할 수 있다."
  (deref y) ;; => 100

  @y ;; => 100
  (+ 1 @y) ;; => 101

  "그러나 값 갱신은 swap! 이 아니라 send 를 쓴다."
  (send y inc)
  (deref y) ;; => 101

  "atom에 대한 갱신은 동기적으로 수행된다.
  그러나 agent에 대한 갱신은 비동기적으로 수행된다.
  따라서 send 함수는 block 되지 않고 바로 리턴된다."
  )
