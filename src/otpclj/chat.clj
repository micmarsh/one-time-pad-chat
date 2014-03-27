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

(defn start-receiver [arg-map]
    (go-loop [{:keys [keys paddings incoming]} arg-map]
        (let [message (<! incoming)
              decrypt (make-decryptor (first keys) (first paddings))]
          (-> message decrypt show-received))
            (recur {
              :keys keys
              :paddings paddings 
              :incoming incoming
            })))

(defn start-client [{:keys [paddings keys room]}]
    (let [outgoing (chan)
          incoming (connect! (str CHAT_SERVER room) outgoing)]
        (start-receiver {
            :keys keys
            :paddings paddings 
            :incoming incoming
          })

        (println (str "Welcome to OTPChat!\n"
                      "This is still very much alpha software, with plenty of bugs and UX/UI issues\n"
                      "Start typing to say hi to whoever's (possibly) on the other end.\n"))
        (main-loop {
          :keys keys
          :paddings paddings
          :outgoing outgoing
          })))
