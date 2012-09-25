package org.atemsource.atem.doc.javadoc;

import java.io.File;
import java.io.Serializable;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.atemsource.atem.api.EntityTypeRepository;
import org.atemsource.atem.api.attribute.Attribute;
import org.atemsource.atem.api.attribute.relation.SingleAttribute;
import org.atemsource.atem.api.extension.MetaAttributeService;
import org.atemsource.atem.api.service.AttributeQuery;
import org.atemsource.atem.api.service.FindByAttributeService;
import org.atemsource.atem.api.service.FindByTypedIdService;
import org.atemsource.atem.api.service.IdentityService;
import org.atemsource.atem.api.service.SingleAttributeQuery;
import org.atemsource.atem.api.type.EntityType;
import org.atemsource.atem.doc.javadoc.model.ClassDescription;
import org.atemsource.atem.doc.javadoc.model.FieldDescription;
import org.atemsource.atem.impl.meta.EntityTypeSubrepository;
import org.atemsource.atem.spi.EntityTypeCreationContext;

public class JavadocDataStore implements MetaDataService, MetaAttributeService {

	private Map<String, ClassDescription> entityTypeData = new HashMap<String, ClassDescription>();

	@PostConstruct
	public void initialize() throws JAXBException {
		context = JAXBContext.newInstance(ClassDescription.class,
				FieldDescription.class);
	}

	private JAXBContext context;

	@Override
	public Object getMetaData(Object targetEntity, XmlMetaAttribute attribute) {
		// TODO Auto-generated method stub

		// attribute is the attribute in The xml object referencing the object
		// outside
		// target entity is the referenced object

		if (targetEntity instanceof Attribute) {
			Attribute targetAttribute = (Attribute) targetEntity;
			ClassDescription value = entityTypeData.get(targetAttribute
					.getEntityType().getCode());
			if (value == null) {
				load(targetAttribute.getEntityType());
				value = entityTypeData.get(targetAttribute.getEntityType()
						.getCode());
				return value.getField(targetAttribute.getCode());
			} else {
				return value.getField(targetAttribute.getCode());
			}
		} else if (targetEntity instanceof EntityType) {
			EntityType targetType = (EntityType) targetEntity;
			ClassDescription value = entityTypeData.get(targetType.getCode());
			if (value == null) {
				load(targetType);
				return entityTypeData.get(targetType.getCode());
			} else {
				return value;
			}
		} else {
			return null;
		}

	}

	private void load(EntityType entityType) {
		URL resource = getClass().getResource(
				"/"+entityType.getCode().replace(".", "/") + ".docml");
		File file = new File(resource.getFile());
		if (file.exists()) {
			ClassDescription object;
			try {
				object = (ClassDescription) context.createUnmarshaller()
						.unmarshal(file);
				entityTypeData.put(entityType.getCode(), object);
			} catch (JAXBException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public <J> SingleAttribute<J> addSingleMetaAttribute(String name,
			EntityType<?> holderType, EntityType<J> metaDataType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <J> SingleAttribute<J> getMetaAttribute(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getMetaData(String name, Object holder) {
		// TODO Auto-generated method stub
		return null;
	}

	public static final String META_ATTRIBUTE_CODE = "javadocDescription";

	@Override
	public void initialize(EntityTypeCreationContext ctx) {
		EntityType attributeType = ctx.getEntityTypeReference(Attribute.class);
		EntityType entityType = ctx.getEntityTypeReference(EntityType.class);
		EntityType fieldDescription = ctx
				.getEntityTypeReference(FieldDescription.class);
		EntityType classDescription = ctx
				.getEntityTypeReference(ClassDescription.class);
		XmlMetaAttribute attributeDescription = new XmlMetaAttribute(
				attributeType, fieldDescription, this, META_ATTRIBUTE_CODE);
		XmlMetaAttribute entityTypeDescription = new XmlMetaAttribute(
				entityType, classDescription, this, META_ATTRIBUTE_CODE);
		ctx.addMetaAttribute(entityType, entityTypeDescription);
		ctx.addMetaAttribute(attributeType, attributeDescription);
	}
}
