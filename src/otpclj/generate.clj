(ns otpclj.generate)

; Need to generate both a fake set of keys AND a fake set of padding, just to be safe
; since having a single padding for use across all instances means they'll always be able
; to decode everything up to the first x letters of your shortest message. Keeping it random
; here also just seems helpful

(def key-length 10)
(def max-messages 100)

(defn fake-constants [n key-length]
    (let [element (base64 key-length)]
        (if (<= n 1)
            (list element)
            (cons (base64 key-length) 
                (lazy-seq (fake-constants (dec n) key-length))))))

(def keys (fake-constants max-messages key-length))
(def paddings (fake-constants max-messages key-length)

(def FILE_NAME "keys.txt")

(defn generate-constants [& [options]])
