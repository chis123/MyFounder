package com.founder.e5.scheduler;
/**
 * ������
 * 
 * @author wanghc
 *
 */
public class Constants
{

	/*������ȵ���־��������*/
	public static final String LOG_NAME = "e5.sys";
	
	public static final String SERVER_ALL = "ALL";
	
	public static final String DEFAULT_SERVER_NAME = "localhost";
	
	/*-==============����״̬=============-*/
	public static final String JOB_STATE_STARTED = "STARTED";
	public static final String JOB_STATE_NOSTART = "NOSTART";
	public static final String JOB_STATE_EXECUTING = "EXECUTING";	
	
	/*-==============������ȵ�����========-*/
	public static final String COMMAND_START_ALL = "STARTALL";
	public static final String COMMAND_STOP_ALL  = "STOPALL";
	public static final String COMMAND_START     = "START";
	public static final String COMMAND_STOP      = "STOP";
	public static final String COMMAND_EXECUTINGJOBS = "EXECUTEJOBS";
	public static final String COMMAND_JOBS      = "JOBS";
	public static final String COMMAND_SHUTDOWN  = "SHUTDOWN";
	/**
	 * ��������������
	 */
	private static String serverName = DEFAULT_SERVER_NAME;	
	
	
	
	public static String getServerName()
	{
		return serverName;
	}
	
	/**
	 * ���÷��������ƣ�ϵͳ��ʼ��ʱ����e5-config.xml��ָ���ı�������������
	 * @param ssname
	 */
	public static void setServerName(String ssname)
	{
		//ֻ��������һ��
		if(serverName.equals(DEFAULT_SERVER_NAME))
			serverName = ssname;
	}
}
