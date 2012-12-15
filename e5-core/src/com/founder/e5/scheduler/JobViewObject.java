package com.founder.e5.scheduler;

/**
 * @author wanghc
 */
public class JobViewObject
{
	private int jobID;

	private String jobName;

	private String logUrl;
	
	private com.founder.e5.scheduler.JobState[] jobStates;


	/**
	 * @roseuid 449669B101C5
	 */
	public JobViewObject()
	{

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
	 * @return Returns the jobName.
	 */
	public String getJobName()
	{
		return jobName;
	}

	/**
	 * @param jobName
	 *            The jobName to set.
	 */
	public void setJobName(String jobName)
	{
		this.jobName = jobName;
	}

	/**
	 * @return Returns the jobStates.
	 */
	public com.founder.e5.scheduler.JobState[] getJobStates()
	{
		return jobStates;
	}

	/**
	 * @param jobStates
	 *            The jobStates to set.
	 */
	public void setJobStates(com.founder.e5.scheduler.JobState[] jobStates)
	{
		this.jobStates = jobStates;
	}

	public String getLogUrl()
	{
		return logUrl;
	}

	public void setLogUrl(String logUrl)
	{
		this.logUrl = logUrl;
	}
}
