(ns aoc2020.problem-4-1
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [clojure.set :as set]))

(comment "
## 문제
https://adventofcode.com/2020/day/4 part1
여권 validate 문제

검사해야 할 여권의 각 필드는 다음과 같다.

- byr (Birth Year)
- iyr (Issue Year)
- eyr (Expiration Year)
- hgt (Height)
- hcl (Hair Color)
- ecl (Eye Color)
- pid (Passport ID)
- cid (Country ID)

cid 는 필수 필드가 아니다. 나머지 7개 필드가 모두 있다면 유효한 여권으로 판별한다.
주어진 문자열을 읽고 유효한 여권의 수를 찾는 것이 문제이다.
")

(def sample-input-string (-> "aoc2020/input4-sample.txt" io/resource slurp))
(def input-string (-> "aoc2020/input4.txt" io/resource slurp))

(defn hgt-to-height
  "여권의 키 정보를 map으로 만들어 리턴해줍니다.
  예) 160cm => {:height 160, :unit 'cm'}"
  [hgt-string]
  (let [[_ height unit] (re-find #"^(\d+)(in|cm)?$" hgt-string)]
    {:height (Integer/parseInt height), :unit unit}))

(comment
  (hgt-to-height "160cm")
  (hgt-to-height "50in"))


(defn string->passport
  "여권 데이터를 표현하는 문자열을 여권 map으로 만들어 리턴합니다."
  [passport-string]
  (->> passport-string
       (re-seq #"([^\s\n]+):([^\s\n]+)")
       (map (fn [[_ k v]] {(keyword k) v}))
       (into {}))) ; (into {})

(comment
  (string->passport "ecl:gry pid:860033327 eyr:2020 hcl:#fffffd\nbyr:1937 iyr:2017 cid:147 hgt:183cm"))

(def required-passport-keywords #{:ecl :pid :eyr :hcl :byr :iyr :hgt})

(defn valid-passport?
  "유효한 여권이라면 true, 그렇지 않은 여권이라면 false를 리턴합니다."
  [passport]
  (set/subset? required-passport-keywords (set (keys passport))))

(comment
  ; true
  (valid-passport? {:ecl "gry", :pid "860033327", :eyr "2020", :hcl "#fffffd", :byr "1937", :iyr "2017", :cid "147", :hgt {:height 183, :unit "cm"}})
  ; false - hgt 누락
  (valid-passport? {:iyr "2013", :ecl "amb", :cid "350", :eyr "2023", :pid "028048884", :hcl "#cfa07d", :byr "1929"})
  ; true - cid는 필수 필드가 아니므로 누락되어도 괜찮음
  (valid-passport? {:hcl "#ae17e1", :iyr "2013", :eyr "2024", :ecl "brn", :pid "760753108", :byr "1931", :hgt {:height 179, :unit "cm"}})
  ; false - byr 누락
  (valid-passport? {:hcl "#cfa07d", :eyr "2025", :pid "166559648", :iyr "2011", :ecl "brn", :hgt {:height 59, :unit "in"}}))

(defn solve-4-1
  "https://adventofcode.com/2020/day/4 part 1 문제를 풀이합니다.
  여러 개의 여권 정보를 표현하는 문자열을 받아, 적합한 여권이 몇 개인지를 세어 리턴합니다."
  [input-string]
  (let [list-of-passport-strings (str/split input-string #"\n\n+")
        passports (map string->passport list-of-passport-strings)]
    (->> passports
         (map valid-passport?)  ; filter
         (filter true?)
         count)))

(comment
  (solve-4-1 sample-input-string)                           ; 2
  (solve-4-1 input-string))                                  ; 254
  ;; clojure.spec 미션
  ;; https://clojure.org/guides/spec 사용
  ;; https://www.youtube.com/watch?v=YR5WdGrpoug

