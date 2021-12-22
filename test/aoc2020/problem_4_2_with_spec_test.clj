(ns aoc2020.problem-4-2-with-spec-test
  (:require [clojure.test :refer :all])
  (:require [aoc2020.problem-4-2-with-spec :refer :all]
            [clojure.java.io :as io]))

(deftest describe:solve-4-2-with-spec
  (let [
        invalid-strings "eyr:1972 cid:100\nhcl:#18171d ecl:amb hgt:170 pid:186cm iyr:2018 byr:1926\n\niyr:2019\nhcl:#602927 eyr:1967 hgt:170cm\necl:grn pid:012533040 byr:1946\n\nhcl:dab227 iyr:2012\necl:brn hgt:182cm pid:021572410 eyr:2020 byr:1992 cid:277\n\nhgt:59cm ecl:zzz\neyr:2038 hcl:74454a iyr:2023\npid:3556412378 byr:2007"
        valid-strings "pid:087499704 hgt:74in ecl:grn iyr:2012 eyr:2030 byr:1980\nhcl:#623a2f\n\neyr:2029 ecl:blu cid:129 byr:1989\niyr:2014 pid:896056539 hcl:#a97842 hgt:165cm\n\nhcl:#888785\nhgt:164cm byr:2001 iyr:2015 cid:88\npid:545766238 ecl:hzl\neyr:2022\n\niyr:2010 hgt:158cm hcl:#b6652a ecl:blu byr:1944 eyr:2021 pid:093154719"
        sample-input-string (-> "aoc2020/input4-sample.txt" io/resource slurp)
        input-string (-> "aoc2020/input4.txt" io/resource slurp)]
    (testing ""
      (is (= 0 (solve-4-2-with-spec invalid-strings)))
      (is (= 4 (solve-4-2-with-spec valid-strings)))
      (is (= 2 (solve-4-2-with-spec sample-input-string)))
      (is (= 184 (solve-4-2-with-spec input-string))))))

(comment
  (run-tests))
