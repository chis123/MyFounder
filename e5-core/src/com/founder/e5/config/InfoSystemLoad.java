package com.founder.e5.config;

/**
 * Created on 2005-7-19
 * @author Gong Lijie
 */
public class InfoSystemLoad
{
	private String sysInit;
	private String domInit;
	private String flowInit;
	private String sqlScript;

	public String toString(){
		return (new StringBuffer()
				.append("[sysInit:").append(sysInit)
				.append(",domInit:").append(domInit)
				.append(",flowInit:").append(flowInit)
				.append(",sqlScript:").append(sqlScript)
				.append("]")
				).toString();
	}
	/**
	 * @return Returns the domInit.
	 */
	public String getDomInit()
	{
		return domInit;
	}
	/**
	 * @param domInit The domInit to set.
	 */
	public void setDomInit(String domInit)
	{
		this.domInit = domInit;
	}
	/**
	 * @return Returns the flowInit.
	 */
	public String getFlowInit()
	{
		return flowInit;
	}
	/**
	 * @param flowInit The flowInit to set.
	 */
	public void setFlowInit(String flowInit)
	{
		this.flowInit = flowInit;
	}
	/**
	 * @return Returns the sqlScript.
	 */
	public String getSqlScript()
	{
		return sqlScript;
	}
	/**
	 * @param sqlScript The sqlScript to set.
	 */
	public void setSqlScript(String sqlScript)
	{
		this.sqlScript = sqlScript;
	}
	/**
	 * @return Returns the sysInit.
	 */
	public String getSysInit()
	{
		return sysInit;
	}
	/**
	 * @param sysInit The sysInit to set.
	 */
	public void setSysInit(String sysInit)
	{
		this.sysInit = sysInit;
	}
}
