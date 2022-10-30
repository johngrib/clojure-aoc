(ns aoc2018.problem-8-1-test
  (:require [aoc2018.problem-8-1 :as sut]
            [clojure.test :refer [deftest is run-tests testing]]))

(deftest test:metadata-sum
  (testing "metadata-sum 함수는 트리의 모든 메타데이터와 그 합을 리턴한다."
    (is (= {:metadata        [10 11 12]
            :sum-of-metadata (reduce + [10 11 12])}
           (sut/metadata-sum [0 3, 10 11 12])))

    (is (= 43351
           (:sum-of-metadata (sut/metadata-sum (sut/data-file->number-list)))))))

(comment
  (run-tests)
  ;;
  )


