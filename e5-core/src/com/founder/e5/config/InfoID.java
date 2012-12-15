package com.founder.e5.config;

/**
 * Created on 2005-7-19
 * @author Gong Lijie
 */
public class InfoID
{
	private String name;
	private String type;
	private String param;
	
	public String toString(){
		return (new StringBuffer()
				.append("[name:").append(name)
				.append(",type:").append(type)
				.append(",param:").append(param)
				.append("]")
				).toString();
	}
	/**
	 * @return Returns the name.
	 */
	public String getName()
	{
		return name;
	}
	/**
	 * @param name The name to set.
	 */
	public void setName(String name)
	{
		this.name = name;
	}
	/**
	 * @return Returns the param.
	 */
	public String getParam()
	{
		return param;
	}
	/**
	 * @param param The param to set.
	 */
	public void setParam(String param)
	{
		this.param = param;
	}
	/**
	 * @return Returns the type.
	 */
	public String getType()
	{
		return type;
	}
	/**
	 * @param type The type to set.
	 */
	public void setType(String type)
	{
		this.type = type;
	}
}
