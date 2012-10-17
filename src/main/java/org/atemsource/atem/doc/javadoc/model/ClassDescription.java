package org.atemsource.atem.doc.javadoc.model;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


@XmlType
@XmlRootElement(name = "class")
@XmlAccessorType(XmlAccessType.FIELD)
public class ClassDescription
{
	private String description;

	private Map<String, FieldDescription> fieldMap;

	@XmlElementWrapper(name = "fields")
	@XmlElement(name = "field")
	private List<FieldDescription> fields = new ArrayList<FieldDescription>();

	private Map<String, MethodDescription> methodMap;

	@XmlElementWrapper(name = "methods")
	@XmlElement(name = "method")
	private final List<MethodDescription> methods = new ArrayList<MethodDescription>();

	private String name;

	public void addField(FieldDescription fieldDescription)
	{
		fields.add(fieldDescription);
	}

	public void addMethod(MethodDescription methodDescription)
	{
		methods.add(methodDescription);
	}

	public String getDescription()
	{
		return description;
	}

	public FieldDescription getField(String code)
	{
		if (fieldMap == null)
		{
			if (fieldMap == null)
			{
				fieldMap = new Hashtable<String, FieldDescription>();
				for (FieldDescription description : fields)
				{
					fieldMap.put(description.getName(), description);
				}
			}
		}
		return fieldMap.get(code);
	}

	public List<FieldDescription> getFields()
	{
		return fields;
	}

	public MethodDescription getMethod(String code)
	{
		if (methodMap == null)
		{
			methodMap = new Hashtable<String, MethodDescription>();
			synchronized (methodMap)
			{
				for (MethodDescription description : methods)
				{
					methodMap.put(description.getName(), description);
				}
			}
		}
		return methodMap.get(code);
	}

	public String getName()
	{
		return name;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public void setFields(List<FieldDescription> fields)
	{
		this.fields = fields;
	}

	public void setName(String name)
	{
		this.name = name;
	}
}
