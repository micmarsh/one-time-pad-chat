(ns otpclj.core
    (:use [otpclj.constants :only [KEYS PADDINGS]]
          [otpclj.chat :only [main-loop]]
          [otpclj.generate :only [generate-constants]]
          [clojure.tools.cli :only [parse-opts]]))

(def parse-int #(Integer/parseInt %))

(def cli-options
  [[ "-l" "--message-length LENGTH" "The length of a chat message, used for generating keys + paddings"
    :default 200
    :parse-fn parse-int]
   [ "-m" "--max-messages NUMBER" "The maximum amount of messages this set of keys will be allowed to send, used for generating keys and paddings"
    :default 1000
    :parse-fn parse-int]
   ["-f" "--file FILENAME" "The name of the file containing the keys and paddings"
    :default "keys.txt"]
  ])

(defn generate? [{:keys [arguments]}]
    (some #{"generate"} arguments))

(def parse-args #(parse-opts % cli-options))

(defn -main [& args]
  (let [arguments (parse-args args)]
    (if (generate? arguments)
      (-> arguments :options generate-constants prn)
      (main-loop KEYS PADDINGS))))
