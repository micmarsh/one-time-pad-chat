(defproject otpclj "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [midje "1.6.3"]
                 [crypto-random "1.2.0"]
                 [org.clojure/tools.cli "0.3.1"]
                 [org.clojure/core.async "0.1.267.0-0d7780-alpha"]
                 [org.java-websocket/Java-WebSocket "1.3.0"]]

  :plugins [[lein-midje "3.0.0"]]

   ;; uberjar
  :uberjar-name "otp-chat.jar"
  :aot :all
  :omit-source true


  :main otpclj.core)
