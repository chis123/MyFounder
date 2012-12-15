package com.founder.e5.config;

/**
 * Created on 2005-7-19
 * @author Gong Lijie
 */
public class InfoDBSession
{
	private String name;
	private String implementation;
	
	public String toString(){
		return (new StringBuffer()
				.append("[name:").append(name)
				.append(",implementation:").append(implementation)
				.append("]")
				).toString();
	}
	/**
	 * @return Returns the implementation.
	 */
	public String getImplementation()
	{
		return implementation;
	}
	/**
	 * @param implementation The implementation to set.
	 */
	public void setImplementation(String implementation)
	{
		this.implementation = implementation;
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
}
