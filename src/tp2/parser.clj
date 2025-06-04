(ns tp2.parser
  (:require [clojure.string :as str]
            [clojure.java.io :as io]))

(defn parse-angle [line]
  (let [parts (clojure.string/split line #"[ ,]+")
        ang1 (Double/parseDouble (first parts))
        ang2 (if (> (count parts) 1)
               (Double/parseDouble (second parts))
               ang1)]
    {:angle-left ang1 :angle-right ang2}))

(defn parse-system-l [lines]
  (let [{:keys [angle-left angle-right]} (parse-angle (first lines))
        axiom (second lines)
        rules (->> (drop 2 lines)
                   (map #(str/split % #"\s+" 2))
                   (into {}))]
    {:angle-left angle-left
     :angle-right angle-right
     :axiom axiom
     :rules rules}))

(defn read-system-l [file-path]
  (let [lines (with-open [rdr (io/reader file-path)]
                (doall (line-seq rdr)))]
    (parse-system-l lines)))