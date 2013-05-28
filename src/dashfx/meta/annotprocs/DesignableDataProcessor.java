/*
 * Copyright (C) 2013 patrick
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package dashfx.meta.annotprocs;

import dashfx.lib.controls.DesignableData;
import java.io.*;
import java.util.*;
import javax.annotation.processing.*;
import javax.lang.model.*;
import javax.lang.model.element.*;
import javax.tools.*;

/**
 *
 * @author patrick
 */
@SupportedAnnotationTypes("dashfx.lib.controls.DesignableData")
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class DesignableDataProcessor extends javax.annotation.processing.AbstractProcessor
{
	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv)
	{
		ArrayList<String> fqdns = new ArrayList<>();
		for (Element elem : roundEnv.getElementsAnnotatedWith(DesignableData.class))
		{
			if (elem.getKind() != ElementKind.CLASS)
				continue;
			fqdns.add(((TypeElement) elem).getQualifiedName().toString());
			DesignableData designable = elem.getAnnotation(DesignableData.class);
			String message = "data annotation found in " + ((TypeElement) elem).getQualifiedName().toString()
							 + " with value '" + designable.name()+ "'";
			processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, message);
		}
		if (fqdns.isEmpty())
			return true;
		try
		{
			JavaFileObject f = processingEnv.getFiler().createSourceFile("dashfx.meta.registers.DesignableDataIntRes");
			//Add the content to the newly generated file.
			try (Writer w = f.openWriter())
			{
				PrintWriter pw = new PrintWriter(w);
				pw.println("package dashfx.meta.registers;");
				pw.println("public class DesignableDataIntRes\n{");
				pw.println("    public static final Class[] KNOWN = { ");
				for (String string : fqdns)
				{
					// note: == works here and is much better
					pw.println(string + ".class" + (fqdns.get(fqdns.size() - 1) == string ? "" : ","));
				}
				pw.println("    };");
				pw.println("}");
				pw.flush();
			}
		}
		catch (IOException x)
		{
			processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,
													 x.toString());
		}
		return true;
	}
}
