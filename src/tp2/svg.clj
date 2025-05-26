(ns tp2.svg
  (:require [clojure.pprint :refer [cl-format]]))

(defn generate-svg [lineas extremos]
  (let [padding-ratio 0.1
        {:keys [min-x max-x min-y max-y]} extremos
        view-width  (- max-x min-x)
        view-height (- max-y min-y)
        padding-x (* view-width padding-ratio)
        padding-y (* view-height padding-ratio)
        view-min-x (- min-x padding-x)
        view-min-y (- min-y padding-y)
        view-box-width (+ view-width (* 2 padding-x))
        view-box-height (+ view-height (* 2 padding-y))
        ;; Convertimos las líneas en comandos de path:
        path-str (apply str
                        (map (fn [[[x1 y1] [x2 y2]]]
                               (str "M " (cl-format nil "~,4F" x1) " " (cl-format nil "~,4F" y1)
                                    " L " (cl-format nil "~,4F" x2) " " (cl-format nil "~,4F" y2) " "))
                             lineas))]
    (str "<svg viewBox=\""
         view-min-x " "
         view-min-y " "
         view-box-width " "
         view-box-height
         "\" xmlns=\"http://www.w3.org/2000/svg\">"
         "<path d=\"" path-str "\" stroke-width=\"1\" stroke=\"black\" fill=\"none\"/>"
         "</svg>")))

(defn write-svg-file [file-path lineas extremos]
  (let [svg-content (generate-svg lineas extremos)]
    (spit file-path svg-content)))