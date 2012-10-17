package org.atemsource.atem.doc.javadoc.model;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;


@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class MethodDescription
{
	private String description;

	private String name;

	@XmlElementWrapper(name = "parameters")
	@XmlElement(name = "parameter")
	private final List<ParamDescription> parameters = new ArrayList<ParamDescription>();

	public void addParameter(ParamDescription parameter)
	{
		parameters.add(parameter);
	}

	public String getDescription()
	{
		return description;
	}

	public String getName()
	{
		return name;
	}

	public List<ParamDescription> getParameters()
	{
		return parameters;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public void setName(String name)
	{
		this.name = name;
	}
}
