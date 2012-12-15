package com.founder.e5.sys;

import com.founder.e5.context.E5Exception;

public interface SysConfigReader
{
	/**
	 * �õ����е�ϵͳ����
	 */
	public SysConfig[] get() throws E5Exception;

	/**
	 * ���ϵͳ����
	 * @param sysConfigID    ϵͳ����ID
	 * 
	 */
	public SysConfig get(int sysConfigID) throws E5Exception;

	/**
	 * �õ�ϵͳ����
	 * @param appID
	 * @param project
	 * @param item    item
	 * 
	 */
	public String get(int appID, String project, String item) throws E5Exception;

	/**
	 * �����ϵͳ������
	 * @param appID    ��ϵͳID
	 * 
	 */
	public SysConfig[] getAppSysConfigs(int appID) throws E5Exception;
	
}
