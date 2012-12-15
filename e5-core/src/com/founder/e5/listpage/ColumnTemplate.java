package com.founder.e5.listpage;

public class ColumnTemplate 
{
	private String name;
	private String header;
	private String width;
	private String template;
	
	public ColumnTemplate() 
	{
		super();
	}

	public String getHeader() 
	{
		return header;
	}

	public void setHeader(String header) 
	{
		this.header = header;
	}

	public String getWidth() 
	{
		return width;
	}

	public void setWidth(String width) 
	{
		this.width = width;
	}

	public String getTemplate() 
	{
		return template;
	}

	public void setTemplate(String template) 
	{
		this.template = template;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}	
}
