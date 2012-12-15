package com.founder.e5.scheduler.db;

/**
 * ϵͳ����������
 * @author wanghc
 */
public class SysTrigger
{
	/**
	 * ������ID
	 */
	private int triggerID;

	/**
	 * ����������
	 */
	private String name;

	/**
	 * ��������ʽ,ʹ��quartz��CronTrigger
	 */
	private String cronExpression;

	/**
	 * ������,ALL��ʾȫ��������Ϊ����ķ�����
	 */
	private String server;

	/**
	 * ������jobID
	 */
	private int jobID;

	/**
	 * ״̬:Y/����,N/����
	 */
	private String active;
	
	/**
	 * HASHCODE
	 */
	private int hashCode = Integer.MIN_VALUE;
	
	/**
	 * @return Returns the active.
	 */
	public String getActive()
	{
		return active;
	}

	/**
	 * @param active
	 *            The active to set.
	 */
	public void setActive(String active)
	{
		this.active = active;
	}

	/**
	 * @return Returns the cronExpression.
	 */
	public String getCronExpression()
	{
		return cronExpression;
	}

	/**
	 * @param cronExpression
	 *            The cronExpression to set.
	 */
	public void setCronExpression(String cronExpression)
	{
		this.cronExpression = cronExpression;
	}

	/**
	 * @return Returns the jobID.
	 */
	public int getJobID()
	{
		return jobID;
	}

	/**
	 * @param jobID
	 *            The jobID to set.
	 */
	public void setJobID(int jobID)
	{
		this.jobID = jobID;
	}

	/**
	 * @return Returns the name.
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param name
	 *            The name to set.
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * @return Returns the server.
	 */
	public String getServer()
	{
		return server;
	}

	/**
	 * @param server
	 *            The server to set.
	 */
	public void setServer(String server)
	{
		this.server = server;
	}

	/**
	 * @return Returns the triggerID.
	 */
	public int getTriggerID()
	{
		return triggerID;
	}

	/**
	 * @param triggerID
	 *            The triggerID to set.
	 */
	public void setTriggerID(int triggerID)
	{
		this.triggerID = triggerID;
	}

	/**
	 * @roseuid 44965FCC03A9
	 */
	public SysTrigger()
	{

	}
	
	public boolean equals(Object obj)
	{
		if(obj == null) return false;
		if(!(obj instanceof SysTrigger)) return false;
		
		if(((SysTrigger)obj).getTriggerID() == triggerID) return true;
		
		return false;
	}
	
	public int hashCode()
	{
		if(hashCode == Integer.MIN_VALUE)
		{
			StringBuffer sb = new StringBuffer();
			sb.append(jobID).append(":").append(triggerID);
			hashCode = sb.toString().hashCode();
		}
		return hashCode;
	}
}
