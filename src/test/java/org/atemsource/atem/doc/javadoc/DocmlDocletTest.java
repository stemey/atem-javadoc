package org.atemsource.atem.doc.javadoc;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import junit.framework.Assert;

import org.atemsource.atem.doc.javadoc.model.ClassDescription;
import org.atemsource.atem.doc.javadoc.model.FieldDescription;
import org.junit.Test;

import com.sun.tools.javadoc.Main;

public class DocmlDocletTest {

	
	@Test
	public void testJaxb() throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(ClassDescription.class,FieldDescription.class);
		StringWriter writer = new StringWriter();
		ClassDescription classDescription = new ClassDescription();
		classDescription.setName("className");
		classDescription.setDescription("classDescription");
		FieldDescription fieldDescription = new FieldDescription();
		fieldDescription.setName("fieldName");
		fieldDescription.setDescription("fieldDescription");
		classDescription.addField(fieldDescription);
		
		jaxbContext.createMarshaller().marshal(classDescription, writer);
		Assert.assertNotNull(writer.toString());
		System.out.println(writer.toString());
	}
}
