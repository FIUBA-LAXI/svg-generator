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

;; Mueve la tortuga
(defn mover-tortuga [estado extremos dibujar?]
  (let [{:keys [x y dir color grosor]} estado
        θ (deg->rad dir)
        x2 (+ x (* paso (Math/cos θ)))
        y2 (+ y (* paso (Math/sin θ)))
        nuevos-extremos (actualizar-extremos extremos x2 y2)
        nuevo-estado {:x x2 :y y2 :dir dir :color color :grosor grosor}]
    (if dibujar?
      {:linea {:inicio [x y]
               :fin [x2 y2]
               :color color
               :grosor grosor}
       :estado nuevo-estado
       :extremos nuevos-extremos}
      {:estado nuevo-estado
       :extremos nuevos-extremos})))


;; Interpreta una cadena de comandos tipo L-system
(defn interpretar [cadena angle-left angle-right]
  (loop [xs []
         figuras []
         estado {:x 0.0 :y 0.0 :dir -90.0 :color "black" :grosor 1}
         pila []
         extremos {:min-x 0.0 :max-x 0.0 :min-y 0.0 :max-y 0.0}
         [c & cs] (seq cadena)]
    (if (nil? c)
      {:lineas xs :extremos extremos :figuras figuras}
      (case c
        (\F \G)
        (let [{:keys [linea estado extremos]} (mover-tortuga estado extremos true)]
          (recur (conj xs linea) figuras estado pila extremos cs))

        (\f \g)
        (let [{:keys [estado extremos]} (mover-tortuga estado extremos false)]
          (recur xs figuras estado pila extremos cs))

        \+
        (recur xs figuras (update estado :dir #(+ % angle-right)) pila extremos cs)

        \-
        (recur xs figuras (update estado :dir #(- % angle-left)) pila extremos cs)

        \|
        (recur xs figuras (update estado :dir #(+ % 180)) pila extremos cs)

        \[
        (recur xs figuras estado (conj pila estado) extremos cs)

        \]
        (recur xs figuras (peek pila) (pop pila) extremos cs)

        \C
        (recur xs (conj figuras {:tipo :circulo :x (:x estado) :y (:y estado) :color (:color estado)}) estado pila extremos cs)

        \R
        (recur xs (conj figuras {:tipo :cuadrado :x (:x estado) :y (:y estado) :color (:color estado) :angulo (:dir estado)}) estado pila extremos cs)

        ;; cambiar color (ejemplos para a,b,c...)
        \a (recur xs figuras (assoc estado :color "blue") pila extremos cs)
        \b (recur xs figuras (assoc estado :color "red") pila extremos cs)
        \c (recur xs figuras (assoc estado :color "green") pila extremos cs)
        \d (recur xs figuras (assoc estado :color "yellow") pila extremos cs)
        \e (recur xs figuras (assoc estado :color "purple") pila extremos cs)
        \h (recur xs figuras (assoc estado :color "brown") pila extremos cs)
        \i (recur xs figuras (assoc estado :color "cyan") pila extremos cs)
        \j (recur xs figuras (assoc estado :color "magenta") pila extremos cs)
        \k (recur xs figuras (assoc estado :color "black") pila extremos cs)
        \l (recur xs figuras (assoc estado :color "gray") pila extremos cs)
        \m (recur xs figuras (assoc estado :color "lime") pila extremos cs)
        \n (recur xs figuras (assoc estado :color "navy") pila extremos cs)
        \o (recur xs figuras (assoc estado :color "olive") pila extremos cs)
        \p (recur xs figuras (assoc estado :color "pink") pila extremos cs)
        \q (recur xs figuras (assoc estado :color "orange") pila extremos cs)

        ;; cambiar grosor
        \0 (recur xs figuras (assoc estado :grosor 0) pila extremos cs)
        \1 (recur xs figuras (assoc estado :grosor 0.2) pila extremos cs)
        \2 (recur xs figuras (assoc estado :grosor 0.4) pila extremos cs)
        \3 (recur xs figuras (assoc estado :grosor 0.6) pila extremos cs)
        \4 (recur xs figuras (assoc estado :grosor 0.8) pila extremos cs)
        \5 (recur xs figuras (assoc estado :grosor 1) pila extremos cs)
        \6 (recur xs figuras (assoc estado :grosor 1.2) pila extremos cs)
        \7 (recur xs figuras (assoc estado :grosor 1.4) pila extremos cs)
        \8 (recur xs figuras (assoc estado :grosor 1.6) pila extremos cs)
        \9 (recur xs figuras (assoc estado :grosor 1.8) pila extremos cs)

        ;; Ignorar caracteres no reconocidos
        (recur xs figuras estado pila extremos cs)))))
