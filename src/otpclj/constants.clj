(ns otpclj.constants
    (:use [crypto.random :only [base64]]))

(def key-length 10)

(defn fake-constants [n]
    (let [element (base64 key-length)]
        (if (<= n 1)
            (list element)
            (cons (base64 key-length) (lazy-seq (fake-constants (dec n)))))))

(def PADDINGS (fake-constants 3))
(def KEYS (fake-constants 3))

