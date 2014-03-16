(ns otpclj.chat
    (:use [otpclj.encrypt :only [otp]]
          [otpclj.padding :only [add-padding remove-padding]]
          [otpclj.connection :only [incoming send-message]]
          [clojure.core.async :only [go-loop <!]]))

(defn crypto-fns [current-key current-padding]
    (let [pad (partial add-padding current-padding)
          unpad (partial remove-padding current-padding)
          encrypt-base (partial otp current-key)
          encrypt (comp encrypt-base pad)
          decrypt (comp unpad encrypt-base)]
        {:encrypt encrypt :decrypt decrypt}))

(defn main-loop [KEYS PADDINGS]

    (go-loop [keys KEYS paddings PADDINGS]
        (let [message (<! incoming)
              {:keys [decrypt]}
                  (crypto-fns (first keys) (first paddings))]
            (->> message decrypt (println "they say:")))
        (recur (rest keys) (rest paddings)))

    (println "starting main loop!")
        (loop [keys KEYS paddings PADDINGS] 
            (if (empty? keys)
                (println "you're all out of encryption keys!")
                (let [{:keys [encrypt]} 
                            (crypto-fns (first keys) (first paddings))]
                      message (read-line)
                      encrypted (encrypt message)
                    (println "you say:" message)
                    (send-message encrypted)
                    (recur (rest keys) (rest paddings))))))