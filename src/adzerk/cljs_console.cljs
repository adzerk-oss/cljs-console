(ns adzerk.cljs-console
  (:require-macros [adzerk.cljs-console]))

(defn clear []
  (.clear js/console))
