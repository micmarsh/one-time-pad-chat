(ns otpclj.chat
    (:use [otpclj.crypto :only [make-encryptor make-decryptor]]
          [otpclj.connection :only [connect!]]
          [clojure.core.async :only [go-loop <! chan put!]]))

(defn show-received [message]
    (println "they say:" message))

(def CHAT_SERVER "ws://chitty-chatty.herokuapp.com/chat/asss")

(defn main-loop [KEYS PADDINGS]
    (let [outgoing (chan)
          incoming (connect! CHAT_SERVER outgoing)]

        (go-loop [keys KEYS paddings PADDINGS]
            (let [message (<! incoming)
                  n (println "decrypting with:" (first keys) )
                  decrypt (make-decryptor (first keys) (first paddings))]
                (-> message decrypt show-received))
            (recur (rest keys) (rest paddings)))
        
        (println "starting main loop!")

        (loop [keys KEYS paddings PADDINGS] 
            (if (empty? keys)
                (println "you're all out of encryption keys!")
                (let [n (println "encrypting with:" (first keys) )

                      encrypt (make-encryptor (first keys) (first paddings))
                      message (read-line)
                      encrypted (encrypt message)]
                    (put! outgoing message)
                    (recur (rest keys) (rest paddings)))))))
