package org.atemsource.atem.doc.javadoc;

import javax.inject.Inject;

import org.atemsource.atem.api.EntityTypeRepository;
import org.atemsource.atem.api.attribute.Attribute;
import org.atemsource.atem.api.method.Method;
import org.atemsource.atem.api.type.EntityType;
import org.atemsource.atem.doc.javadoc.model.ClassDescription;
import org.atemsource.atem.doc.javadoc.model.FieldDescription;
import org.atemsource.atem.doc.javadoc.model.MethodDescription;
import org.atemsource.atem.doc.javadoc.model.ParamDescription;
import org.atemsource.atem.impl.common.method.MethodFactory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.test.DomainA;

@ContextConfiguration(locations = { "classpath:/test/atem/javadoc/javadoc.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class JavadocDataStoreTest {

	@Inject
	private EntityTypeRepository entityTypeRepository;

	@Inject
	private MethodFactory methodFactory;

	@Test
	public void testEntityTypeDescription() {
		EntityType<DomainA> entityType = entityTypeRepository.getEntityType(DomainA.class);
		EntityType<EntityType> entityTypeEntityType = entityTypeRepository.getEntityType(EntityType.class);

		Attribute metaAttribute = entityTypeEntityType.getMetaAttribute(JavadocDataStore.META_ATTRIBUTE_CODE);
		ClassDescription classDescription = (ClassDescription) metaAttribute.getValue(entityType);
		Assert.assertNotNull(classDescription);
		Assert.assertNotNull(classDescription.getDescription());
	}

	@Test
	public void testAttributeDescription() {
		EntityType<DomainA> entityType = entityTypeRepository.getEntityType(DomainA.class);
		EntityType<Attribute> attributeEntityType = entityTypeRepository.getEntityType(Attribute.class);

		FieldDescription fieldDescription = (FieldDescription) attributeEntityType.getMetaAttribute(
				JavadocDataStore.META_ATTRIBUTE_CODE).getValue(entityType.getAttribute("field1"));
		Assert.assertNotNull(fieldDescription);
		Assert.assertNotNull(fieldDescription.getDescription());
	}

	@Test
	public void testMethodDescription() {
		Method method = methodFactory.create(DomainA.class.getMethods()[0]);
		EntityType<Method> methodType = entityTypeRepository.getEntityType(Method.class);

		MethodDescription methodDescription = (MethodDescription) methodType.getMetaAttribute(
				JavadocDataStore.META_ATTRIBUTE_CODE).getValue(method);
		Assert.assertNotNull(methodDescription);
		Assert.assertEquals("add something",methodDescription.getDescription());
	}

	@Test
	public void testParameterDescription() {
		Method method = methodFactory.create(DomainA.class.getMethods()[0]);
		EntityType<Attribute> attributeEntityType = entityTypeRepository.getEntityType(Attribute.class);

		ParamDescription parameterDescription = (ParamDescription) attributeEntityType.getMetaAttribute(
				JavadocDataStore.META_ATTRIBUTE_CODE).getValue(method.getParameterType().getParameter(0));
		Assert.assertNotNull(parameterDescription);
		Assert.assertEquals("some value",parameterDescription.getDescription());
	}

}
