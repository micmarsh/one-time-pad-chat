(defproject otpclj "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [crypto-random "1.2.0"]
                 [org.clojure/tools.cli "0.3.1"]]
   ;; uberjar
  :aot :all
  :omit-source true

  :main otpclj.core)
