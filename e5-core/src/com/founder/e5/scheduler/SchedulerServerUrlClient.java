package com.founder.e5.scheduler;

import com.founder.e5.commons.HttpClient;
import com.founder.e5.commons.Log;
import com.founder.e5.context.Context;

/**
 * 集群控制URL调用的实现
 * 
 * @author wanghc
 * 
 */
public class SchedulerServerUrlClient implements SchedulerServer
{
	private Log log = Context.getLog(Constants.LOG_NAME);

	/**
	 * url地址
	 */
	private String serverUrl;

	/**
	 * 任务调度的file
	 */
	private String file = "/e5scheduler/SchedulerRemoteCommand.do?command=";

	/**
	 * @param url
	 * @roseuid 44966F4702BF
	 */
	public SchedulerServerUrlClient(String url)
	{
		serverUrl = url + file;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.founder.e5.scheduler.SchedulerServer#getCurrentlyExectingJobs()
	 */
	public String[] getCurrentlyExectingJobs()
	{
		String result[] = null;
		try
		{
			String content = HttpClient.doGet(serverUrl
					+ Constants.COMMAND_EXECUTINGJOBS);
			if (content != null && !content.equals(""))
			{
				result = content.split("\r\n");
			}
		} catch (Exception e)
		{
			log.error("access " + serverUrl + " failed", e);
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.founder.e5.scheduler.SchedulerServer#getJobs()
	 */
	public String[] getJobs()
	{
		String result[] = null;
		try
		{
			String content = HttpClient.doGet(serverUrl
					+ Constants.COMMAND_JOBS);
			if (content != null && !content.equals(""))
			{
				result = content.split("\r\n");
			}
		} catch (Exception e)
		{
			log.error("access " + serverUrl + " failed", e);
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.founder.e5.scheduler.SchedulerServer#scheduler(int)
	 */
	public boolean scheduler(int jobID)
	{

		try
		{
			HttpClient.doGet(serverUrl + Constants.COMMAND_START + "&jobID="
					+ jobID);
		} catch (Exception e)
		{

			log.error("access " + serverUrl + " failed", e);
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.founder.e5.scheduler.SchedulerServer#schedulerAll()
	 */
	public int schedulerAll()
	{
		try
		{
			HttpClient.doGet(serverUrl + Constants.COMMAND_START_ALL);
		} catch (Exception e)
		{

			log.error("access " + serverUrl + " failed", e);
		}
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.founder.e5.scheduler.SchedulerServer#shutdown()
	 */
	public void shutdown()
	{
		try
		{
			HttpClient.doGet(serverUrl + Constants.COMMAND_SHUTDOWN);
		} catch (Exception e)
		{
			log.error("access " + serverUrl + " failed", e);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.founder.e5.scheduler.SchedulerServer#stop(int)
	 */
	public boolean stop(int jobID)
	{
		try
		{
			HttpClient.doGet(serverUrl + Constants.COMMAND_STOP + "&jobID="
					+ jobID);
		} catch (Exception e)
		{

			log.error("access " + serverUrl + " failed", e);
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.founder.e5.scheduler.SchedulerServer#stopAll()
	 */
	public int stopAll()
	{
		try
		{
			HttpClient.doGet(serverUrl + Constants.COMMAND_STOP_ALL);
		} catch (Exception e)
		{

			log.error("access " + serverUrl + " failed", e);
		}
		return 0;
	}

}
