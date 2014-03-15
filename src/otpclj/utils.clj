(ns otpclj.utils)

(def to-ints (partial map int))
(def to-string #(apply str (map char %)))