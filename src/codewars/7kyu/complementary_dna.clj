(ns codewars.7kyu.complementary-dna)

(comment "
https://www.codewars.com/kata/554e4a2f232cdd87d9000038/train/clojure

주어진 DNA의 짝 DNA를 리턴하는 함수를 작성하세요.
각 symbol의 짝으로 구성된 DNA를 생성해 리턴하면 됩니다.
각 symbol의 짝은 (A, T), (C, G) 입니다.
")

(defn dna-strand [dna]
  (->> dna
       (map {\A \T, \T \A, \C \G, \G \C})
       (apply str)))

(comment
  (dna-strand "ATTGC")
  (dna-strand "GTAT"))
