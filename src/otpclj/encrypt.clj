(ns otpclj.encrypt
    (:use [otpclj.utils :only [to-string to-ints]]))

(def xor-all (partial map bit-xor))

(defn otp [key message]
    (let [key-ints (to-ints key)
          msg-ints (to-ints message)
          encrypted (xor-all key-ints msg-ints) ]
        (to-string encrypted)))
