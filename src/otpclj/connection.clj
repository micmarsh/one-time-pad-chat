(ns otpclj.connection
    (:use [clojure.core.async :only [chan put!]]))
(import [org.java_websocket.client WebSocketClient])

(def CHAT_SERVER "ws://chitty-chatty.herokuapp.com/chat/asss")

(def incoming (chan))

(def last-messsage (atom nil))

(def socket
    (proxy [WebSocketClient] 
        [(java.net.URI. CHAT_SERVER)]
        (onOpen [data] (println "opened socket!"))
        (onClose [code reason remote] (println "closed socket because" reason))
        (onMessage [message]
            (when 
                (not (= message @last-messsage)) 
                    (put! incoming message)))
        (onError [exception] (println "hey socket error"))))

(.connect socket)

(defn send! [^WebSocketClient client message]
    (reset! last-messsage message)
    (.send client message))

(def send-message (partial send! socket))


