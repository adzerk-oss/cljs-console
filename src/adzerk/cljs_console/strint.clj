;;; strint.clj -- String interpolation for Clojure
;; originally proposed/published at http://muckandbrass.com/web/x/AgBP

;; by Chas Emerick <cemerick@snowtide.com>
;; December 4, 2009

;; Modified by Micha Niskin <micha.niskin@gmail.com>
;; Sun Sep  6 11:41:19 EDT 2015
;; - added support for %{}, %(), @{}, and @()

;; Copyright (c) Chas Emerick, 2009. All rights reserved.  The use
;; and distribution terms for this software are covered by the Eclipse
;; Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;; which can be found in the file epl-v10.html at the root of this
;; distribution.  By using this software in any fashion, you are
;; agreeing to be bound by the terms of this license.  You must not
;; remove this notice, or any other, from this software.

(ns
  ^{:author "Chas Emerick, Micha Niskin",
    :doc "String interpolation for Clojure."}
  adzerk.cljs-console.strint)

(defn- silent-read
  "Attempts to clojure.core/read a single form from the provided String, returning
   a vector containing the read form and a String containing the unread remainder
   of the provided String.  Returns nil if no valid form can be read from the
   head of the String."
  [s]0
  (try
    (let [r (-> s java.io.StringReader. java.io.PushbackReader.)]
      [(read r) (slurp r)])
    (catch Exception e))) ; this indicates an invalid form -- the head of s is just string data

(defn interpolate
  "Yields a seq of Strings and read forms."
  ([s atom?]
   (lazy-seq
     (if-let [[form rest] (silent-read (subs s (if atom? 2 1)))]
       (let [ch (subs s 0 1)]
         (cons [ch form] (interpolate (if atom? (subs rest 1) rest))))
       (cons (subs s 0 2) (interpolate (subs s 2))))))
  ([^String s]
   (if-let [start (->> ["~{" "~("
                        "#{" "#("
                        ".{" ".("
                        "'{" "'("
                        "<{" "<("
                        "@{" "@("
                        "^{" "^("]
                       (map #(.indexOf s %))
                       (remove #(== -1 %))
                       sort
                       first)]
     (lazy-seq (cons
                 (subs s 0 start)
                 (interpolate (subs s start) (= \{ (.charAt s (inc start))))))
     [s])))

(def escape-char
  {"~" "s"
   "#" "d"
   "." "f"
   "'" "s"
   "<" "o"
   "@" "O"
   "^" "c"})

(defn cljs-pprint
  [[k v]]
  [(str "%" (escape-char k))
   (if (not= k "~") v `(with-out-str (cljs.pprint/pprint ~v)))])

(defn build-args [string]
  (let [in   (interpolate string)
        strs (filter string? in)
        vars (map cljs-pprint (filter vector? in))
        escs (conj (mapv first vars) "")]
    (into [(apply str (interleave strs escs))] (map second vars))))
