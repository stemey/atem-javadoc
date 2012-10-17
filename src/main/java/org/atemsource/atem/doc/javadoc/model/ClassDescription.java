package org.atemsource.atem.doc.javadoc.model;

import java.util.ArrayList;
import java.util.HashMap;
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
public class ClassDescription {
	private String description;
	private String name;
	@XmlElementWrapper(name = "fields")
	@XmlElement(name = "field")
	private List<FieldDescription> fields = new ArrayList<FieldDescription>();

	@XmlElementWrapper(name = "methods")
	@XmlElement(name = "method")
	private List<MethodDescription> methods = new ArrayList<MethodDescription>();

	public String getDescription() {
		return description;
	}

	private Map<String, FieldDescription> fieldMap;
	private Map<String, MethodDescription> methodMap;

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<FieldDescription> getFields() {
		return fields;
	}

	public void setFields(List<FieldDescription> fields) {
		this.fields = fields;
	}

	public void addField(FieldDescription fieldDescription) {
		fields.add(fieldDescription);
	}

	public void addMethod(MethodDescription methodDescription) {
		methods.add(methodDescription);
	}

	public FieldDescription getField(String code) {
		if (fieldMap == null) {
			synchronized (fieldMap) {
				if (fieldMap == null) {
					fieldMap = new HashMap<String, FieldDescription>();
					for (FieldDescription description : fields) {
						fieldMap.put(description.getName(), description);
					}
				}
			}
		}
		return fieldMap.get(code);
	}

	public MethodDescription getMethod(String code) {
		if (methodMap == null) {
			synchronized (methodMap) {
				if (methodMap == null) {
					methodMap = new HashMap<String, MethodDescription>();
					for (MethodDescription description : methods) {
						methodMap.put(description.getName(), description);
					}
				}
			}
		}
		return methodMap.get(code);
	}
}
