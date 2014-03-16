(ns otpclj.crypto-test
  (:use midje.sweet)               
  (:require [otpclj.utils :as utils]
            [otpclj.padding :as padding]
            [otpclj.encrypt :as encrypt]))

(fact "to-ints converts sequences into integers"
    (utils/to-ints "abcd") => (range 97 101)
    (utils/to-ints (range 3)) => [0 1 2]
    (utils/to-ints [1.0 2.0 3.0]) => [1 2 3])

(fact "to-string converts a sequence into a string"
    (utils/to-string (range 97 101)) => "abcd"
    (utils/to-string [\A \B \C]) => "ABC")

(def padding-id (comp
    (partial padding/remove-padding "the best padding")
    (partial padding/add-padding "the best padding")))

(fact "relevant padding can be added and removed"
    (padding/add-padding "padding" "runn") => "running"
    (padding/remove-padding "padding" "running") => "runn"
    (padding-id "word") => "word"
    (padding-id (range 5)) => (-> 5 range utils/to-string))
