(ns tp2.parser
  (:require [clojure.string :as str]
            [clojure.java.io :as io]))

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