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

(def encrypted (encrypt/otp "the key" "the msg"))

(fact "xor and otp work as expected"
    (encrypt/xor-all (range 10) (range 10)) => (->> 0 repeat (take 10))
    (encrypt/xor-all (range 9) (range 9 18)) => [9 11 9 15 9 11 9 23 25]
    (encrypt/otp "a key" "a msg") =not=> "a msg"
    (encrypt/otp "the key" encrypted) => "the msg"
    (encrypt/otp "key" "message") => (encrypt/otp "key" "mes"))

; (def encrypted1 (encrypt/otp "the key" "hey hey"))

; (fact "otp can be cracked if a key is re-used"
;     )
