(ns aoc2020.problem-4-2
  (:require [aoc2020.problem-4-1 :refer [string->passport sample-input-string input-string]]
            [clojure.string :as str]))

(comment "
## 문제
https://adventofcode.com/2020/day/4 part2
여권 validate 문제

part1 문제에 추가로 각 필드별 유효조건이 붙었다.
각 필드별 조건은 다음과 같다.

- byr (Birth Year) - 4 자리 숫자; 최소 1920 & 최대 2002.
- iyr (Issue Year) - 4 자리 숫자; 최소 2010 & 최대 2020.
- eyr (Expiration Year) - 4 자리 숫자; 최소 2020 & 최대 2030.
- hgt (Height) - 마지막에 cm 혹은 in이 오는 숫자:
  - cm의 경우, 숫자는 최소 150 & 최대 193.
  - in의 경우, 숫자는 최소 59 & 최대 76.
- hcl (Hair Color) - #뒤에 오는 정확히 6개의 캐릭터 0-9 혹은 a-f.
- ecl (Eye Color) - 정확히 amb blu brn gry grn hzl oth 중 하나.
- pid (Passport ID) - 처음 0을 포함하는 9자리 숫자.
- cid (Country ID) - 없어도 됨.

모든 필드의 조건을 만족하는 여권들의 수가 문제의 답이다.
")

(defn number-string?
  "숫자 형식의 문자열이면 true, 그 외의 경우는 false를 리턴합니다."
  [str]
  (and
    (string? str)
    (some? (re-find #"^-?\d+$" str))))

(comment
  (number-string? "123")
  (number-string? nil))

(defn valid-height?
  "주어진 문자열이 올바른 신장을 표현한다면 true, 그렇지 않다면 false를 리턴합니다."
  [hgt-string]
  (let [[_ height unit] (re-find #"^(\d+)(in|cm)?$" hgt-string)]
    (if (or (nil? height) (nil? unit))                      ; (and height unit)
      false
      (let [size (parse-long height)]
        (case unit
          "cm" (<= 150 size 193)
          "in" (<= 59 size 76))))))

(comment
  (valid-height? "in")
  (valid-height? "180cm")
  (valid-height? "10in"))

(def validator
  "여권의 각 필드를 검증하기 위한 검증함수들의 리스트입니다. 각 검증함수 리스트는 short-circuit 평가를 염두에 두고 정렬되어 있습니다."
  {
   :byr [some?
         number-string?
         #(<= 1920 (parse-long %) 2002)]
   :iyr [some?
         number-string?
         #(<= 2010 (parse-long %) 2020)]
   :eyr [some?
         number-string?
         #(<= 2020 (parse-long %) 2030)]
   :hgt [some?
         valid-height?]
   :hcl [some?
         #(re-find #"^#[0-9a-f]{6}$" %)]
   :ecl [some?
         #(#{"amb" "blu" "brn" "gry" "grn" "hzl" "oth"} %)]
   :pid [some?
         number-string?
         #(re-find #"^\d{9}$" %)]
   :cid [(constantly true)]})

(defn check
  "검증함수 리스트를 순회하며 값을 검증합니다.
   검증에 실패하면 short-circuit으로 false를 리턴하며, 모든 검증에 성공하면 true를 리턴합니다."
  [checker-list value]
  (let [checker (first checker-list)]
    (cond
      (empty? checker-list) true
      (checker value) (recur (rest checker-list) value)
      :else false)))

(defn validate-passport
  "여권 정보를 검사하여, invalid한 값이 있는 경우 :invalid-keys 필드와 값을 추가한 여권 정보를 리턴합니다.
  :invalid-keys 는 set 으로 주어지며, 검증에 실패한 키값들이 수집됩니다."
  [passport]
  (let [invalid-keys (->> validator
                          (keep (fn [[key checker-list]]
                                  (when-not (check checker-list (key passport)) key))) ; keep is friend with when
                          set)]
    (if (empty? invalid-keys)
      passport
      (assoc passport :invalid-keys invalid-keys))))

(comment
  (validate-passport {:ecl "gry", :pid "860033327", :eyr "2020", :hcl "#fffffd", :byr "1937", :iyr "2017", :cid "147", :hgt "183cm"})
  (validate-passport {:iyr "2013", :ecl "amb", :cid "350", :eyr "2023", :pid "028048884", :hcl "#cfa07d", :byr "1929"})
  (validate-passport {:hcl "#ae17e1", :iyr "2013", :eyr "2024", :ecl "brn", :pid "760753108", :byr "1931", :hgt "179cm"})
  (validate-passport {:hcl "#cfa07d", :eyr "2025", :pid "166559648", :iyr "2011", :ecl "brn", :hgt "59in"})
  ;;
  )

(defn solve-4-2
  "https://adventofcode.com/2020/day/4 part 2 문제를 풀이하여, 유효한 여권의 수를 리턴합니다."
  [input-string]
  (->> (str/split input-string #"\n\n+")
       (map string->passport)
       (map validate-passport)
       (filter #(nil? (:invalid-keys %)))
       count))

(def invalid-strings "eyr:1972 cid:100\nhcl:#18171d ecl:amb hgt:170 pid:186cm iyr:2018 byr:1926\n\niyr:2019\nhcl:#602927 eyr:1967 hgt:170cm\necl:grn pid:012533040 byr:1946\n\nhcl:dab227 iyr:2012\necl:brn hgt:182cm pid:021572410 eyr:2020 byr:1992 cid:277\n\nhgt:59cm ecl:zzz\neyr:2038 hcl:74454a iyr:2023\npid:3556412378 byr:2007")
(def valid-strings "pid:087499704 hgt:74in ecl:grn iyr:2012 eyr:2030 byr:1980\nhcl:#623a2f\n\neyr:2029 ecl:blu cid:129 byr:1989\niyr:2014 pid:896056539 hcl:#a97842 hgt:165cm\n\nhcl:#888785\nhgt:164cm byr:2001 iyr:2015 cid:88\npid:545766238 ecl:hzl\neyr:2022\n\niyr:2010 hgt:158cm hcl:#b6652a ecl:blu byr:1944 eyr:2021 pid:093154719")

(comment
  (solve-4-2 invalid-strings)                               ; 0
  (solve-4-2 valid-strings)                                 ; 4
  (solve-4-2 sample-input-string)                           ; 2
  (solve-4-2 input-string)                                  ; 184
  ;;
  )
