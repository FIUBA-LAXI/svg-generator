(ns tp2.turtle
  (:import [java.lang Math]))

(def paso 10.0)

;; Conversión de grados a radianes
(defn deg->rad [deg]
  (* (/ Math/PI 180) deg))

;; Actualiza los extremos del dibujo
(defn actualizar-extremos [ext x y]
  {:min-x (min (:min-x ext) x)
   :max-x (max (:max-x ext) x)
   :min-y (min (:min-y ext) y)
   :max-y (max (:max-y ext) y)})

;; Mueve la tortuga, con o sin trazo
(defn mover-tortuga [estado extremos dibujar?]
  (let [{:keys [x y dir]} estado
        θ (deg->rad dir)
        x2 (+ x (* paso (Math/cos θ)))
        y2 (+ y (* paso (Math/sin θ)))
        nuevos-extremos (actualizar-extremos extremos x2 y2)
        nuevo-estado {:x x2 :y y2 :dir dir}]
    {:linea (when dibujar? [[x y] [x2 y2]])
     :estado nuevo-estado
     :extremos nuevos-extremos}))

;; Interpreta una cadena de comandos tipo L-system
(defn interpretar [cadena angle-left angle-right]
  (loop [xs []
         estado {:x 0.0 :y 0.0 :dir -90.0}
         pila []
         extremos {:min-x 0.0 :max-x 0.0 :min-y 0.0 :max-y 0.0}
         [c & cs] (seq cadena)]
    (if (nil? c)
      {:lineas xs :extremos extremos}
      (case c
        (\F \G)
        (let [{:keys [linea estado extremos]} (mover-tortuga estado extremos true)]
          (recur (conj xs linea) estado pila extremos cs))

        (\f \g)
        (let [{:keys [estado extremos]} (mover-tortuga estado extremos false)]
          (recur xs estado pila extremos cs))

        \+
        (recur xs (update estado :dir #(+ % angle-right)) pila extremos cs)

        \-
        (recur xs (update estado :dir #(- % angle-left)) pila extremos cs)

        \|
        (recur xs (update estado :dir #(+ % 180)) pila extremos cs)

        \[
        (recur xs estado (conj pila estado) extremos cs)

        \]
        (recur xs (peek pila) (pop pila) extremos cs)

        ;; Ignorar caracteres no reconocidos
        (recur xs estado pila extremos cs)))))
