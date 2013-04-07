package org.atemsource.atem.doc.javadoc;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import org.atemsource.atem.api.attribute.Attribute;
import org.atemsource.atem.api.attribute.relation.SingleAttribute;
import org.atemsource.atem.api.extension.EntityTypeRepositoryPostProcessor;
import org.atemsource.atem.api.extension.MetaAttributeService;
import org.atemsource.atem.api.infrastructure.exception.TechnicalException;
import org.atemsource.atem.api.method.ParameterType;
import org.atemsource.atem.api.type.EntityType;
import org.atemsource.atem.doc.javadoc.model.ClassDescription;
import org.atemsource.atem.doc.javadoc.model.FieldDescription;
import org.atemsource.atem.doc.javadoc.model.MethodDescription;
import org.atemsource.atem.impl.meta.MetaAttribute;
import org.atemsource.atem.impl.meta.MetaDataService;
import org.atemsource.atem.spi.EntityTypeCreationContext;

public class JavadocDataStore implements MetaDataService, EntityTypeRepositoryPostProcessor, MetaAttributeService {

	public static final String META_ATTRIBUTE_CODE = "javadocDescription";

	private JAXBContext context;

	private final Map<String, ClassDescription> entityTypeData = new ConcurrentHashMap<String, ClassDescription>();

	@Override
	public <J> SingleAttribute<J> addSingleMetaAttribute(String name, EntityType<?> holderType,
			EntityType<J> metaDataType) {
		throw new UnsupportedOperationException("not implemented");
	}

	private ClassDescription getIfAvailable(ClassDescription classDescription) {
		return classDescription == null || classDescription.getName() == null ? null : classDescription;
	}

	public ClassDescription getDescription(Class<?> clazz) {
		if (entityTypeData.containsKey(clazz.getName())) {
			return getIfAvailable(entityTypeData.get(clazz.getName()));
		} else {
			load(clazz.getName());
			ClassDescription classDescription = getIfAvailable(entityTypeData.get(clazz.getName()));
			return classDescription;
		}
	}

	@Override
	public Object getMetaData(Object targetEntity, MetaAttribute attribute) {
		// TODO Auto-generated method stub

		// attribute is the attribute in The xml object referencing the object
		// outside
		// target entity is the referenced object

		if (targetEntity instanceof Attribute && ((Attribute) targetEntity).getEntityType() instanceof ParameterType) {
			// it's a method parameter
			ParameterType parameterType = (ParameterType) ((Attribute) targetEntity).getEntityType();
			Method method = parameterType.getMethod().getJavaMethod();
			Attribute targetAttribute = (Attribute) targetEntity;
			ClassDescription value = getOrLoad(parameterType.getMethod().getEntityType().getCode());
			int index = parameterType.indexOf(targetAttribute);
			if (value != null) {

				MethodDescription methodDescription = value.getMethod(method.getName());
				if (methodDescription == null) {
					return null;
				} else {
					return methodDescription.getParameters().get(index);
				}
			} else {
				return null;
			}

		} else if (targetEntity instanceof org.atemsource.atem.api.method.Method) {
			org.atemsource.atem.api.method.Method method = (org.atemsource.atem.api.method.Method) targetEntity;
			ClassDescription value = getOrLoad(method.getEntityType().getCode());
			if (value != null) {
				return value.getMethod(method.getJavaMethod().getName());
			} else {
				return null;
			}

		} else if (targetEntity instanceof Attribute) {
			Attribute targetAttribute = (Attribute) targetEntity;
			ClassDescription value = getOrLoad(targetAttribute.getEntityType().getCode());
			if (value != null) {
				return value.getField(targetAttribute.getCode());
			} else {
				return null;
			}

		} else if (targetEntity instanceof EntityType) {
			EntityType targetType = (EntityType) targetEntity;
			ClassDescription value = getOrLoad(targetType.getCode());
			if (value != null) {
				return value;
			} else {
				return null;
			}
		} else {
			return null;
		}

	}

	private ClassDescription getOrLoad(String className) {
		ClassDescription classDescription = entityTypeData.get(className);
		if (classDescription == null) {
			load(className);
			classDescription = entityTypeData.get(className);
		}
		if (classDescription.getName() == null) {
			return null;
		} else {
			return classDescription;
		}
	}

	@Override
	public void initialize(EntityTypeCreationContext ctx) {
		try {
			context = JAXBContext.newInstance(ClassDescription.class, FieldDescription.class);
			EntityType attributeType = ctx.getEntityTypeReference(Attribute.class);
			EntityType entityType = ctx.getEntityTypeReference(EntityType.class);
			EntityType methodType = ctx.getEntityTypeReference(org.atemsource.atem.api.method.Method.class);
			EntityType fieldDescription = ctx.getEntityTypeReference(FieldDescription.class);
			EntityType classDescription = ctx.getEntityTypeReference(ClassDescription.class);
			EntityType methodDescription = ctx.getEntityTypeReference(MethodDescription.class);
			MetaAttribute attributeDescription = new MetaAttribute(attributeType, fieldDescription, this,
					META_ATTRIBUTE_CODE);
			MetaAttribute entityTypeDescription = new MetaAttribute(entityType, classDescription, this,
					META_ATTRIBUTE_CODE);
			ctx.addMetaAttribute(entityType, entityTypeDescription);
			ctx.addMetaAttribute(attributeType, attributeDescription);
			if (methodType != null) {
				MetaAttribute methodTypeDescription = new MetaAttribute(methodType, methodDescription, this,
						META_ATTRIBUTE_CODE);
				ctx.addMetaAttribute(methodType, methodTypeDescription);
			}
		} catch (JAXBException e) {
			throw new TechnicalException("errors with xml binding", e);
		}

	}

	private void load(Class clazz) {
		load(clazz.getName());
	}

	private void load(EntityType<?> entityType) {
		load(entityType.getCode());
	}

	private void load(String className) {
		URL resource = getClass().getResource("/" + className.replace(".", "/") + ".docml");
		if (resource == null) {
			entityTypeData.put(className, new ClassDescription());
			return;
		}
		File file = new File(resource.getFile());
		if (file.exists()) {
			ClassDescription object;
			try {
				object = (ClassDescription) context.createUnmarshaller().unmarshal(file);
				entityTypeData.put(className, object);
			} catch (JAXBException e) {
				entityTypeData.put(className, new ClassDescription());
			}
		} else {
			entityTypeData.put(className, new ClassDescription());
		}
	}

	@Override
	public void setMetaData(Object entity, Object value, MetaAttribute attribute) {
		throw new UnsupportedOperationException("cannot be modified");
	}
}
