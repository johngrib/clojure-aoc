(ns aoc2020.problem-4-1-test
  (:require [aoc2020.problem-4-1 :refer [hgt:string->height:map string->passport valid-passport? solve-4-1 sample-input-string input-string]]
            [clojure.test :refer [deftest testing is are run-tests]]))

(deftest describe:hgt-to-height
  (testing "hgt-to-height 함수는
           입력된 신장 문자열을 map으로 만들어 리턴합니다."
    (are [height-string expect-result]
         (= expect-result
            (hgt:string->height:map height-string))
      "160cm" {:height 160, :unit "cm"}
      "52in" {:height 52, :unit "in"})))

(deftest describe:to-passport
  (testing "to-passport 함수는 여권 데이터를 표현하는 문자열을 받아 여권 map으로 변환해 리턴합니다."
    (are [passport-string passport]
         (= (string->passport passport-string)
            passport)
      "ecl:gry pid:860033327 eyr:2020 hcl:#fffffd\nbyr:1937 iyr:2017 cid:147 hgt:183cm"
      {:ecl "gry"
       :pid "860033327"
       :eyr "2020"
       :hcl "#fffffd"
       :byr "1937"
       :iyr "2017"
       :cid "147"
       :hgt "183cm"}

      "hcl:#ae17e1 iyr:2013 eyr:2024 ecl:brn pid:760753108 byr:1931 hgt:179cm"
      {:hcl "#ae17e1"
       :iyr "2013"
       :eyr "2024"
       :ecl "brn"
       :pid "760753108"
       :byr "1931"
       :hgt "179cm"})))

(deftest describe:valid-passport?
  (testing "valid-passport? 함수는
           유효한 여권이면 true를 리턴하고, 그렇지 않으면 false를 리턴합니다."
    (testing "유효한 여권이면 true를 리턴합니다."
      (is (valid-passport? {:ecl "gry"
                            :pid "860033327"
                            :eyr "2020"
                            :hcl "#fffffd"
                            :byr "1937"
                            :iyr "2017"
                            :cid "147"
                            :hgt {:height 183 :unit "cm"}}))

      (is (valid-passport? {:hcl "#ae17e1"
                            :iyr "2013"
                            :eyr "2024"
                            :ecl "brn"
                            :pid "760753108"
                            :byr "1931"
                            :hgt {:height 179 :unit "cm"}})))

    (testing "유효하지 않은 여권이면 false를 리턴합니다."
      (is (false? (valid-passport? {:iyr "2013"
                                    :ecl "amb"
                                    :cid "350"
                                    :eyr "2023"
                                    :pid "028048884"
                                    :hcl "#cfa07d"
                                    :byr "1929"})))
      (is (false? (valid-passport? {:hcl "#cfa07d"
                                    :eyr "2025"
                                    :pid "166559648"
                                    :iyr "2011"
                                    :ecl "brn"
                                    :hgt {:height 59 :unit "in"}}))))))

(deftest describe:solve-4-1
  (testing "solve-4-1 함수는"
    (testing "https://adventofcode.com/2020/day/4 에 올라온 part1 예제를 제공하면 정답 결과를 리턴한다"
      (is (= 2
             (solve-4-1 sample-input-string))))

    (testing "https://adventofcode.com/2020/day/4 에 올라온 part1 입력을 제공하면 정답 결과를 리턴한다"
      (is (= 254
             (solve-4-1 input-string))))))

(comment
  (run-tests)
  ;;
  )

