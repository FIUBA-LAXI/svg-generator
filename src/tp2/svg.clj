(ns tp2.svg)

"
 Dentro del atributo d usamos los comandos M x y para movernos a las coordenadas (x, y) y L x y para movernos dibujando una línea recta:\n\n<svg viewBox=\"-50 -150 300 200\" xmlns=\"http://www.w3.org/2000/svg\">\n  <path d=\"M 0 0 L 200 0 L 200 -100 M 100 -100 L 0 -100\" stroke-width=\"1\" stroke=\"black\" fill=\"none\"/>\n</svg>\n
"
(defn generate-svg [lineas extremos]
  (let [{:keys [min-x max-x min-y max-y]} extremos
        width (- max-x min-x)
        height (- max-y min-y)]
    (str "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
         "<svg xmlns=\"http://www.w3.org/2000/svg\"\n"
         "     width=\"" width "\" height=\"" height "\"\n"
         "     viewBox=\"" min-x " " min-y " " width " " height "\">\n"
         "<path d=\"M "
         (apply str
                (interpose " L "
                           (map (fn [[start end]]
                                  (let [[x1 y1] start
                                        [x2 y2] end]
                                    (str x1 " " y1)))
                                lineas)))
         "\" style=\"stroke:black;stroke-width:1;fill:none;\"/>\n"
         "</svg>")))

(defn write-svg-file [file-path lineas extremos]
  (let [svg-content (generate-svg lineas extremos)]
    (spit file-path svg-content)))