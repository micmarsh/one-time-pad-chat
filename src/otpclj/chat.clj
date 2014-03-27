(ns otpclj.chat
    (:use [otpclj.crypto :only [make-encryptor make-decryptor]]
          [otpclj.connection :only [connect!]]
          [clojure.core.async :only [go-loop <! chan put!]]))

(defn show-received [message]
    (println "they say:" message))

(def CHAT_SERVER "ws://localhost:8080/chat/");"ws://chitty-chatty.herokuapp.com/chat/asss")

(defn main-loop [{:keys [keys paddings outgoing]}]
    (if (empty? keys)
        (println "you're all out of encryption keys!")
        (let [message (read-line)
              encrypt (make-encryptor (first keys) (first paddings))
              encrypted (encrypt message)]
            (put! outgoing encrypted)
            (recur {
              :keys (rest keys) 
              :paddings (rest paddings)
              :outgoing outgoing
            }))))

(defn start-loops [KEYS PADDINGS room]
    (let [outgoing (chan)
          incoming (connect! (str CHAT_SERVER room) outgoing)]

        (go-loop [keys KEYS paddings PADDINGS]
            (let [message (<! incoming)
                  decrypt (make-decryptor (first keys) (first paddings))]
                (-> message decrypt show-received))
            (recur (rest keys) (rest paddings)))

        (println (str "Welcome to OTPChat!\n"
                      "This is still very much alpha software, with plenty of bugs and UX/UI issues\n"
                      "Start typing to say hi to whoever's (possibly) on the other end.\n"))
        (main-loop {
          :keys KEYS
          :paddings PADDINGS
          :outgoing outgoing
          })))
