package com.founder.e5.sys;
import org.hibernate.Session;

import com.founder.e5.context.E5Exception;
import com.founder.e5.sys.SysConfig;

/**
 * @created 04-七月-2005 16:04:17
 * @version 1.0
 * @updated 11-七月-2005 13:03:41
 */
public interface SysConfigManager extends SysConfigReader{

	/**
	 * 创建系统配置
	 * @param sysConfig    系统配置
	 * 
	 */
	public void create(SysConfig sysConfig) throws E5Exception;
	
	/**
	 * 创建系统配置
	 * @param sysConfig    系统配置
	 * 
	 */
	public void create(SysConfig sysConfig, Session session) throws E5Exception;

	/**
	 * 更新系统配置
	 * @param sysConfig    系统配置对象
	 * 
	 */
	public void update(SysConfig sysConfig) throws E5Exception;

	/**
	 * 删除系统配置
	 * @param sysConfigID    系统配置ID
	 * 
	 */
	public void delete(int sysConfigID) throws E5Exception;


	/**
	 * 删除子系统的系统配置
	 * @param appID    子系统ID
	 * 
	 */
	public void deleteAppSysConfig(int appID) throws E5Exception;

	/**
	 * 删除子系统的系统配置
	 * @param appID    子系统ID
	 * @param session hibernate session 对象
	 */
	public void deleteAppSysConfig(int appID, Session session) throws E5Exception;

}