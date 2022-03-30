(ns brave-clojure.chapter6
  "https://www.braveclojure.com/organization/"
  (:require [clojure.string :as s])
  (:refer-clojure :exclude [min max]))

(def great-books ["East of Eden" "The Glass Bead Game"])
; #'brave-clojure.chapter6/great-books

(ns-interns *ns*)
; {great-books #'brave-clojure.chapter6/great-books}

(get (ns-interns *ns*) 'great-books)
; #'brave-clojure.chapter6/great-books

(deref #'brave-clojure.chapter6/great-books)
; ["East of Eden" "The Glass Bead Game"]

#_(def great-books ["The Power of Bees" "Journey to Upstairs"])
; ["The Power of Bees" "Journey to Upstairs"]

(create-ns 'cheese.taxonomy)

(defn comparator-over-maps
  [comparison-fn ks]
  (fn [maps]
    (zipmap ks
            (map (fn [k] (apply comparison-fn (map k maps)))
                 ks))))

(def min (comparator-over-maps clojure.core/min [:lat :lng]))
(def max (comparator-over-maps clojure.core/max [:lat :lng]))

(comment
  (min [{:a 1 :b 3} {:a 5 :b 0}])
  (zipmap [:a :b] [1 2]))

