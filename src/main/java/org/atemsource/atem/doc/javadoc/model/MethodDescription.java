package org.atemsource.atem.doc.javadoc.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;

@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class MethodDescription {
	private String name;
	private String description;
	@XmlElementWrapper(name = "parameters")
	@XmlElement(name = "parameter")
	private List<ParamDescription> parameters;

	public void addParameter(ParamDescription parameter) {
		parameters.add(parameter);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<ParamDescription> getParameters() {
		return parameters;
	}
}
