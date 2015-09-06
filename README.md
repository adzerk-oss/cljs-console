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

<samp><pre>(debug <em>message</em>)</pre></samp>
<samp><pre>(info <em>message</em>)</pre></samp>
<samp><pre>(warn <em>message</em>)</pre></samp>
<samp><pre>(error <em>message</em>)</pre></samp>

### Grouping

<samp><pre>(group+ <em>message</em> & <em>body</em>) <em>; expanded</em></pre></samp>
<samp><pre>(group- <em>message</em> & <em>body</em>) <em>; collapsed</em></pre></samp>

### Debug Log Level Only

<samp><pre>(debugger)</pre></samp>
<samp><pre>(assert <em>expression</em> <em>message</em>)</pre></samp>
<samp><pre>(count <em>label</em>)</pre></samp>
<samp><pre>(profile-start <em>label</em>)</pre></samp>
<samp><pre>(profile-end <em>label</em>)</pre></samp>
<samp><pre>(with-profile <em>label</em> & <em>body</em>)</pre></samp>
<samp><pre>(time-start <em>label</em>)</pre></samp>
<samp><pre>(time-end <em>label</em>)</pre></samp>
<samp><pre>(with-time <em>label</em> & <em>body</em>)</pre></samp>
<samp><pre>(timestamp), (timestamp <em>label</em>)</pre></samp>
<samp><pre>(trace <em>object</em> & <em>objects</em>)</pre></samp>
<samp><pre>(spy-> <em>form</em>), (spy-> <em>form</em> <em>message</em>)</pre></samp>
<samp><pre>(spy->> <em>form</em>), (spy->> <em>message</em> <em>form</em>)</pre></samp>

### Misc

<samp><pre>(clear)</pre></samp>

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
