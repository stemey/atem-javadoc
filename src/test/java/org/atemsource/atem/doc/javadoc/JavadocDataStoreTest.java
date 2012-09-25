package org.atemsource.atem.doc.javadoc;

import javax.inject.Inject;

import org.atemsource.atem.api.EntityTypeRepository;
import org.atemsource.atem.api.attribute.Attribute;
import org.atemsource.atem.api.type.EntityType;
import org.atemsource.atem.doc.javadoc.model.ClassDescription;
import org.atemsource.atem.doc.javadoc.model.FieldDescription;
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
	
	@Test
	public void testEntityTypeDescription() {
		EntityType<DomainA> entityType = entityTypeRepository.getEntityType(DomainA.class);
		EntityType<EntityType> entityTypeEntityType = entityTypeRepository.getEntityType(EntityType.class);
		
		ClassDescription classDescription = (ClassDescription) entityTypeEntityType.getMetaAttribute(JavadocDataStore.META_ATTRIBUTE_CODE).getValue(entityType);
		Assert.assertNotNull(classDescription);
		Assert.assertNotNull(classDescription.getDescription());
	}
	@Test
	public void testAttributeDescription() {
		EntityType<DomainA> entityType = entityTypeRepository.getEntityType(DomainA.class);
		EntityType<Attribute> attributeEntityType = entityTypeRepository.getEntityType(Attribute.class);
		
		FieldDescription fieldDescription = (FieldDescription) attributeEntityType.getMetaAttribute(JavadocDataStore.META_ATTRIBUTE_CODE).getValue(entityType.getAttribute("field1"));
		Assert.assertNotNull(fieldDescription);
		Assert.assertNotNull(fieldDescription.getDescription());
	}

}
