(ns otpclj.core
    (:use [otpclj.constants :only [KEYS PADDINGS]]
          [otpclj.encrypt :only [otp]]
          [otpclj.padding :only [add-padding remove-padding]]))

(defn main-loop []
    (loop [keys KEYS
           paddings PADDINGS]
        (if (empty? keys)
            (println "you're all out of encryption keys!")
            (let [current-padding (first paddings)
                  pad (partial add-padding current-padding)
                  unpad (partial remove-padding current-padding)

                  encrypt-base (partial otp (first keys))
                  encrypt (comp encrypt-base pad)
                  decrypt (comp unpad encrypt-base)

                  message (read-line)
                  encrypted (-> message pad encrypt)
                  decrypted (-> encrypted decrypt unpad)]
                (println "your message:" message)
                (println "encrypted:" encrypted)
                (println "decrypted:" decrypted)
                (recur (rest keys) (rest paddings))))))

(def -main main-loop)
