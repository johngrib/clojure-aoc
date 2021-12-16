(ns aoc2018.problem-1-2)

(comment "
## 문제
https://adventofcode.com/2018/day/1

Part 2. 주어진 입력의 숫자를 더할 때마다 나오는 숫자 중, 처음으로 두 번 나오는 숫자를 리턴하시오.

예) +3, +3, +4, -2, -4 => 10

## 참고
위 예의 답이 10 이 나오는 것이 이상하게 느껴질 수 있는데,
주어진 숫자를 전부 더했는데도 답이 나오지 않는다면 해당 숫자열을 처음부터 다시 더해가야 한다.
따라서 예제의 `+3, +3, +4, -2, -4`는
           `+3, +3, +4, -2, -4 +3, +3, +4...`와 같이 순환시켜 더해가야 한다.
그러므로 문제를 풀 때 cycle 함수의 사용을 고려할 수 있다.
")

(defn solve-1-2
  "주어진 숫자열을 순서대로 덧셈하다 처음으로 두 번째로 나오는 수를 리턴합니다.
  https://adventofcode.com/2018/day/1 "
  ([numbers last-sum number-set]
   (let [next-sum (+ last-sum (first numbers))]
     (if (number-set next-sum)
       ; return
       next-sum
       (recur
         (rest numbers)
         next-sum
         (conj number-set next-sum))
       )))
  ([numbers] (solve-1-2 numbers 0 #{0}))
  )
