; Manejar las iteraciones de las reglas SOBRE el axioma
(defn expand-system-l [axiom rules iterations]
  ; la funcion recibe un axioma, reglas (un mapa) e interaciones (osea las veces q se aplican las reglas
  (loop [current axiom
         n iterations]
    (if (zero? n)
      ; si no hay mas interaciones, devuelvo el string actual
      current
      ; sino:
      (let [next (apply str (mapcat #(get rules (str %) [(str %)]) current))] ;Si hay una regla, se reemplaza por su valor - si no, se mantiene el carácter original.
        ; recursivo, pero disminuyo en una la interacion
        (recur next (dec n))))))