package com.founder.e5.scheduler.db;

/**
 * Job属性对象
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
	 * 任务名称
	 */
	private String name;

	/**
	 * 任务的实现类
	 */
	private String jobClass;

	/**
	 * 配置页面URL
	 */
	private String configUrl;

	/**
	 * 日志产看的URL
	 */
	private String logUrl;

	/**
	 * 状态：Y/启用（手工）,A/启用（自动）,N/禁用
	 */
	private String active;

	/**
	 * 描述
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
	 * 将名字规则改为:jobID_job(原因是如果job启动后,通过页面修改job名称时会导致无法停止这个job了)
	 * 返回调度程序使用名字，规则为jobID_jobName
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
