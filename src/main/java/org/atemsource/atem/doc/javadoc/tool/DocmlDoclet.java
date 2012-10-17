package org.atemsource.atem.doc.javadoc.tool;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.atemsource.atem.doc.javadoc.Options;
import org.atemsource.atem.doc.javadoc.model.ClassDescription;
import org.atemsource.atem.doc.javadoc.model.FieldDescription;
import org.atemsource.atem.doc.javadoc.model.MethodDescription;
import org.atemsource.atem.doc.javadoc.model.ParamDescription;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.FieldDoc;
import com.sun.javadoc.LanguageVersion;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.ParamTag;
import com.sun.javadoc.Parameter;
import com.sun.javadoc.RootDoc;
import com.sun.tools.doclets.standard.Standard;

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
			for (MethodDoc methodDoc : classDoc.methods()) {
				MethodDescription methodDescription = new MethodDescription();
				methodDescription.setName(methodDoc.name());
				methodDescription.setDescription(methodDoc.commentText());
				Map<String,ParamDescription> nameToParam = new HashMap<String,ParamDescription>();
				for (Parameter parameter:methodDoc.parameters()) {
					ParamDescription paramDescription=new ParamDescription();
					paramDescription.setName(parameter.name());
					methodDescription.addParameter(paramDescription);
					nameToParam.put(parameter.name(), paramDescription);
				}
				for(ParamTag paramTag:methodDoc.paramTags()) {
					ParamDescription paramDescription = nameToParam.get(paramTag.parameterName());
				paramDescription.setDescription(paramTag.parameterComment());
				}
				
				classDescription.addMethod(methodDescription);
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
