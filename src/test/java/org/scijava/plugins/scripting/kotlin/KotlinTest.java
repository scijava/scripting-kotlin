/*
 * #%L
 * SciJava wrapper around DynKT, a Kotlin JSR-223 implementation.
 * %%
 * Copyright (C) 2016 Board of Regents of the University of
 * Wisconsin-Madison.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

package org.scijava.plugins.scripting.kotlin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.xafero.dynkt.KotlinScriptEngine;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.scijava.Context;
import org.scijava.script.ScriptLanguage;
import org.scijava.script.ScriptModule;
import org.scijava.script.ScriptService;

/**
 * Tests the {@link KotlinScriptLanguage}.
 *
 * @author Curtis Rueden
 */
public class KotlinTest {

	private Context context;
	private ScriptService scriptService;

	@Before
	public void setUp() {
		context = new Context(ScriptService.class);
		scriptService = context.getService(ScriptService.class);
	}

	@After
	public void tearDown() {
		if (context != null) context.dispose();
	}

	@Test
	public void testVeryBasic() throws InterruptedException, ExecutionException,
		IOException, ScriptException
	{
		final String script = "\"Hello\"\n";
		final ScriptModule m = scriptService.run("hello.kts", script, true).get();
		final Object result = m.getReturnValue();
		assertEquals("Hello", result.toString());
	}

	@Test
	public void testBasic() throws InterruptedException, ExecutionException,
		IOException, ScriptException
	{
		final String script = "" + //
			"fun fib(n: Int) {\n" + //
			"  if (n <= 1) return 1\n" + //
			"  return fib(n) + fib(n - 1)\n" + //
			"}\n" + //
			"fib(10)";
		final ScriptModule m = scriptService.run("fib.kts", script, true).get();
		final Object result = m.getReturnValue();
		assertEquals("3", result.toString());
	}

	@Test
	public void testParameters() throws InterruptedException, ExecutionException,
		IOException, ScriptException
	{
		final String langClass = KotlinScriptLanguage.class.getName();
		final String script = "" + //
			"// @OUTPUT " + langClass + " language\n" + //
			"// @OUTPUT String langName\n" + //
			"import " + langClass + "\n" + //
			"language = KotlinScriptLanguage()\n" + //
			"langName = language.getEngineName()\n";
		final ScriptModule m = scriptService.run("params.kts", script, true).get();

		final Object language = m.getOutput("language");
		final Object langName = m.getOutput("langName");
		assertEquals(KotlinScriptLanguage.class, language.getClass());
		assertEquals("Kotlin", langName);
	}

	@Test
	public void testBindings() throws ScriptException {
		final ScriptLanguage language =
			scriptService.getLanguageByExtension("Kotlin");
		final ScriptEngine engine = language.getScriptEngine();
		assertSame(KotlinScriptEngine.class, engine.getClass());

		engine.put("hello", 17);
		assertEquals(17, engine.eval("hello"));
		assertEquals(17, engine.get("hello"));

		final Bindings bindings = engine.getBindings(ScriptContext.ENGINE_SCOPE);
		bindings.clear();
		assertNull(engine.get("hello"));
		assertNull(engine.get("goodbye"));
	}

}
