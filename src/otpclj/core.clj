(ns otpclj.core
    (:use [crypto.random :only [base64]]
          [otpclj.encrypt :only [otp]]
          [otpclj.padding :only [add-padding remove-padding]]))

(def KEY (base64 200))
(def PADDING (base64 200))


(def pad (partial add-padding PADDING))
(def unpad (partial remove-padding PADDING))

(def encrypt-base (partial otp KEY))
(def encrypt (comp encrypt-base pad))
(def decrypt (comp unpad encrypt-base))

(defn -main []
    (let [message (read-line)
          encrypted (-> message pad encrypt)
          decrypted (-> encrypted decrypt unpad)]
        (println "your message:" message)
        (println "encrypted:" encrypted)
        (println "decrypted:" decrypted)
        (recur)))
