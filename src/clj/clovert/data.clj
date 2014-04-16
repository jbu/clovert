(ns clovert.data
  (:require [clojure.xml :as xml]
            [clojure.zip :as zip]
            [clojure.walk :as w]
            [clojure.data.json :as json]))


(defn zip-str [s]
  (zip/xml-zip (xml/parse (java.io.ByteArrayInputStream. (.getBytes s)))))


(def m2 (zip-str (slurp "resources/public/data/movies100.rdf")))

(defmulti replace-node :tag)
(defmethod replace-node :rdf:rdf [node]
  (:content node))
(defmethod replace-node :rdf:Description [node]
  {:about (get-in node [:attrs :about])
   :title (some identity
                (map #(get-in % [:content 0]) (:content node)))
   :peers (filter identity
                  (map #(get-in % [:attrs :rdf:resource]) (:content node)))
   :mark  (Float/parseFloat
           (some identity
                 (map #(get-in % [:attrs :gmp:mark]) (:content node))))
   :reliability  (Float/parseFloat
                  (some identity
                        (map #(get-in % [:attrs :gmp:reliability]) (:content node))))
   })
(defmethod replace-node :default [node]
  node)

(def parsed (first (w/postwalk #(replace-node %) m2)))

(json/pprint parsed)
