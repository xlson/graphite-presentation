(ns client.core
  (:import java.util.Date))

(defn now []
  (int (/ (System/currentTimeMillis) 1000)))

(def units {:seconds 1
            :minutes 60
            :hours   3600})

(defn in-seconds [n unit]
  (* n (units unit)))

(defn every [n unit]
  (let [seconds (in-seconds n unit)]
    (iterate (partial + seconds) 0)))

(defn before [instant n unit]
  (- instant (in-seconds n unit)))

(defn from-onward [start s]
  (map (partial + start) s))

(defn metric [name f]
  (fn [timestamp]
    [name (f) timestamp]))

(defn flat-line [value]
  (repeat value))

(defn constantly-increasing
  ([]
      (constantly-increasing 0 1))
  ([by]
     (constantly-increasing 0 by))
  ([start by]
     (iterate (partial + by) start)))


