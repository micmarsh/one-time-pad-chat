(ns otpclj.chat
    (:use [otpclj.crypto :only [make-encryptor make-decryptor]]
          [otpclj.connection :only [connect!]]
          [clojure.core.async :only [go-loop <! chan put!]]))

(defn show-received [message]
    (println "they say:" message))

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
              :keys (rest keys)
              :paddings (rest paddings) 
              :incoming incoming
            })))

(defn append [string end]
    (if (= end (last string))
        string
        (str string end)))

(defn start-client [{:keys [paddings keys room server]}]
    (let [outgoing (chan)
          incoming (connect! (str (append server \/) room) outgoing)]
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
