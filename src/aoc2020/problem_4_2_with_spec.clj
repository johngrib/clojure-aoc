(ns aoc2020.problem-4-2-with-spec
  (:require [aoc2020.problem-4-1 :refer :all]
            [aoc2020.problem-4-2 :refer :all]
            [clojure.spec.alpha :as s]
            [clojure.string :as str]))

(s/def :passport/byr (s/and
                       some?
                       number-string?
                       #(<= 1920 (Integer/parseInt %) 2002)))
(s/def :passport/iyr (s/and
                       some?
                       number-string?
                       #(<= 2010 (Integer/parseInt %) 2020)))
(s/def :passport/eyr (s/and
                       some?
                       number-string?
                       #(<= 2020 (Integer/parseInt %) 2030)))
(s/def :passport/hgt (s/and
                       some?
                       valid-height?))
(s/def :passport/hcl (s/and
                       some?
                       #(re-matches #"^#[0-9a-f]{6}$" %)))
(s/def :passport/ecl (s/and
                       some?
                       #{"amb" "blu" "brn" "gry" "grn" "hzl" "oth"}))
(s/def :passport/pid (s/and
                       some?
                       number-string?
                       #(re-matches #"^\d{9}$" %)))
(s/def :passport/cid (constantly true))

(s/def :unq/passport (s/keys :req-un [:passport/byr
                                      :passport/iyr
                                      :passport/eyr
                                      :passport/hgt
                                      :passport/hcl
                                      :passport/ecl
                                      :passport/pid]
                             :opt-un [:passport/cid]))

(defn solve-4-2-with-spec
  "https://adventofcode.com/2020/day/4 part 2 문제를 풀이하여, 유효한 여권의 수를 리턴합니다."
  [input-string]
  (->> (str/split input-string #"\n\n+")
       (map string->passport)
       (filter #(s/valid? :unq/passport %))
       count))

(comment
  (solve-4-2-with-spec invalid-strings)                     ; 0
  (solve-4-2-with-spec valid-strings)                       ; 4
  (solve-4-2-with-spec sample-input-string)                 ; 2
  (solve-4-2-with-spec input-string)                        ; 184
  )
