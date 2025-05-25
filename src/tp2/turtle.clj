(ns tp2.turtle
  (:import [java.lang Math]))

(defn interpretar [cadena angulo]
  (let [rads (fn [deg] (* (/ Math/PI 180) deg))]
    (loop [xs []                                      ;; líneas acumuladas
           estado {:x 0.0 :y 0.0 :dir 0.0}            ;; estado actual de la tortuga
           pila []                                    ;; pila para guardar y recuperar el estado
           [c & cs] (seq cadena)]                     ;; comandos por procesar !
      (if (nil? c)
        xs
        (let [{:keys [x y dir]} estado
              θ (rads dir)]
          (case c
            ;; F o G = avanzar y dibujar
            (\F \G)
            (let [x2 (+ x (Math/cos θ))
                  y2 (+ y (Math/sin θ))]
              (recur (conj xs [[x y] [x2 y2]])
                     {:x x2 :y y2 :dir dir}
                     pila
                     cs))

            ;; f o g = avanzar sin dibujar
            (\f \g)
            (let [x2 (+ x (Math/cos θ))
                  y2 (+ y (Math/sin θ))]
              (recur xs
                     {:x x2 :y y2 :dir dir}
                     pila
                     cs))

            ;; + = girar a la derecha (sumar ángulo)
            \+
            (recur xs
                   (update estado :dir #(+ % angulo))
                   pila
                   cs)

            ;; - = girar a la izquierda (restar ángulo)
            \-
            (recur xs
                   (update estado :dir #(- % angulo))
                   pila
                   cs)

            ;; | = invertir la dirección (girar 180°)
            \|
            (recur xs
                   (update estado :dir #(+ % 180))
                   pila
                   cs)

            ;; [ = apilar
            \[
            (recur xs
                   estado
                   (conj pila estado)
                   cs)

            ;; ] = desapilar
            \]
            (recur xs
                   (peek pila)
                   (pop pila)
                   cs)

            ;; cualquier otro caracter = ignorar ????????????????????
            (recur xs estado pila cs)))))))