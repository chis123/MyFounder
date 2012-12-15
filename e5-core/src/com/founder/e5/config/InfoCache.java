package com.founder.e5.config;

/**
 * �������ļ���Ӧ��һ��������Ϣ
 * @created on 2005-7-13
 * @author Gong Lijie
 */
public class InfoCache
{
	private String name;
	private String invokeClass;
	
	/**
	 * @return Returns the invokeClass.
	 */
	public String getInvokeClass()
	{
		return invokeClass;
	}
	/**
	 * @param invokeClass The invokeClass to set.
	 */
	public void setInvokeClass(String invokeClass)
	{
		this.invokeClass = invokeClass;
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
				.append("[name:").append(name)
				.append(",invokeClass:").append(invokeClass)
				.append("]")
				).toString();
	}
}
