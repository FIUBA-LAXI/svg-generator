(ns tp2.core
  (:gen-class)
  (:require [clojure.java.io :as io]
            [tp2.parser :as parser]
            [tp2.lsystem :as lsystem]
            [tp2.turtle :as turtle]
            [tp2.svg :as svg]))

(defn validate-reading-file [file-path]
  (let [f (io/file file-path)]
    (cond
      (not (.exists f))
      (throw (Exception. (str "Error: el archivo '" file-path "' no existe.")))
      (not (.isFile f))
      (throw (Exception. (str "Error: la ruta '" file-path "' no apunta a un archivo válido.")))
      :else true)))

(defn validate-integer [value]
  (try
    (Integer/parseInt value)
    (if (<= (Integer/parseInt value) 0)
      (throw (Exception. "Error: el número de iteraciones debe ser mayor que cero."))
      true)
    (catch NumberFormatException e
      (throw (Exception. "Error: el número de iteraciones debe ser un entero válido.")))))

(defn validate-writing-file [file-path]
  (if (.endsWith file-path ".svg")
    true
    (throw (Exception. "Error: el archivo de salida debe tener la extensión '.svg'."))))

(defn validate-args [args]
  (if (< (count args) 3)
    (throw (Exception. "Uso: lein run <archivo-entrada> <iteraciones> <archivo-salida>")))
    (let [[input-file iterations output-file] args]
      (validate-reading-file input-file)
      (validate-integer iterations)
      (validate-writing-file output-file)))

(defn -main [& args]
  (validate-args args)
  (let [input-file (nth args 0)
        iterations (Integer/parseInt (nth args 1))
        output-file (nth args 2)
        system-l (parser/read-system-l input-file)
        expanded (lsystem/expand-system-l (:axiom system-l)
                                          (:rules system-l)
                                          iterations)
        lineas (turtle/interpretar expanded
                                   (:angle-left system-l)
                                   (:angle-right system-l))]
    (let [extremos (:extremos lineas)]
      (svg/write-svg-file output-file
                          (:lineas lineas)
                          extremos
                          (:figuras lineas)))))
