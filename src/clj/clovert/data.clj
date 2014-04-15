(ns clovert.data
  (:require [clojure.xml :as xml]
            [clojure.zip :as zip]
            [clojure.walk :as w]))


(defn zip-str [s]
  (zip/xml-zip (xml/parse (java.io.ByteArrayInputStream. (.getBytes s)))))


(def m2 (zip-str (slurp "resources/public/data/movies2.rdf")))

(defmulti replace-node :tag)
(defmethod replace-node :rdf:rdf [node]
  (get node :content))
(defmethod replace-node :dc:Title [node]
  {:title (get-in node [:content 0])})
(defmethod replace-node :rdf:Description [node]
  {:about (get-in node [:attrs :about])
   :title (get-in (some #(= (:tag %) :dc:Title)(:content node)) [:content 0])
   ;:mark  (get-in node [:content 0 :])
   :node node})
(defmethod replace-node :gmp:peer [node]
  {:peer (get-in node [:attrs :rdf:resource])})
(defmethod replace-node :gmp:results [node]
  {:mark (Float/parseFloat (get-in node [:attrs :gmp:mark]))
   :reliability (Float/parseFloat (get-in node [:attrs :gmp:reliability]))})
(defmethod replace-node :default [node]
  node)

(w/prewalk #(replace-node %) m2)
