(ns clovert.core
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [sablono.core :as html :refer-macros [html]]
            [goog.net.XhrIo :as xhr]
            [cljs.core.async :as async :refer [chan close!]])
  (:require-macros
   [cljs.core.async.macros :refer [go alt!]]))


(defn ^:export greet [n]
  (str "Hello " n))

(def app-state (atom {}))

(defn widget [data]
  (om/component
   (html [:div "Hello world!"
          [:ul (for [n [1 2 3]]
                 [:li (get "title" n)])]])))

(om/root
 (fn [app owner]
   (apply dom/ul nil
          (map (fn [text] (dom/li nil text)) (:list app))))
 app-state
 {:target js/document.body})

(defn GET [url]
  (let [ch (chan 1)]
    (xhr/send url
              (fn [event]
                (let [res (-> event .-target .getResponseText)]
                  (go (>! ch res)
                      (close! ch)))))
    ch))

(go
  (swap! app-state :movies (js->clj (<! (GET "http://localhost:8000/resources/public/data/movies100.json"))
                {:keywordize-keys true})))

;(def movies (js->clj (.getResponseJson (.-target json)) :keywordize-keys true))
;(def movies (json/read-str (slurp "resources/public/data/movies100.json")))

;(om/root widget {:text "Hello world!"}
;  {:target (. js/document (getElementById "stuff"))})
