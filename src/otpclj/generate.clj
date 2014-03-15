(ns otpclj.generate
    (:use [crypto.random :only [base64]]))

; TODO hook up to random.org like a baws

(defn fake-constants [n key-length]
    (let [element (base64 key-length)]
        (if (<= n 1)
            (list element)
            (cons (base64 key-length) 
                (lazy-seq (fake-constants (dec n) key-length))))))

(defn generate-constants [options]
    (let [{:keys [max-messages message-length]} options
          keys (fake-constants max-messages message-length)
          paddings (fake-constants max-messages message-length)]
        {:keys (vec keys)
         :paddings (vec paddings)}))
