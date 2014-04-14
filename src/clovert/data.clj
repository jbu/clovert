(ns clovert.data
  :require [clojure.xml :as xml]
           [clojure.zip :as zip])


(defn zip-str [s]
  (zip/xml-zip (xml/parse (java.io.ByteArrayInputStream. (.getBytes s)))))

(def m2 (zip-str (slurp "resources/public/data/movies2.rdf")))
