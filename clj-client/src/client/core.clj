(ns client.core
  (:import java.util.Date)
  (:require [monotony.configured :as mc]))

(defn now []
  (/ (System/currentTimeMillis) 1000))

