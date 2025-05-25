(ns tp2.lsystem)

;; Maneja las iteraciones de las reglas sobre el axioma
(defn expand-system-l [axiom rules iterations]
  (loop [current axiom
         n iterations]
    (if (zero? n)
      current
      (let [next (apply str
                        (mapcat (fn [c]
                                  (get rules (str c) [(str c)]))
                                current))]
        (recur next (dec n))))))