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

import org.junit.Assert
import org.junit.Test
import org.scijava.script.AbstractScriptLanguageTest
import org.scijava.script.ScriptLanguage
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

    // TODO Why is kotlin not discovered by ScriptService?
    //      It is discovered in ImageJ/Fiji
//    val context: Context = Context(ScriptService::class.java)
//    val scriptService: ScriptService = context.getService(ScriptService::class.java)
//    val kotlinLang: ScriptLanguage = scriptService.getLanguageByName("Kotlin")
    val kotlinLang: ScriptLanguage = KotlinScriptLanguage()
    val engine: ScriptEngine = kotlinLang.scriptEngine

    @Test
    fun testDiscovery() = assertDiscovered(KotlinScriptLanguage::class.java)

    @Test
    @Throws(InterruptedException::class, ExecutionException::class, IOException::class, ScriptException::class)
    fun `test basic script engine eval`() = Assert.assertEquals(3, engine.eval("1 + 2"))


    @Test
    @Throws(ScriptException::class)
    fun `test basic engine eval with bindings`() {
        try {
            engine.put("Hello", ", SciJava!")
            Assert.assertEquals(", SciJava!", engine.eval("Hello"))
            Assert.assertEquals(", SciJava!", engine["Hello"])

            val bindings = engine.createBindings()
            bindings["base"] = E
            val script = "import kotlin.math.*; base.pow(0)"
            Assert.assertThrows(ScriptException::class.java) { engine.eval(script) }
            Assert.assertEquals(1.0, engine.eval(script, bindings) as Double, 0.0)
        } finally {
            engine.getBindings(ScriptContext.ENGINE_SCOPE)?.clear()
            Assert.assertNull(engine["Hello"])
        }
    }

    // Code copied from original Java test
    // FIXME: Calling methods on injected instance vars fails with
    // "unresolved reference". Nor can we store values into the bindings map!
    //	@Test
    //	public void testParameters() throws InterruptedException, ExecutionException,
    //		IOException, ScriptException
    //	{
    //		final Context context = new Context(ScriptService.class);
    //		final ScriptService scriptService = context.getService(ScriptService.class);
    //
    //		final String script = "" + //
    //			"// @ScriptService ss\n" + //
    //			"// @OUTPUT String language\n" + //
    //			"// @OUTPUT ScriptService ssOut\n" + //
    //			"println(\"==> \" + bindings[\"ss\"])\n" + //
    //			"bindings[\"ssOut\"] = bindings[\"ss\"]\n";
    ////			"println(\"==> \" + ss.canHandleFile(\"/Users/curtis\"))\n";
    ////			"val language = " + //
    ////			"bindings[\"ss\"].getLanguageByName(\"kotlin\").getLanguageName()\n";
    //		final ScriptModule m = scriptService.run("hello.kt", script, true).get();
    //
    //		final Object actual = m.getOutput("ssOut");
    //		final Object expected =
    //			scriptService.getLanguageByName("kotlin");
    //		assertEquals(expected, actual);
    //	}
}