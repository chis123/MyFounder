package com.founder.e5.sys;

import com.founder.e5.context.E5Exception;

public interface SysConfigReader
{
	/**
	 * 得到所有的系统配置
	 */
	public SysConfig[] get() throws E5Exception;

	/**
	 * 获得系统配置
	 * @param sysConfigID    系统配置ID
	 * 
	 */
	public SysConfig get(int sysConfigID) throws E5Exception;

	/**
	 * 得到系统配置
	 * @param appID
	 * @param project
	 * @param item    item
	 * 
	 */
	public String get(int appID, String project, String item) throws E5Exception;

	/**
	 * 获得子系统的配置
	 * @param appID    子系统ID
	 * 
	 */
	public SysConfig[] getAppSysConfigs(int appID) throws E5Exception;
	
}
