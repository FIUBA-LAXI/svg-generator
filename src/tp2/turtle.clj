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
         pila [{:x 0.0 :y 0.0 :dir -90.0 :color "black" :grosor 1}]
         extremos {:min-x 0.0 :max-x 0.0 :min-y 0.0 :max-y 0.0}
         [c & cs] (seq cadena)]
    (if (nil? c)
      {:lineas xs :extremos extremos :figuras figuras}
      (let [estado (peek pila)]
        (case c
          (\F \G)
          (let [{:keys [linea estado extremos]} (mover-tortuga estado extremos true)]
            (recur (conj xs linea) figuras (conj (pop pila) estado) extremos cs))

          (\f \g)
          (let [{:keys [estado extremos]} (mover-tortuga estado extremos false)]
            (recur xs figuras (conj (pop pila) estado) extremos cs))

          \+
          (recur xs figuras (conj (pop pila) (update estado :dir #(+ % angle-right))) extremos cs)

          \-
          (recur xs figuras (conj (pop pila) (update estado :dir #(- % angle-left))) extremos cs)

          \|
          (recur xs figuras (conj (pop pila) (update estado :dir #(+ % 180))) extremos cs)

          \[
          (recur xs figuras (conj pila estado) extremos cs)

          \]
          (recur xs figuras (pop pila) extremos cs)

          \C
          (recur xs (conj figuras {:tipo :circulo :x (:x estado) :y (:y estado) :color (:color estado)}) pila extremos cs)

          \R
          (recur xs (conj figuras {:tipo :cuadrado :x (:x estado) :y (:y estado) :color (:color estado) :angulo (:dir estado)}) pila extremos cs)

          ;; cambiar color
          \a (recur xs figuras (conj (pop pila) (assoc estado :color "blue")) extremos cs)
          \b (recur xs figuras (conj (pop pila) (assoc estado :color "red")) extremos cs)
          \c (recur xs figuras (conj (pop pila) (assoc estado :color "green")) extremos cs)
          \d (recur xs figuras (conj (pop pila) (assoc estado :color "yellow")) extremos cs)
          \e (recur xs figuras (conj (pop pila) (assoc estado :color "purple")) extremos cs)
          \h (recur xs figuras (conj (pop pila) (assoc estado :color "brown")) extremos cs)
          \i (recur xs figuras (conj (pop pila) (assoc estado :color "cyan")) extremos cs)
          \j (recur xs figuras (conj (pop pila) (assoc estado :color "magenta")) extremos cs)
          \k (recur xs figuras (conj (pop pila) (assoc estado :color "black")) extremos cs)
          \l (recur xs figuras (conj (pop pila) (assoc estado :color "gray")) extremos cs)
          \m (recur xs figuras (conj (pop pila) (assoc estado :color "lime")) extremos cs)
          \n (recur xs figuras (conj (pop pila) (assoc estado :color "navy")) extremos cs)
          \o (recur xs figuras (conj (pop pila) (assoc estado :color "olive")) extremos cs)
          \p (recur xs figuras (conj (pop pila) (assoc estado :color "pink")) extremos cs)
          \q (recur xs figuras (conj (pop pila) (assoc estado :color "orange")) extremos cs)

          ;; cambiar grosor
          \0 (recur xs figuras (conj (pop pila) (assoc estado :grosor 0)) extremos cs)
          \1 (recur xs figuras (conj (pop pila) (assoc estado :grosor 0.2)) extremos cs)
          \2 (recur xs figuras (conj (pop pila) (assoc estado :grosor 0.4)) extremos cs)
          \3 (recur xs figuras (conj (pop pila) (assoc estado :grosor 0.6)) extremos cs)
          \4 (recur xs figuras (conj (pop pila) (assoc estado :grosor 0.8)) extremos cs)
          \5 (recur xs figuras (conj (pop pila) (assoc estado :grosor 1)) extremos cs)
          \6 (recur xs figuras (conj (pop pila) (assoc estado :grosor 1.2)) extremos cs)
          \7 (recur xs figuras (conj (pop pila) (assoc estado :grosor 1.4)) extremos cs)
          \8 (recur xs figuras (conj (pop pila) (assoc estado :grosor 1.6)) extremos cs)
          \9 (recur xs figuras (conj (pop pila) (assoc estado :grosor 1.8)) extremos cs)

          ;; Ignorar caracteres no reconocidos
          (recur xs figuras pila extremos cs))))))
