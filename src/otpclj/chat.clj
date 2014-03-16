(ns otpclj.chat
    (:use [otpclj.encrypt :only [otp]]
          [otpclj.padding :only [add-padding remove-padding]]
          [otpclj.connection :only [make-messenger]]
          [clojure.core.async :only [go-loop <!]]))

(defn crypto-fns [current-key current-padding]
    (let [pad (partial add-padding current-padding)
          unpad (partial remove-padding current-padding)
          encrypt-base (partial otp current-key)
          encrypt (comp encrypt-base pad)
          decrypt (comp unpad encrypt-base)]
        {:encrypt encrypt :decrypt decrypt}))

(def send-message (make-messenger println "asss"))

(defn main-loop [KEYS PADDINGS]
    (println "starting main loop!")
        (loop [keys KEYS paddings PADDINGS] 
            (if (empty? keys)
                (println "you're all out of encryption keys!")
                (let [{:keys [encrypt decrypt]} 
                            (crypto-fns (first keys) (first paddings))
                      message (read-line)
                      encrypted (encrypt message)
                      decrypted (decrypt encrypted)]
                    (println "your message:" message)
                    (println "encrypted:" encrypted)
                    (println "decrypted:" decrypted)
                    (send-message encrypted)
                    (recur (rest keys) (rest paddings))))))