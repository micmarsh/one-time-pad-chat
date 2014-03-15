(ns otpclj.core
    (:use [crypto.random :only [base64]]))

(def to-ints (partial map int))

(def KEY (-> 200 base64 to-ints))

(defn add-padding [message pad]
    (let [length (count message)
          padding (drop length pad)]
        (concat message padding)))

(defn remove-padding [message pad]
    (let [rmsg (reverse message)
          rpad (reverse pad)]))

(defn otp [key message]
    (let [padding (-> key count base64 to-ints)
          key-ints (to-ints key)
          msg-ints (to-ints message)
          full-msg (add-padding msg-ints padding)
          encrypted (map (comp char bit-xor) key-ints full-msg) ]
        (apply str encrypted)))

(defn -main []
    (let [message (read-line)
          encrypt (partial otp KEY)
          decrypt encrypt
          encrypted (encrypt message)]
        (println "your message:" message)
        (println "encrypted:" encrypted)
        (println "decrypted:" (decrypt encrypted))
        (recur)))
