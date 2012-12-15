package com.founder.e5.scheduler;

/**
 * @author wanghc �����ڷ������ϵ�״̬����
 */
public class JobState
{
	
	/**
	 * ������������
	 */
	private String serverName;

	/**
	 * �����״̬
	 * 
	 */
	private String jobState;

	/**
	 * �����״̬����
	 * 
	 */
	private String jobStateName;

	/**
	 * �Ƿ����
	 */
	private boolean available = false;
	
	
	public JobState(String serverName,String jobState,boolean available)
	{
		this.serverName = serverName;
		this.jobState   = jobState;
		this.available  = available;
	}
	/**
	 * @return Returns the jobState.
	 */
	public String getJobState()
	{
		return jobState;
	}

	/**
	 * @param jobState
	 *            The jobState to set.
	 */
	public void setJobState(String jobState)
	{
		this.jobState = jobState;
	}

	/**
	 * @return Returns the jobStateName.
	 */
	public String getJobStateName()
	{
		return jobStateName;
	}

	/**
	 * @param jobStateName
	 *            The jobStateName to set.
	 */
	public void setJobStateName(String jobStateName)
	{
		this.jobStateName = jobStateName;
	}

	/**
	 * @return Returns the serverName.
	 */
	public String getServerName()
	{
		return serverName;
	}

	/**
	 * @param serverName
	 *            The serverName to set.
	 */
	public void setServerName(String serverName)
	{
		this.serverName = serverName;
	}

	/**
	 * @roseuid 4496695E0000
	 */
	public JobState()
	{

	}

	public boolean isAvailable()
	{
		return available;
	}

	public void setAvailable(boolean available)
	{
		this.available = available;
	}
}
