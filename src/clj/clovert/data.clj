(ns clovert.data
  (:require [clojure.xml :as xml]
            [clojure.zip :as zip]
            [clojure.walk :as w]
            [clojure.data.json :as json]
            [clojure.edn :as edn]
            [clojure.string :as string]))


(defn zip-str [s]
  (zip/xml-zip (xml/parse (java.io.ByteArrayInputStream. (.getBytes s)))))

(def root "resources/public/data/")
(def files ["movies100", "movies300", "movies500", "movies700"])

(defmulti replace-node :tag)

(defmethod replace-node :rdf:rdf [node]
  (:content node))

(defmethod replace-node :rdf:Description [node]
  {:about (get-in node [:attrs :about])
   :title (some identity
                (map #(get-in % [:content 0]) (:content node)))
   :peers (map #(-> % (string/split #"\?") last keyword)
               (filter identity
                  (map #(get-in % [:attrs :rdf:resource]) (:content node))))
   :results {:average
             {:mark
              (Float/parseFloat (some identity
                                      (map #(get-in % [:attrs :gmp:mark]) (:content node))))
              :reliability
              (Float/parseFloat (some identity
                                      (map #(get-in % [:attrs :gmp:reliability]) (:content node))))}}})

(defmethod replace-node :default [node]
  node)

(defn process-file [fl]
  (let [rdfn (str root fl ".rdf")
        jsnn (str root fl ".json")
        ednn (str root fl ".edn")
        z (zip-str (slurp rdfn))
        p (first (w/postwalk #(replace-node %) z))
        d (reduce #(assoc %1 (-> %2 :about (string/split #"\?") last keyword) %2) {}
                  (map #(assoc %1 :year %2) p (iterate inc 1)))]
    (do
      (spit jsnn (json/write-str d))
      (spit ednn (prn-str d)))))

(doseq [f files]
  (process-file f))
