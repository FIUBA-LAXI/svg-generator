(ns tp2.svg
  (:require [clojure.pprint :refer [cl-format]]))

(defn figura->svg [{:keys [tipo x y angulo color]}]
  (case tipo
    :circulo
    (str "<circle cx=\"" (cl-format nil "~,4F" x)
         "\" cy=\"" (cl-format nil "~,4F" y)
         "\" r=\"4\" fill=\"" (or color "black") "\"/>")

    :cuadrado
    (let [size 8
          x0 (- x (/ size 2))
          y0 (- y (/ size 2))
          transform (if angulo
                      (str "rotate(" angulo " " x " " y ")")
                      nil)]
      (str "<rect x=\"" (cl-format nil "~,4F" x0)
           "\" y=\"" (cl-format nil "~,4F" y0)
           "\" width=\"" size "\" height=\"" size
           "\" fill=\"" (or color "black") "\""
           (when transform (str " transform=\"" transform "\""))
           "/>"))

    ;; Si hay alguna otra figura que no reconocemos
    ""))

(defn generate-svg [lineas extremos grosor color figuras]
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
        path-str (apply str
                        (map (fn [[[x1 y1] [x2 y2]]]
                               (str "M " (cl-format nil "~,4F" x1) " " (cl-format nil "~,4F" y1)
                                    " L " (cl-format nil "~,4F" x2) " " (cl-format nil "~,4F" y2) " "))
                             lineas))
        figuras-svg (apply str (map figura->svg figuras))]
    (str "<svg viewBox=\""
         view-min-x " "
         view-min-y " "
         view-box-width " "
         view-box-height
         "\" xmlns=\"http://www.w3.org/2000/svg\">"
         "<path d=\"" path-str "\" stroke-width=\"" grosor "\" stroke=\"" color "\" fill=\"none\"/>"
         figuras-svg
         "</svg>")))

(defn write-svg-file [file-path lineas extremos grosor color figuras]
  (let [svg-content (generate-svg lineas extremos grosor color figuras)]
    (spit file-path svg-content)))