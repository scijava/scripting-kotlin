/*
 * #%L
 * JSR-223-compliant Kotlin scripting language plugin.
 * %%
 * Copyright (C) 2016 - 2022 Board of Regents of the University of
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

import org.junit.Assert
import org.junit.BeforeClass
import org.junit.Test
import org.scijava.Context
import org.scijava.script.AbstractScriptLanguageTest
import org.scijava.script.ScriptLanguage
import org.scijava.script.ScriptService
import java.io.IOException
import java.util.concurrent.ExecutionException
import javax.script.ScriptContext
import javax.script.ScriptEngine
import javax.script.ScriptException
import kotlin.math.E

/**
 * Kotlin unit tests.
 *
 * @author Curtis Rueden
 * @author Philipp Hanslovsky
 */
class KotlinTest : AbstractScriptLanguageTest() {

    @Test
    fun testDiscovery() = assertDiscovered(KotlinScriptLanguage::class.java)

    @Test
    @Throws(InterruptedException::class, ExecutionException::class, IOException::class, ScriptException::class)
    fun `test basic script engine eval`() = Assert.assertEquals(3, engine.eval("1 + 2"))

    @Test
    @Throws(ScriptException::class)
    fun `test basic engine eval with bindings`() {
        engine.put("Hello", ", SciJava!")
        Assert.assertEquals(", SciJava!", engine.eval("Hello"))
        Assert.assertEquals(", SciJava!", engine["Hello"])

        val bindings = engine.createBindings()
        bindings["base"] = E
        val script = "import kotlin.math.*; base.pow(0)"
        Assert.assertThrows(ScriptException::class.java) { engine.eval(script) }
        Assert.assertEquals(1.0, engine.eval(script, bindings) as Double, 0.0)

        // clear bindings and access variable Hello to cause ScriptException
        engine.getBindings(ScriptContext.ENGINE_SCOPE)?.clear()
        Assert.assertNull(engine["Hello"])
    }


    companion object {
        @BeforeClass
        @JvmStatic
        fun initKotlinLang() {
            val context = Context(ScriptService::class.java)
            val scriptService: ScriptService = context.getService(ScriptService::class.java)
            _kotlinLang = scriptService.getLanguageByName("Kotlin")
            _engine = _kotlinLang.scriptEngine
        }
        private lateinit var _kotlinLang: ScriptLanguage
        private lateinit var _engine: ScriptEngine
        val engine get() = _engine
    }
}
