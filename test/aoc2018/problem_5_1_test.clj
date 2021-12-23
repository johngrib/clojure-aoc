(ns aoc2018.problem-5-1-test
  (:require [clojure.test :refer :all])
  (:require [aoc2018.problem-5-1 :refer :all]))

(deftest describe:pair-alphabet?
  (testing
    (are [char1 char2 expect-result]
      (= expect-result (pair-alphabet? char1 char2))
      \a \A true, \A \a true,
      \z \Z true, \Z \z true,

      \a \a false, \A \A false,
      \z \z false, \Z \Z false,
      \a \b false, \a \B false,
      \c \a false, \D \E false)))

(deftest describe:react-polymer-reducer
  (testing "무시할 문자 set이 주어지지 않으면,
  소문자-대문자 매칭을 통해 반응시키는 reducer를 리턴한다."
    (let [test-subject (react-polymer-reducer)]
      ""
      (are [입력-문자열 예상되는-반응-결과]
        (= (reduce test-subject [] 입력-문자열) 예상되는-반응-결과)
        [] [],
        (seq "aA") [],
        (seq "aAb") (seq "b"),
        (seq "aAbcCB") []
        (seq "aAbBc") (seq "c"),
        (seq "dabAcCaCBAcCcaDA") (seq "dabCBAcaDA"))))
  (testing "무시할 문자 set이 주어지면
  주어진 문자들을 무시하고, 소문자-대문자 매칭을 통해 반응시키는 reducer를 리턴한다."
    (are [무시할-문자-set 입력-문자열 예상되는-반응-결과]
      (= (reduce (react-polymer-reducer 무시할-문자-set) [] 입력-문자열) 예상되는-반응-결과)
      #{\a} (seq "a") [],
      #{\A \a} [] [],
      #{\b \B} (seq "abA") [],
      #{\c \C} (seq "acccAbC") (seq "b"),
      #{\z \Z} (seq "aAbZcCB") []
      #{\f \F \e} (seq "aeAbBc") (seq "c"),
      #{} (seq "dabAcCaCBAcCcaDA") (seq "dabCBAcaDA"))))

(deftest describe:solve-5-1
  "https://adventofcode.com/2018/day/5 part 1 문제를 풀이하여, 반응이 끝난 후의 문자열 길이를 리턴합니다."
  (testing
    (is (= 10 (solve-5-1 sample-input-string)))
    (is (= 9686 (solve-5-1 input-string)))))

(comment
  (run-tests))
