package com.founder.e5.sys;
import org.hibernate.Session;

import com.founder.e5.context.E5Exception;
import com.founder.e5.sys.SysConfig;

/**
 * @created 04-����-2005 16:04:17
 * @version 1.0
 * @updated 11-����-2005 13:03:41
 */
public interface SysConfigManager extends SysConfigReader{

	/**
	 * ����ϵͳ����
	 * @param sysConfig    ϵͳ����
	 * 
	 */
	public void create(SysConfig sysConfig) throws E5Exception;
	
	/**
	 * ����ϵͳ����
	 * @param sysConfig    ϵͳ����
	 * 
	 */
	public void create(SysConfig sysConfig, Session session) throws E5Exception;

	/**
	 * ����ϵͳ����
	 * @param sysConfig    ϵͳ���ö���
	 * 
	 */
	public void update(SysConfig sysConfig) throws E5Exception;

	/**
	 * ɾ��ϵͳ����
	 * @param sysConfigID    ϵͳ����ID
	 * 
	 */
	public void delete(int sysConfigID) throws E5Exception;


	/**
	 * ɾ����ϵͳ��ϵͳ����
	 * @param appID    ��ϵͳID
	 * 
	 */
	public void deleteAppSysConfig(int appID) throws E5Exception;

	/**
	 * ɾ����ϵͳ��ϵͳ����
	 * @param appID    ��ϵͳID
	 * @param session hibernate session ����
	 */
	public void deleteAppSysConfig(int appID, Session session) throws E5Exception;

}