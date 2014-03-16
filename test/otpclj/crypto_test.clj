(ns otpclj.crypto-test
  (:use midje.sweet)               
  (:require [otpclj.crypto :as c]))

(fact "to-ints converts sequences into integers"
    (c/to-ints "abcd") => (range 97 101)
    (c/to-ints (range 3)) => [0 1 2]
    (c/to-ints [1.0 2.0 3.0]) => [1 2 3])

(fact "to-string converts a sequence into a string"
    (c/to-string (range 97 101)) => "abcd"
    (c/to-string [\A \B \C]) => "ABC")

(def padding-id (comp
    (partial c/remove-padding "the best padding")
    (partial c/add-padding "the best padding")))

(fact "relevant padding can be added and removed"
    (c/add-padding "padding" "runn") => "running"
    (c/remove-padding "padding" "running") => "runn"
    (padding-id "word") => "word"
    (padding-id (range 5)) => (-> 5 range c/to-string))

(def encrypted (c/otp "the key" "the msg"))

(fact "xor and otp work as expected"
    (c/xor-all (range 10) (range 10)) => (->> 0 repeat (take 10))
    (c/xor-all (range 9) (range 9 18)) => [9 11 9 15 9 11 9 23 25]
    (c/otp "a key" "a msg") =not=> "a msg"
    (c/otp "the key" encrypted) => "the msg"
    (c/otp "key" "message") => (c/otp "key" "mes"))

; (def encrypted1 (c/otp "the key" "hey hey"))

; (fact "otp can be cracked if a key is re-used"
;     )
