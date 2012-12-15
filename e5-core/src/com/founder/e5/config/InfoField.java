package com.founder.e5.config;

/**
 * 与配置文件对应的平台字段信息
 * Created on 2005-7-13
 * @author Gong Lijie
 */
public class InfoField
{
	private String id;
	private String code;
	private String name;
	private String type;
	private String length;
	private String nullable;
	private String defaultValue;
	
	public String getDefaultValue()
	{
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue)
	{
		this.defaultValue = defaultValue;
	}
	/**
	 * @return Returns the code.
	 */
	public String getCode()
	{
		return code;
	}
	/**
	 * @param code The code to set.
	 */
	public void setCode(String code)
	{
		this.code = code;
	}
	/**
	 * @return Returns the id.
	 */
	public String getId()
	{
		return id;
	}
	/**
	 * @param id The id to set.
	 */
	public void setId(String id)
	{
		this.id = id;
	}
	/**
	 * @return Returns the length.
	 */
	public String getLength()
	{
		return length;
	}
	/**
	 * @param length The length to set.
	 */
	public void setLength(String length)
	{
		this.length = length;
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
	 * @return Returns the nullable.
	 */
	public String getNullable()
	{
		return nullable;
	}
	/**
	 * @param nullable The nullable to set.
	 */
	public void setNullable(String nullable)
	{
		this.nullable = nullable;
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
	
	public String toString(){
		return (new StringBuffer()
				.append("[id:").append(id)
				.append(",name:").append(name)
				.append(",Code:").append(code)
				.append(",type:").append(type)
				.append(",length:").append(length)
				.append(",nullable:").append(nullable)
				.append("]")
				).toString();
	}
}
