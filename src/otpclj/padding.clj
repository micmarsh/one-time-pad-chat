(ns otpclj.padding
    (:use [otpclj.utils :only [to-string]]))

(defn add-padding [padding message]
    (let [length (count message)
          pad (drop length padding)]
        (concat message pad)))

(defn remove-padding [pad msg]
  (loop [padding (reverse pad) 
         message (reverse msg)]
    (if (= (first padding) (first message))
        (recur (rest padding) (rest message))
        (-> message reverse to-string))))