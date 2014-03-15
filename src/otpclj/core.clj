(ns otpclj.core
    (:use [otpclj.constants :only [KEYS PADDING]]
          [otpclj.encrypt :only [otp]]
          [otpclj.padding :only [add-padding remove-padding]]))

(def pad (partial add-padding PADDING))
(def unpad (partial remove-padding PADDING))

(defn -main []
    (loop [keys KEYS]
        (if (empty? keys)
            (println "you're all out of encryption keys!")
            (let [encrypt-base (partial otp (first keys))
                  encrypt (comp encrypt-base pad)
                  decrypt (comp unpad encrypt-base)

                  message (read-line)
                  encrypted (-> message pad encrypt)
                  decrypted (-> encrypted decrypt unpad)]
                (println "your message:" message)
                (println "encrypted:" encrypted)
                (println "decrypted:" decrypted)
                (recur (rest keys))))))
