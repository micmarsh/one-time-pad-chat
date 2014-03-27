(ns otpclj.core
    (:use [otpclj.chat :only [start-client]]
          [otpclj.generate :only [generate-constants]]
          [clojure.tools.cli :only [parse-opts]])
    (:gen-class))

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
   ["-r" "--room ROOMNAME" "Where to connect to (only 2 allowed per room)"
    :default "default"]
  ])

(def parse-args #(parse-opts % cli-options))

(defn generate? [{:keys [arguments]}]
    (some #{"generate"} arguments))

(defn read-file [name]
  (-> name slurp read-string))

(defn file-to-args [file]
    [(:keys file) (:paddings file)])

(defn chat-args [options]
  (try
    (let [filename (:file options)
          {:keys [keys paddings]} (read-file filename)]
        (assoc options :keys keys
                       :paddings paddings))
    (catch java.io.FileNotFoundException e 
        (println 
          (str "The the file \"" 
            (:file options) "\" doesn't exist")))))

(defn -main [& args]
  (let [arguments (parse-args args)
        options (:options arguments)]
    (if (generate? arguments)
      (-> options generate-constants prn)
      (-> options chat-args start-client))))
