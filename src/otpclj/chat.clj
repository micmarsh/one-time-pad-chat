(ns otpclj.chat
    (:use [otpclj.encrypt :only [otp]]
          [otpclj.padding :only [add-padding remove-padding]]))

(defn main-loop [keys paddings]
    (if (empty? keys)
        (println "you're all out of encryption keys!")
        (let [current-padding (first paddings)
              pad (partial add-padding current-padding)
              unpad (partial remove-padding current-padding)

              encrypt-base (partial otp (first keys))
              encrypt (comp encrypt-base pad)
              decrypt (comp unpad encrypt-base)

              message (read-line)
              encrypted (encrypt message)
              decrypted (decrypt encrypted)]
            (println "your message:" message)
            (println "encrypted:" encrypted)
            (println "decrypted:" decrypted)
            (recur (rest keys) (rest paddings)))))