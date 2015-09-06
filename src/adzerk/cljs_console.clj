(ns adzerk.cljs-console
  (:require
    [adzerk.env :as env]
    [adzerk.cljs-console.strint :refer [build-args]]))

(env/def ^:private CLJS_LOG_LEVEL nil)

(def ^:private levels
  {"DEBUG" 1
   "INFO"  2
   "WARN"  3
   "ERROR" 4
   "NONE"  5})

;; helpers ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn- env-level []
  (levels CLJS_LOG_LEVEL 1))

(defn- debugging? []
  (<= (env-level) (levels "DEBUG")))

(create-ns 'cljs.pprint)

(defn- emit-log [fn-name msg]
  (when (<= (env-level) (levels (.toUpperCase (name fn-name))))
    `(. js/console ~fn-name ~@(build-args msg))))

(defn- emit-group [fn-name msg body]
  (when-let [body (seq (keep identity (map macroexpand body)))]
    `(do (. js/console ~fn-name ~@(build-args msg))
         ~@body
         (.groupEnd js/console))))

(defmacro ^:private deflogfn [fn-name]
  `(defmacro ~fn-name [msg#]
     (emit-log '~fn-name msg#)))

(defmacro ^:private defgroupfn [macro-name fn-name]
  `(defmacro ~macro-name [msg# & body#]
     (emit-group '~fn-name msg# body#)))

(defmacro ^:private defdebugfn [macro-name args body]
  `(defmacro ~macro-name ~args
     ~(when (debugging?) body)))

;; public api ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(deflogfn debug)
(deflogfn info)
(deflogfn warn)
(deflogfn error)

(defgroupfn group+ group)
(defgroupfn group- groupCollapsed)

(defdebugfn debugger []
  `(js/debugger))

(defdebugfn log-assert [expr msg]
  `(when-not ~expr (.error js/console ~@(build-args msg))))

(defdebugfn log-count [label]
  `(.count js/console ~label))

(defdebugfn profile-start [label]
  `(.profile js/console ~label))

(defdebugfn profile-end [label]
  `(.profileEnd js/console ~label))

(defdebugfn with-profile [label & body]
  `(do (profile-start ~label) ~@body (profile-end ~label)))

(defdebugfn time-start [label]
  `(.time js/console ~label))

(defdebugfn time-end [label]
  `(.timeEnd js/console ~label))

(defdebugfn with-time [label & body]
  `(do (time-start ~label) ~@body (time-end ~label)))

(defdebugfn timestamp [& [label :as args]]
  `(.apply (.-timeStamp js/console) js/console (into-array args)))

(defdebugfn trace [obj & objs]
  `(.apply (.-trace js/console) js/console (into-array (cons obj objs))))

(defmacro spy->>
  ([x]
   `(spy->> "spy" ~x))
  ([msg x]
   (let [x' (gensym "spy")]
     `(let [~x' ~x] (group- ~msg (debug ~(str "~{" x' "}"))) ~x'))))

(defmacro spy->
  ([form]
   `(spy-> ~form "spy"))
  ([form msg]
   `(spy->> ~msg ~form)))
