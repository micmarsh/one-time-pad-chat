(ns otpclj.connection
    (:use [clojure.core.async :only [chan put! go >! <! <!!]]))
(import [org.java_websocket.client WebSocketClient])

(defn make-sender [where incoming]
    (let [last-messsage (atom nil)
          done (chan)
          ^WebSocketClient socket (proxy [WebSocketClient] 
            [(java.net.URI. where)]
            (onOpen [data] 
                (println "opened socket!")
                (put! done true))
            (onClose [code reason remote] (println "closed socket because" reason))
            (onMessage [message]
                (when 
                    (not (= message @last-messsage)) 
                        (put! incoming message)))
            (onError [exception] (println "hey socket error")))]
        (.connect socket)
        (<!! done)
        (fn [message]
            (reset! last-messsage message)
            (.send socket message))))

(defn connect! [where outgoing]
    (let [incoming (chan)
          send! (make-sender where incoming)]
        (go (while true
            (let [message (<! outgoing)]
                (send! message))))
        incoming))
