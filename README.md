[![](https://travis-ci.org/scijava/scripting-kotlin.svg?branch=master)](https://travis-ci.org/scijava/scripting-kotlin)

# Kotlin Scripting

This library provides a scripting plugin wrapping the JSR-223 engine for the
[Kotlin](https://kotlinlang.org/) language using the
`KotlinJsr223DefaultScriptEngineFactory`.


It is implemented as a `ScriptLanguage` plugin for the [SciJava
Common](https://github.com/scijava/scijava-common) platform, which means that
in addition to being usable directly as a `javax.script.ScriptEngineFactory`,
it also provides some functionality on top, such as the ability to generate
lines of script code based on SciJava events.

For a complete list of scripting languages available as part of the SciJava
platform, see the
[Scripting](https://github.com/scijava/scijava-common/wiki/Scripting) page on
the SciJava Common wiki.

## Installation into Fiji

Clone this repository and run, at the root of this repository:
```shell
mvn -Dscijava.app.directory=/path/to/Fiji.app
```
