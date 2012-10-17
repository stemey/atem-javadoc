package org.atemsource.atem.doc.javadoc;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import org.atemsource.atem.api.attribute.Attribute;
import org.atemsource.atem.api.attribute.relation.SingleAttribute;
import org.atemsource.atem.api.extension.EntityTypeRepositoryPostProcessor;
import org.atemsource.atem.api.extension.MetaAttributeService;
import org.atemsource.atem.api.infrastructure.exception.TechnicalException;
import org.atemsource.atem.api.type.EntityType;
import org.atemsource.atem.doc.javadoc.model.ClassDescription;
import org.atemsource.atem.doc.javadoc.model.FieldDescription;
import org.atemsource.atem.impl.meta.MetaAttribute;
import org.atemsource.atem.impl.meta.MetaDataService;
import org.atemsource.atem.spi.EntityTypeCreationContext;


public class JavadocDataStore implements MetaDataService, EntityTypeRepositoryPostProcessor, MetaAttributeService
{

	public static final String META_ATTRIBUTE_CODE = "javadocDescription";

	private JAXBContext context;

	private final Map<String, ClassDescription> entityTypeData = new HashMap<String, ClassDescription>();

	@Override
	public <J> SingleAttribute<J> addSingleMetaAttribute(String name, EntityType<?> holderType,
		EntityType<J> metaDataType)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getMetaData(Object targetEntity, MetaAttribute attribute)
	{
		// TODO Auto-generated method stub

		// attribute is the attribute in The xml object referencing the object
		// outside
		// target entity is the referenced object

		if (targetEntity instanceof Attribute)
		{
			Attribute targetAttribute = (Attribute) targetEntity;
			ClassDescription value = entityTypeData.get(targetAttribute.getEntityType().getCode());
			if (value == null && !entityTypeData.containsKey(targetAttribute.getEntityType().getCode()))
			{
				load(targetAttribute.getEntityType());
				value = entityTypeData.get(targetAttribute.getEntityType().getCode());
				if (value == null)
				{
					return null;
				}
				return value.getField(targetAttribute.getCode());
			}
			else if (value == null)
			{
				return null;
			}
			else
			{
				return value.getField(targetAttribute.getCode());
			}
		}
		else if (targetEntity instanceof EntityType)
		{
			EntityType targetType = (EntityType) targetEntity;
			ClassDescription value = entityTypeData.get(targetType.getCode());
			if (value == null)
			{
				load(targetType);
				return entityTypeData.get(targetType.getCode());
			}
			else
			{
				return value;
			}
		}
		else
		{
			return null;
		}

	}

	@Override
	public void initialize(EntityTypeCreationContext ctx)
	{
		try
		{
			context = JAXBContext.newInstance(ClassDescription.class, FieldDescription.class);
			EntityType attributeType = ctx.getEntityTypeReference(Attribute.class);
			EntityType entityType = ctx.getEntityTypeReference(EntityType.class);
			EntityType fieldDescription = ctx.getEntityTypeReference(FieldDescription.class);
			EntityType classDescription = ctx.getEntityTypeReference(ClassDescription.class);
			MetaAttribute attributeDescription =
				new MetaAttribute(attributeType, fieldDescription, this, META_ATTRIBUTE_CODE);
			MetaAttribute entityTypeDescription =
				new MetaAttribute(entityType, classDescription, this, META_ATTRIBUTE_CODE);
			ctx.addMetaAttribute(entityType, entityTypeDescription);
			ctx.addMetaAttribute(attributeType, attributeDescription);
		}
		catch (JAXBException e)
		{
			throw new TechnicalException("errors with xml binding", e);
		}

	}

	private void load(EntityType entityType)
	{
		URL resource = getClass().getResource("/" + entityType.getCode().replace(".", "/") + ".docml");
		if (resource == null)
		{
			entityTypeData.put(entityType.getCode(), null);
			return;
		}
		File file = new File(resource.getFile());
		if (file.exists())
		{
			ClassDescription object;
			try
			{
				object = (ClassDescription) context.createUnmarshaller().unmarshal(file);
				entityTypeData.put(entityType.getCode(), object);
			}
			catch (JAXBException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void setMetaData(Object entity, Object value, MetaAttribute attribute)
	{
		throw new UnsupportedOperationException("cannot be modified");
	}
}
