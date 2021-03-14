/*
 * #%L
 * JSR-223-compliant Kotlin scripting language plugin.
 * %%
 * Copyright (C) 2016 - 2019 Board of Regents of the University of
 * Wisconsin-Madison.
 * %%
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */
package org.scijava.plugins.scripting.kotlin

import org.jetbrains.kotlin.cli.common.repl.*
import org.jetbrains.kotlin.com.intellij.openapi.util.Disposer
import org.jetbrains.kotlin.script.jsr223.KotlinJsr223JvmDaemonCompileScriptEngine
import org.jetbrains.kotlin.script.jsr223.KotlinJsr223JvmLocalScriptEngine
import org.jetbrains.kotlin.script.jsr223.KotlinStandardJsr223ScriptTemplate
import org.scijava.plugin.Plugin
import org.scijava.script.AdaptedScriptLanguage
import org.scijava.script.ScriptLanguage
import java.io.Reader
import java.lang.Exception
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.locks.ReentrantReadWriteLock
import javax.script.*
import kotlin.script.experimental.jvm.util.KotlinJars
import kotlin.script.experimental.jvm.util.scriptCompilationClasspathFromContext
import kotlin.system.exitProcess

/**
 * A SciJava [ScriptLanguage] for Kotlin.
 *
 * @author Curtis Rueden
 * @author Philipp Hanslovsky
 * @see ScriptEngine
 */
@Plugin(type = ScriptLanguage::class, name = "Kotlin")
class KotlinScriptLanguage : AdaptedScriptLanguage(Factory()) {
    // NB: The wrapped ScriptEngineFactory does not include Kotlin in its list.
    override fun getNames() = listOf("kotlin", "Kotlin")

    // NB: The wrapped ScriptEngineFactory does not include .kt in its list.
    override fun getExtensions() = listOf("kt", "kts")

    class Factory : KotlinJsr223JvmScriptEngineFactoryBase() {
        override fun getScriptEngine(): ScriptEngine {
            return SynchronizedScriptEngine(
//                KotlinJsr223JvmDaemonCompileScriptEngine(
//                    this,
//                    KotlinJars.compilerWithScriptingClasspath,
//                    scriptCompilationClasspathFromContext("kotlin-script-util.jar", wholeClasspath = true),
//                    KotlinStandardJsr223ScriptTemplate::class.qualifiedName!!,
//                    { ctx, types -> ScriptArgsWithTypes(arrayOf(ctx.getBindings(ScriptContext.ENGINE_SCOPE)), types ?: emptyArray()) },
//                    arrayOf(Bindings::class)
//                )
                KotlinJsr223JvmLocalScriptEngine(
                    this,
                    scriptCompilationClasspathFromContext("kotlin-script-util.jar", wholeClasspath = true),
                    KotlinStandardJsr223ScriptTemplate::class.qualifiedName!!,
                    { ctx, types -> ScriptArgsWithTypes(arrayOf(ctx.getBindings(ScriptContext.ENGINE_SCOPE)), types ?: emptyArray()) },
                    arrayOf(Bindings::class))
            )
        }
    }

    class SynchronizedScriptEngine(private val delegate: ScriptEngine) : ScriptEngine {
        @Synchronized
        @Throws(ScriptException::class)
        override fun eval(s: String, scriptContext: ScriptContext): Any? = delegate.eval(s, scriptContext)

        @Synchronized
        @Throws(ScriptException::class)
        override fun eval(reader: Reader, scriptContext: ScriptContext): Any? = delegate.eval(reader, scriptContext)

        @Synchronized
        @Throws(ScriptException::class)
        override fun eval(s: String): Any? = delegate.eval(s)

        @Synchronized
        @Throws(ScriptException::class)
        override fun eval(reader: Reader): Any? = delegate.eval(reader)

        @Synchronized
        @Throws(ScriptException::class)
        override fun eval(s: String, bindings: Bindings): Any? = delegate.eval(s, bindings)

        @Synchronized
        @Throws(ScriptException::class)
        override fun eval(reader: Reader, bindings: Bindings): Any? = delegate.eval(reader, bindings)

        @Synchronized
        override fun put(s: String, o: Any) = delegate.put(s, o)

        @Synchronized
        override fun get(s: String): Any? = delegate[s]

        @Synchronized
        override fun getBindings(i: Int): Bindings? = delegate.getBindings(i)

        @Synchronized
        override fun setBindings(bindings: Bindings, i: Int) = delegate.setBindings(bindings, i)

        @Synchronized
        override fun createBindings(): Bindings = delegate.createBindings()

        @Synchronized
        override fun getContext(): ScriptContext = delegate.context

        @Synchronized
        override fun setContext(scriptContext: ScriptContext) {
            delegate.context = scriptContext
        }

        @Synchronized
        override fun getFactory(): ScriptEngineFactory = delegate.factory
    }
}