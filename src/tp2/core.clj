(ns tp2.core
  (:gen-class)
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

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
  (if (= (count args) 3)
    true
    (do
      (println "Uso: lein run <archivo-entrada> <iteraciones> <archivo-salida>")
      false))
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
      :else true)))

(defn parse-system-l [lines]
  (let [angle (Double/parseDouble (first lines))
        axiom (second lines)
        rules (->> (drop 2 lines)
                   (map #(str/split % #"\s+" 2))
                   (into {}))]
    {:angle angle
     :axiom axiom
     :rules rules}))

(defn read-system-l [file-path]
  (let [lines (with-open [rdr (io/reader file-path)]
                (doall (line-seq rdr)))]
    (parse-system-l lines)))

(defn -main
  [& args]
  (if (not (validate-args args))
    (System/exit 1)
    (let [input-file (nth args 0)
          iterations (Integer/parseInt (nth args 1))
          output-file (nth args 2)
          system-l (read-system-l input-file)]
      (println "Sistema L cargado:")
      (println "Ángulo:" (:angle system-l))
      (println "Axioma:" (:axiom system-l))
      (println "Reglas:" (:rules system-l)))))
