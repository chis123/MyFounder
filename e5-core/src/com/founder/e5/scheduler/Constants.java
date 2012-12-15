package com.founder.e5.scheduler;
/**
 * 常量类
 * 
 * @author wanghc
 *
 */
public class Constants
{

	/*任务调度的日志分类名称*/
	public static final String LOG_NAME = "e5.sys";
	
	public static final String SERVER_ALL = "ALL";
	
	public static final String DEFAULT_SERVER_NAME = "localhost";
	
	/*-==============任务状态=============-*/
	public static final String JOB_STATE_STARTED = "STARTED";
	public static final String JOB_STATE_NOSTART = "NOSTART";
	public static final String JOB_STATE_EXECUTING = "EXECUTING";	
	
	/*-==============任务调度的命令========-*/
	public static final String COMMAND_START_ALL = "STARTALL";
	public static final String COMMAND_STOP_ALL  = "STOPALL";
	public static final String COMMAND_START     = "START";
	public static final String COMMAND_STOP      = "STOP";
	public static final String COMMAND_EXECUTINGJOBS = "EXECUTEJOBS";
	public static final String COMMAND_JOBS      = "JOBS";
	public static final String COMMAND_SHUTDOWN  = "SHUTDOWN";
	/**
	 * 本机服务器名称
	 */
	private static String serverName = DEFAULT_SERVER_NAME;	
	
	
	
	public static String getServerName()
	{
		return serverName;
	}
	
	/**
	 * 设置服务器名称，系统初始化时根据e5-config.xml中指定的本级名称来设置
	 * @param ssname
	 */
	public static void setServerName(String ssname)
	{
		//只允许设置一次
		if(serverName.equals(DEFAULT_SERVER_NAME))
			serverName = ssname;
	}
}
