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

package org.scijava.plugins.scripting.kotlin;

import org.jetbrains.kotlin.cli.common.repl.KotlinJsr223JvmScriptEngineFactoryBase;
import org.scijava.plugin.Plugin;
import org.scijava.script.AdaptedScriptLanguage;
import org.scijava.script.ScriptLanguage;

import javax.script.*;
import java.io.Reader;
import java.util.Arrays;
import java.util.List;

/**
 * A SciJava {@link ScriptLanguage} for Kotlin.
 *
 * @author Curtis Rueden
 * @see ScriptEngine
 */
@Plugin(type = ScriptLanguage.class, name = "Kotlin")
public class KotlinScriptLanguage extends AdaptedScriptLanguage {

	public KotlinScriptLanguage() {
		super(new MyScriptEngineFactory());
	}

	@Override
	public List<String> getNames() {
		// NB: The wrapped ScriptEngineFactory does not include Kotlin in its list.
		return Arrays.asList("kotlin", "Kotlin");
	}

	@Override
	public List<String> getExtensions() {
		// NB: The wrapped ScriptEngineFactory does not include .kt in its list.
		return Arrays.asList("kt", "kts");
	}

	public static class MyScriptEngineFactory extends KotlinJsr223JvmScriptEngineFactoryBase {
		@Override
		public ScriptEngine getScriptEngine() {
			return new SynchronizedScriptEngine(new ScriptEngineManager().getEngineByExtension("kts"));
		}
	}

	public static class SynchronizedScriptEngine implements ScriptEngine {

		private final ScriptEngine delegate;

		public SynchronizedScriptEngine(final ScriptEngine delegate) {
			this.delegate = delegate;
		}

		@Override
		public synchronized Object eval(String s, ScriptContext scriptContext) throws ScriptException {
			return delegate.eval(s, scriptContext);
		}

		@Override
		public synchronized Object eval(Reader reader, ScriptContext scriptContext) throws ScriptException {
			return delegate.eval(reader, scriptContext);
		}

		@Override
		public synchronized Object eval(String s) throws ScriptException {
			return delegate.eval(s);
		}

		@Override
		public synchronized Object eval(Reader reader) throws ScriptException {
			return delegate.eval(reader);
		}

		@Override
		public synchronized Object eval(String s, Bindings bindings) throws ScriptException {
			return delegate.eval(s, bindings);
		}

		@Override
		public synchronized Object eval(Reader reader, Bindings bindings) throws ScriptException {
			return delegate.eval(reader, bindings);
		}

		@Override
		public synchronized void put(String s, Object o) {
			delegate.put(s, o);
		}

		@Override
		public synchronized Object get(String s) {
			return delegate.get(s);
		}

		@Override
		public synchronized Bindings getBindings(int i) {
			return delegate.getBindings(i);
		}

		@Override
		public synchronized void setBindings(Bindings bindings, int i) {
			delegate.setBindings(bindings, i);
		}

		@Override
		public synchronized Bindings createBindings() {
			return delegate.createBindings();
		}

		@Override
		public synchronized ScriptContext getContext() {
			return delegate.getContext();
		}

		@Override
		public synchronized void setContext(ScriptContext scriptContext) {
			delegate.setContext(scriptContext);
		}

		@Override
		public synchronized ScriptEngineFactory getFactory() {
			return delegate.getFactory();
		}
	}
}
