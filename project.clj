(defproject clovert "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :plugins [[lein-cljsbuild "1.0.3"]]
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2202"]
                 [hiccups "0.3.0"]
                 [om "0.5.3"]
                 [sablono "0.2.15"]
                 [org.clojure/data.json "0.2.4"]
                 [aysylu/loom "0.4.2"]
                 ]
  :hooks [leiningen.cljsbuild]
  :source-paths ["src/clj"]
  :cljsbuild {
              :repl-listen-port 9000
              :repl-launch-commands
                {"index" ["open" "-a" "Google Chrome" "--args" "http://localhost:8000/index.html"]}
              :builds [{
                        ; The path to the top-level ClojureScript source directory:
                        ;:source-paths ["src-cljs"]
                        :source-paths ["src/cljs"]
                        ; The standard ClojureScript compiler options
                        ; (See the ClojureScript compiler documentation for details.)
                        :compiler {
                                   ;:output-to "war/javascripts/main.js"  ; default: target/cljsbuild-main.js
                                   :output-dir "resources/public/js"
                                   :output-to "resources/public/js/main.js"
                                   :source-map "resources/public/js/main.js.map"
                                   :warnings true
                                   :optimizations :whitespace
                                   :pretty-print true}}]})
