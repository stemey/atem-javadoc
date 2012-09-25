package org.atemsource.atem.doc.javadoc.tool;

/*
 * File: JELDoclet.java
 * Purpose: A Doclet to be used with JavaDoc which will output XML with all of the information
 *    from the JavaDoc.
 * Date: Mar 2nd, 2003
 * 
 * History:
 * 		Sep 14th, 2005 - Updated by TP to allow multiple file output.
 * 					   - Added support for exceptions.
 * 					   - Added support for a few missing method modifiers (final, etc).
 *                     - Added support for xml namespaces.
 *                     
 *    Dec 8/9th, 2005 - updated by T.Zwolsky (all extensions marked thz.../thz):
 *      - added cmdline parameter -outputEncoding
 *      - bugfix: implements/interface node(s) were created but not inserted into class node
 *      - added exception comment (both here and in the xsd as optional element)
 *      - added cmdline parameter -filename
 *      - added output directory check
 *      - added some comments to readme
 *      - added test target in build.xml
 *      - added nested class in test classes (test/MyInterClass.java)
 *      - added xsl transformation jel2html.xsl
 *      
 *      Dec 16th, 2005 - updated by T.Zwolsky (all extensions marked thz.../thz):
 *      - added version here and in xsd
 *      - added admin stuff in xsd
 * 
 * Author: Jack D. Herrington <jack_d_herrington@codegeneration.net>
 * 		   Toby Patke 		  <toby_patke _?_ hotmail.com>
 * 
 * This source is covered by the Open Software Licenses (1.1)
 */

import com.sun.javadoc.*;
import com.sun.tools.doclets.standard.Standard;
import com.sun.tools.internal.xjc.runtime.JAXBContextFactory;

import java.io.File;
import java.io.Writer;
import java.util.Date;

import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.atemsource.atem.doc.javadoc.Options;
import org.atemsource.atem.doc.javadoc.model.ClassDescription;
import org.atemsource.atem.doc.javadoc.model.FieldDescription;

public class DocmlDoclet {

	/**
	 * Option check, forwards options to the standard doclet, if that one
	 * refuses them, they are sent to UmlGraph
	 */
	public static int optionLength(String option) {
		int result = Standard.optionLength(option);
		if (result != 0)
			return result;
		else
			return -1;
	}

	/**
	 * Standand doclet entry
	 * 
	 * @return
	 */
	public static LanguageVersion languageVersion() {
		return Standard.languageVersion();
	}

	public static boolean start(RootDoc root) throws JAXBException {
		Options options = getOptions(root);
		JAXBContext jaxbContext = JAXBContext.newInstance(
				ClassDescription.class, FieldDescription.class);
		Marshaller marshaller = jaxbContext.createMarshaller();
		for (ClassDoc classDoc : root.classes()) {
			ClassDescription classDescription = new ClassDescription();
			classDescription.setName(classDoc.qualifiedTypeName());
			classDescription.setDescription(classDoc.commentText());
			if (classDoc.isEnum()) {
				for (FieldDoc fieldDoc : classDoc.enumConstants()) {
					String enumDescription = fieldDoc.commentText();
				}
			}
			for (FieldDoc fieldDoc : classDoc.fields()) {
				FieldDescription fieldDescription = new FieldDescription();
				fieldDescription.setName(fieldDoc.name());
				fieldDescription.setDescription(fieldDoc.commentText());
				classDescription.addField(fieldDescription);
			}
			String file = classDoc.qualifiedTypeName().replace(".", "/")
					+ ".docml";

			File docFile = new File(options.getOutputDirectory(), file);
			if (docFile.exists() && docFile.isDirectory()) {
				docFile.delete();
			}
			docFile.getParentFile().mkdirs();
			marshaller.marshal(classDescription, docFile);
		}

		return true;
	}

	public static Options getOptions(RootDoc root) {
		Options options = new Options();

		String[][] optionArray = root.options();
		for (int opt = 0; opt < optionArray.length; opt++) {
			if (optionArray[opt][0].compareToIgnoreCase("-d") == 0) {
				options.setOutputDirectory(new File(optionArray[opt][1]));

			}
		}
		return options;

	}
}
