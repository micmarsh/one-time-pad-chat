(ns otpclj.connection-test
  (:use midje.sweet
        [clojure.core.async :only [chan put! take! <!!]])               
  (:require [otpclj.connection :as c]))

(def ECHO_URL "ws://echo.websocket.org")

; (def outgoing (chan))

; (def echo (c/connect! ECHO_URL outgoing))

; (put! outgoing "hey")
; (put! outgoing "what's up")

; (fact "reads things back from echo"
;     (<!! echo) => "hey"
;     (<!! echo) => "what's up")
;     