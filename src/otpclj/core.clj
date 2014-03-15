(ns otpclj.core
    (:use [crypto.random :only [base64]]
          [otpclj.encrypt :only [otp]]
          [otpclj.utils :only [to-ints]]
          [otpclj.padding :only [add-padding remove-padding]]))

(def KEY (-> 200 base64 to-ints))
(def PADDING (-> 200 base64 to-ints))

(def encrypt (partial otp KEY))
(def decrypt encrypt)

(def pad (partial add-padding PADDING))
(def unpad (partial remove-padding PADDING))

(defn -main []
    (let [message (read-line)
          encrypted (encrypt message)
          decrypted (-> encrypted decrypt unpad)]
        (println "your message:" message)
        (println "encrypted:" encrypted)
        (println "decrypted:" decrypted)
        (recur)))
