(set-env!
  :resource-paths #{"src"}
  :dependencies   '[[org.clojure/clojure "1.7.0"  :scope "provided"]
                    [adzerk/bootlaces    "0.1.10" :scope "test"]
                    [adzerk/env          "0.2.0"]])

(require
  '[adzerk.bootlaces :refer :all])

(def +version+ "0.1.0")

(bootlaces! +version+)

(task-options!
  pom  {:project     'adzerk/cljs-console
        :version     +version+
        :description "ClojureScript console logging macros."
        :url         "https://github.com/adzerk-oss/cljs-console"
        :scm         {:url "https://github.com/adzerk-oss/cljs-console"}
        :license     {"Eclipse Public License" "http://www.eclipse.org/legal/epl-v10.html"}})

