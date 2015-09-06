(ns adzerk.cljs-console
  (:require [cljs.pprint])
  (:require-macros [adzerk.cljs-console]))

(defn clear []
  (.clear js/console))
