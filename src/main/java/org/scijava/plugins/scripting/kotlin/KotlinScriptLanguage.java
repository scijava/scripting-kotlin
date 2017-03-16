/*
 * #%L
 * JSR-223-compliant Kotlin scripting language plugin.
 * %%
 * Copyright (C) 2016 - 2017 Board of Regents of the University of
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

import java.util.Arrays;
import java.util.List;

import javax.script.ScriptEngine;

import org.jetbrains.kotlin.script.jsr223.KotlinJsr223JvmLocalScriptEngineFactory;
import org.scijava.plugin.Plugin;
import org.scijava.script.AdaptedScriptLanguage;
import org.scijava.script.ScriptLanguage;

/**
 * A SciJava {@link ScriptLanguage} for Kotlin.
 *
 * @author Curtis Rueden
 * @see ScriptEngine
 */
@Plugin(type = ScriptLanguage.class, name = "Kotlin")
public class KotlinScriptLanguage extends AdaptedScriptLanguage {

	public KotlinScriptLanguage() {
		super(new KotlinJsr223JvmLocalScriptEngineFactory());
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
}
