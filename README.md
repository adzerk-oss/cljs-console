# cljs-console

ClojureScript console logging macros.

- No code is emitted for log commands that are below the log level specified
  in the environment at compile time.
- Groups are only emitted when their body is not elided.
- Support for log message formatting via string interpolation.
- ClojureScript forms are pretty-printed.
- Support for debugger, profiling and timing, respecting environment log level.

## Usage

[](dependency)
```clojure
[adzerk/env "0.2.0"] ;; latest release
```
[](/dependency)

```clojure
(ns foo
  (:require [adzerk.cljs-console :as log :include-macros true]))
```

### Environment

```
CLJS_LOG_LEVEL={DEBUG|INFO|WARN|ERROR|NONE}
```

### Message Formatting

String interpolation is used to generate [formatted log messages][format]. The
interpolated form or expression is delimited by a control character followed by
`{}` for atoms or `()` for expressions.

| Char | Format | Mnemonic            | Example                    |
|------|--------|---------------------|----------------------------|
| `~`  | `%s`   | Clojure unquote     | `"data: ~(:data x)"`       |
| `#`  | `%d`   | ints are numbers    | `"found #{count} things"`  |
| `.`  | `%f`   | floats have points  | `"money = $.{amount}"`     |
| `'`  | `%s`   | strings are quoted  | `"hello, '(:name person)"` |
| `<`  | `%o`   | xml has `<>`        | `"doc: <{js/document}"`    |
| `@`  | `%O`   | looks like big-O    | `"status: @{some-js-obj}"` |
| `^`  | `%c`   | nobody uses this    | `"^{css-format}hi there"`  |

### Logging

```clojure
(debug message)
(info message)
(warn message)
(error message)
```

### Grouping

```clojure
(group+ message & body) ; expanded
(group- message & body) ; collapsed
```

### Debug Log Level Only

```clojure
;; drop into the js debugger
(debugger)

;; log error if expression isn't truthy
(assert expression message)

;; count how many times it's called
(count label)

;; engage js profiling engine
(profile-start label)
(profile-end label)
(with-profile label & body)

;; record a time interval
(time-start label)
(time-end label)
(with-time label & body)

;; mark the timeline
(timestamp), (timestamp label)

;; get a stack trace
(trace object & objects)

;; inline inspection of values
(spy-> form), (spy-> form message)
(spy->> form), (spy->> message form)
```

### Misc

```clojure
(clear)
```

## Hacking

```
# build and install locally
boot build-jar
```
```
# push snapshot
boot build-jar push-snapshot
```
```
# push release
boot build-jar push-release
```

## License

Copyright © 2015 Adzerk

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.

[format]: https://developer.chrome.com/devtools/docs/console-api#consolelogobject-object
