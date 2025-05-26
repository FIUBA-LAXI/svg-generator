(ns tp2.turtle
  (:import [java.lang Math]))

(defn interpretar [cadena angulo]
  (let [rads (fn [deg] (* (/ Math/PI 180) deg))]
    (loop [xs []                                      ;; líneas acumuladas
           estado {:x 0.0 :y 0.0 :dir -90.0}            ;; estado actual de la tortuga
           pila []                                    ;; pila de estados
           extremos {:min-x 0.0 :max-x 0.0
                     :min-y 0.0 :max-y 0.0}           ;; extremos
           [c & cs] (seq cadena)]                     ;; comandos por procesar
      (if (nil? c)
        {:lineas xs :extremos extremos}
        (let [{:keys [x y dir]} estado
              θ (rads dir)
              actualizar-extremos
              (fn [ext x y]
                {:min-x (min (:min-x ext) x)
                 :max-x (max (:max-x ext) x)
                 :min-y (min (:min-y ext) y)
                 :max-y (max (:max-y ext) y)})]
          (case c
            (\F \G)
            (let [x2 (+ x (* 10.0 (Math/cos θ))) ;; Multiplicamos por 10.0 para dar un paso más largo
                  y2 (+ y (* 10.0 (Math/sin θ)))
                   nuevos-extremos (-> extremos
                                      (actualizar-extremos x2 y2))]
              (recur (conj xs [[x y] [x2 y2]])
                     {:x x2 :y y2 :dir dir}
                     pila
                     nuevos-extremos
                     cs))

            (\f \g)
            (let [x2 (+ x (* 10.0 (Math/cos θ)))
                  y2 (+ y (* 10.0 (Math/sin θ)))
                   nuevos-extremos (-> extremos
                                      (actualizar-extremos x2 y2))]
              (recur xs
                     {:x x2 :y y2 :dir dir}
                     pila
                     nuevos-extremos
                     cs))

            \+
            (recur xs
                   (update estado :dir #(+ % angulo))
                   pila
                   extremos
                   cs)

            \-
            (recur xs
                   (update estado :dir #(- % angulo))
                   pila
                   extremos
                   cs)

            \|
            (recur xs
                   (update estado :dir #(+ % 180))
                   pila
                   extremos
                   cs)

            \[
            (recur xs
                   estado
                   (conj pila estado)
                   extremos
                   cs)

            \]
            (recur xs
                   (peek pila)
                   (pop pila)
                   extremos
                   cs)

            ;; ignorar caracteres no reconocidos
            (recur xs estado pila extremos cs)))))))
