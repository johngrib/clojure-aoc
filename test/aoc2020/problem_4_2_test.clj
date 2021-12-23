(ns aoc2020.problem-4-2-test
  (:require [clojure.test :refer :all]
            [aoc2020.problem-4-2 :refer :all]
            [clojure.java.io :as io]))

(deftest describe:number-string?
  (testing
    (is (true? (number-string? "1234")))
    (is (true? (number-string? "-1234")))
    (is (false? (number-string? nil)))
    (is (false? (number-string? 1234)))
    (is (false? (number-string? "foo")))
    (is (false? (number-string? [])))))

(deftest describe:valid-height?
  (testing
    (are [input expect]
      (= expect (valid-height? input))
      ; cm 인 경우
      "149cm" false, "150cm" true, "193cm" true, "194cm" false,
      ; in 인 경우
      "58in" false, "59in" true, "76in" true, "77in" false
      ; 그 외의 경우
      "" false, "180" false, "cm" false, "in" false)))

(deftest describe:validate-passport
  (testing
    (are [input-passport expect-invalid-fields]
      (= expect-invalid-fields (:invalid-keys (validate-passport input-passport)))

      ; :hgt가 없음
      {:iyr "2013", :ecl "amb", :cid "350", :eyr "2023", :pid "028048884", :hcl "#cfa07d", :byr "1929"}
      #{:hgt}

      ; :byr이 없음
      {:hcl "#cfa07d", :eyr "2025", :pid "166559648", :iyr "2011", :ecl "brn", :hgt "59in"}
      #{:byr}

      ; 잘못된 필드가 없음
      {:ecl "gry", :pid "860033327", :eyr "2020", :hcl "#fffffd", :byr "1937", :iyr "2017", :cid "147", :hgt "183cm"}
      nil
      )))


(deftest describe:solve-4-2
  (let [
        invalid-strings "eyr:1972 cid:100\nhcl:#18171d ecl:amb hgt:170 pid:186cm iyr:2018 byr:1926\n\niyr:2019\nhcl:#602927 eyr:1967 hgt:170cm\necl:grn pid:012533040 byr:1946\n\nhcl:dab227 iyr:2012\necl:brn hgt:182cm pid:021572410 eyr:2020 byr:1992 cid:277\n\nhgt:59cm ecl:zzz\neyr:2038 hcl:74454a iyr:2023\npid:3556412378 byr:2007"
        valid-strings "pid:087499704 hgt:74in ecl:grn iyr:2012 eyr:2030 byr:1980\nhcl:#623a2f\n\neyr:2029 ecl:blu cid:129 byr:1989\niyr:2014 pid:896056539 hcl:#a97842 hgt:165cm\n\nhcl:#888785\nhgt:164cm byr:2001 iyr:2015 cid:88\npid:545766238 ecl:hzl\neyr:2022\n\niyr:2010 hgt:158cm hcl:#b6652a ecl:blu byr:1944 eyr:2021 pid:093154719"
        sample-input-string (-> "aoc2020/input4-sample.txt" io/resource slurp)
        input-string (-> "aoc2020/input4.txt" io/resource slurp)]
    (testing
      (is (= 0 (solve-4-2 invalid-strings)))
      (is (= 4 (solve-4-2 valid-strings)))
      (is (= 2 (solve-4-2 sample-input-string)))
      (is (= 184 (solve-4-2 input-string))))))

(comment
  (run-tests))
