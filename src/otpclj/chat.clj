(ns otpclj.chat
    (:use [otpclj.crypto :only [make-encryptor make-decryptor]]
          [otpclj.connection :only [incoming send-message]]
          [clojure.core.async :only [go-loop <!]]))

(defn main-loop [KEYS PADDINGS]

    (go-loop [keys KEYS paddings PADDINGS]
        (let [message (<! incoming)
              decrypt (make-decryptor (first keys) (first paddings))]
            (->> message decrypt (println "they say:")))
        (recur (rest keys) (rest paddings)))

    (println "starting main loop!")
        (loop [keys KEYS paddings PADDINGS] 
            (if (empty? keys)
                (println "you're all out of encryption keys!")
                (let [encrypt (make-encryptor (first keys) (first paddings))
                      message (read-line)
                      encrypted (encrypt message)]
                    (send-message encrypted)
                    (recur (rest keys) (rest paddings))))))