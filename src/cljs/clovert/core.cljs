(ns clovert.core
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [sablono.core :as html :refer-macros [html]]
            [goog.net.XhrIo :as xhr]
            [cljs.reader :as reader]
            [goog.dom :as gdom]
            [cljs.core.async :as async :refer [chan close! <!]])
  (:require-macros
   [cljs.core.async.macros :refer [go alt!]]))

(enable-console-print!)

(defn ^:export greet [n]
  (str "Hello " n))

(def app-state (atom {:movies [{:title "a"} {:title "B"} {:title "c"}]}))

(defn widget [data]
  (om/component
     (html [:ul (for [n (:movies data)]
                   [:li (:title n)])])))


(om/root widget app-state
  {:target (gdom/getElement "stuff")})

(defn GET [url]
  (let [ch (chan 1)]
    (xhr/send url
              (fn [event]
                (let [res (-> event .-target .getResponseText)]
                  (go (>! ch res)
                      (close! ch)))))
    ch))

(go
  (let [res (<! (GET "http://localhost:8000/resources/public/data/movies100.edn"))]
    (swap! app-state
           #(assoc-in % [:movies] (reader/read-string res)))))

