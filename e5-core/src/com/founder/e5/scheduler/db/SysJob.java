package com.founder.e5.scheduler.db;

/**
 * Job���Զ���
 * @author wanghc
 */
public class SysJob
{
	public static final String STATE_DISABLE = "N";
	public static final String STATE_ENABLE_HUMAN = "Y";
	public static final String STATE_ENABLE_AUTO = "A";
	
	/**
	 * jobID
	 */
	private int jobID;

	/**
	 * ��������
	 */
	private String name;

	/**
	 * �����ʵ����
	 */
	private String jobClass;

	/**
	 * ����ҳ��URL
	 */
	private String configUrl;

	/**
	 * ��־������URL
	 */
	private String logUrl;

	/**
	 * ״̬��Y/���ã��ֹ���,A/���ã��Զ���,N/����
	 */
	private String active;

	/**
	 * ����
	 */
	private String description;
	
	/**
	 * @roseuid 44965FCC0271
	 */
	public SysJob()
	{

	}

	/**
	 * @return Returns the active.
	 */
	public String getActive()
	{
		return active;
	}

	/**
	 * @param active The active to set.
	 */
	public void setActive(String active)
	{
		this.active = active;
	}

	/**
	 * @return Returns the configUrl.
	 */
	public String getConfigUrl()
	{
		return configUrl;
	}

	/**
	 * @param configUrl The configUrl to set.
	 */
	public void setConfigUrl(String configUrl)
	{
		this.configUrl = configUrl;
	}

	/**
	 * @return Returns the jobClass.
	 */
	public String getJobClass()
	{
		return jobClass;
	}

	/**
	 * @param jobClass The jobClass to set.
	 */
	public void setJobClass(String jobClass)
	{
		this.jobClass = jobClass;
	}

	/**
	 * @return Returns the jobID.
	 */
	public int getJobID()
	{
		return jobID;
	}

	/**
	 * @param jobID The jobID to set.
	 */
	public void setJobID(int jobID)
	{
		this.jobID = jobID;
	}

	/**
	 * @return Returns the logUrl.
	 */
	public String getLogUrl()
	{
		return logUrl;
	}

	/**
	 * @param logUrl The logUrl to set.
	 */
	public void setLogUrl(String logUrl)
	{
		this.logUrl = logUrl;
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
	 * �����ֹ����Ϊ:jobID_job(ԭ�������job������,ͨ��ҳ���޸�job����ʱ�ᵼ���޷�ֹͣ���job��)
	 * ���ص��ȳ���ʹ�����֣�����ΪjobID_jobName
	 * @return
	 */
	public String getJobDetailName()
	{
		return jobID+"_job";
	}



	/**
	 * @return Returns the description.
	 */
	public String getDescription()
	{
		return description;
	}

	/**
	 * @param description The description to set.
	 */
	public void setDescription(String desription)
	{
		this.description = desription;
	}
	
	public boolean isAutoStart(){
		return STATE_ENABLE_AUTO.equals(getActive());
	}
	
	public boolean equals(Object obj)
	{
		if(obj == null) 
			return false;
		if(!(obj instanceof SysJob))
			return false;
		
		return ((SysJob)obj).getJobID() == jobID;
	}	
	
	public int hashCode()
	{
		return jobID;
	}
}
