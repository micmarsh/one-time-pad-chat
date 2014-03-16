(ns otpclj.crypto)

(def to-ints (partial map int))
(def to-string #(apply str (map char %)))

(defn add-padding [padding message]
    (let [length (count message)
          pad (drop length padding)]
        (-> message (concat pad) to-string)))

(defn remove-padding [pad msg]
  (loop [padding (reverse pad) 
         message (reverse msg)]
    (if (= (first padding) (first message))
        (recur (rest padding) (rest message))
        (-> message reverse to-string))))
        
(def xor-all (partial map bit-xor))

(defn otp [key message]
    (let [key-ints (to-ints key)
          msg-ints (to-ints message)
          encrypted (xor-all key-ints msg-ints) ]
        (to-string encrypted)))

(defn make-encryptor [key padding]
    (let [pad (partial add-padding padding)
          encrypt-base (partial otp key)]
        (comp encrypt-base pad)))

(defn make-decryptor [key padding]
    (let [unpad (partial remove-padding padding)
          encrypt-base (partial otp key)]
        (comp unpad encrypt-base)))