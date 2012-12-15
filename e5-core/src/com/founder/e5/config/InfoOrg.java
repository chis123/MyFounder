package com.founder.e5.config;

/**
 * 与配置文件对应的机构类型信息
 * Created on 2005-7-13
 * @author Gong Lijie
 */
public class InfoOrg
{
	private String id;
	private String name;
	
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
	public String toString(){
		return (new StringBuffer()
				.append("[id:").append(id)
				.append(",name:").append(name)
				.append("]")
				).toString();
	}
}
