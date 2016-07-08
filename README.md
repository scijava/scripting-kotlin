[![](http://jenkins.imagej.net/job/scripting-Kotlin/lastBuild/badge/icon)](http://jenkins.imagej.net/job/scripting-Kotlin/)

# Kotlin Scripting

This library provides a scripting plugin wrapping the
[DynKT](https://github.com/xafero/dynkt) library, a JSR-223 engine for the
[Kotlin](https://kotlinlang.org/) language.

It is implemented as a `ScriptLanguage` plugin for the [SciJava
Common](https://github.com/scijava/scijava-common) platform, which means that
in addition to being usable directly as a `javax.script.ScriptEngineFactory`,
it also provides some functionality on top, such as the ability to generate
lines of script code based on SciJava events.

For a complete list of scripting languages available as part of the SciJava
platform, see the
[Scripting](https://github.com/scijava/scijava-common/wiki/Scripting) page on
the SciJava Common wiki.

See also:
* [Kotlin Scripting](http://imagej.net/Kotlin_Scripting)
  on the ImageJ wiki.
