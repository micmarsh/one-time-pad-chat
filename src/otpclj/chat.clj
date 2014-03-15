(ns otpclj.chat
    (:use [otpclj.encrypt :only [otp]]
          [otpclj.padding :only [add-padding remove-padding]]))

(defn crypto-fns [current-key current-padding]
    (let [pad (partial add-padding current-padding)
          unpad (partial remove-padding current-padding)
          encrypt-base (partial otp current-key)
          encrypt (comp encrypt-base pad)
          decrypt (comp unpad encrypt-base)]
        {:encrypt encrypt :decrypt decrypt}))

(defn main-loop [keys paddings]
    (if (empty? keys)
        (println "you're all out of encryption keys!")
        (let [{:keys [encrypt decrypt]} (crypto-fns (first keys) (first paddings))
              message (read-line)
              encrypted (encrypt message)
              decrypted (decrypt encrypted)]
            (println "your message:" message)
            (println "encrypted:" encrypted)
            (println "decrypted:" decrypted)
            (recur (rest keys) (rest paddings)))))