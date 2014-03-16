(ns otpclj.generate
    (:use [crypto.random :only [base64]]))

; TODO hook up to random.org like a baws

(defn make-str [length]
  (->> length
      base64
      (take length)
      (apply str)))

(defn fake-constants [n key-length]
    (let [element (make-str key-length)]
        (if (<= n 1)
            (list element)
            (cons (make-str key-length) 
                (lazy-seq (fake-constants (dec n) key-length))))))

(defn generate-constants [options]
    (let [{:keys [max-messages message-length]} options
          keys (fake-constants max-messages message-length)
          paddings (fake-constants max-messages message-length)]
        {:keys (vec keys)
         :paddings (vec paddings)}))
