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
      (do
        (println "Error: el archivo no existe.")
        false)
      (not (.isFile f))
      (do
        (println "Error: la ruta no apunta a un archivo válido.")
        false)
      :else true)))

(defn validate-integer [value]
  (try
    (Integer/parseInt value)
    (if (<= (Integer/parseInt value) 0)
      (do
        (println "Error: el valor debe ser un número entero positivo.")
        false)
      true)
    (catch NumberFormatException e
      (do
        (println "Error: el valor no es un número entero.")
        false))))

(defn validate-writing-file [file-path]
  (if (.endsWith file-path ".svg")
    true
    (do
      (println "Error: el archivo de salida debe tener la extensión .svg.")
      false)))

(defn validate-args [args]
  (if (< (count args) 3)
    (do
      (println "Uso: lein run <archivo-entrada> <iteraciones> <archivo-salida> [grosor] [color]")
      false)
    (let [input-file (nth args 0)
          iterations (nth args 1)
          output-file (nth args 2)]
      (cond
        (not (validate-reading-file input-file))
        false
        (not (validate-integer iterations))
        false
        (not (validate-writing-file output-file))
        false
        :else true))))

(defn -main [& args]
  (if (not (validate-args args))
    (System/exit 1)
    (let [input-file (nth args 0)
          iterations (Integer/parseInt (nth args 1))
          output-file (nth args 2)
          [grosor color] (cond ; agregué esto: si el usuario en vez de poner 5 argpone 4, veo si el 4to arg es el grosor o un color.
                           (= (count args) 4)
                           (let [arg4 (nth args 3)]
                             (try
                               [(Integer/parseInt arg4) "black"] ; si es número: [grosor, color por defecto]
                               (catch NumberFormatException _ ; si falla la conversión
                                 [1 arg4])))  ; lo tratocomo color y grosor = 1
                           (>= (count args) 5) ; si hay justo 5 args
                           (let [arg4 (nth args 3) ; este es grosor
                                 arg5 (nth args 4)] ; este es color
                             (try
                               [(Integer/parseInt arg4) arg5]
                               (catch NumberFormatException _
                                 (do
                                   (println "Advertencia: grosor inválido, se usará 1.")
                                   [1 arg5]))))
                           :else
                           [1 "black"]) ;default, uso grosor 1 y color negro!
          system-l (parser/read-system-l input-file)
          expanded (lsystem/expand-system-l (:axiom system-l)
                                            (:rules system-l)
                                            iterations)
          lineas (turtle/interpretar expanded (:angle-left system-l) (:angle-right system-l))]
      (println "Sistema L cargado:")
      (println "Ángulo izquierda:" (:angle-left system-l))
      (println "Ángulo derecha:" (:angle-right system-l))
      (println "Axioma:" (:axiom system-l))
      (println "Reglas:" (:rules system-l))
      (println "Cadena expandida:")
      (println expanded)
      (println "Cantidad de líneas generadas por turtle:" (count (:lineas lineas)))
      (let [extremos (:extremos lineas)]
        (svg/write-svg-file output-file
                            (:lineas lineas)
                            extremos
                            grosor
                            color
                            (:figuras lineas))))))