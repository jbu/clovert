(ns clovert.core
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [sablono.core :as html :refer-macros [html]]))


(defn ^:export greet [n]
  (str "Hello " n))

(defn widget [data]
  (om/component
   (html [:div "Hello world!"
          [:ul (for [n (range 1 10)]
                 [:li n])]
          (html/submit-button "React!")])))

(om/root widget {} {:target js/document.body})

;(om/root widget {:text "Hello world!"}
;  {:target (. js/document (getElementById "stuff"))})
