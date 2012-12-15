package com.founder.e5.config;

/**
 * 与配置文件对应的工厂类单例的配置信息
 * Created on 2005-7-19
 * @author Gong Lijie
 */
public class InfoFactory
{
	private String id;
	private String beanClass;
	private String invokeClass;
	private String invokeMethod;
	public String toString(){
		return (new StringBuffer()
				.append("[id:").append(id)
				.append(",beanClass:").append(beanClass)
				.append(",invokeClass:").append(invokeClass)
				.append(",invokeMethod:").append(invokeMethod)
				.append("]")
				).toString();
	}
	/**
	 * @return Returns the beanClass.
	 */
	public String getBeanClass()
	{
		return beanClass;
	}
	/**
	 * @param beanClass The beanClass to set.
	 */
	public void setBeanClass(String beanClass)
	{
		this.beanClass = beanClass;
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
	 * @return Returns the invokeMethod.
	 */
	public String getInvokeMethod()
	{
		return invokeMethod;
	}
	/**
	 * @param invokeMethod The invokeMethod to set.
	 */
	public void setInvokeMethod(String invokeMethod)
	{
		this.invokeMethod = invokeMethod;
	}
}
