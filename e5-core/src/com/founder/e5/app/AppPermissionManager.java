package com.founder.e5.app;
import org.hibernate.Session;

import com.founder.e5.app.AppPermission;
import com.founder.e5.context.E5Exception;

/**
 * @created 04-����-2005 16:12:56
 * @version 1.0
 * @updated 11-����-2005 12:45:16
 */
public interface AppPermissionManager {

	/**
	 * ������ϵͳȨ��ҳ����Ϣ
	 * @param appPermission    ��ϵͳȨ��ҳ����Ϣ
	 * 
	 */
	public void create(AppPermission appPermission) throws E5Exception;
	
	/**
	 * ������ϵͳȨ��ҳ����Ϣ
	 * @param appPermission    ��ϵͳȨ��ҳ����Ϣ
	 * 
	 */
	public void create(AppPermission appPermission, Session session) throws E5Exception;

	/**
	 * ������е���ϵͳȨ��ҳ����Ϣ
	 */
	public AppPermission[] get() throws E5Exception;

	/**
	 * ���ָ����ϵͳ������Ȩ��ҳ����Ϣ
	 * @param appID    ��ϵͳID
	 * 
	 */
	public AppPermission[] get(int appID) throws E5Exception;

	/**
	 * ������Դ���͵õ���ϵͳȨ��ҳ����Ϣ
	 * 
	 * @param nResourceType    ��Դ����
	 */
	public AppPermission[] get(String resourceType) throws E5Exception;

	/**
	 * ���ָ����ϵͳ��ָ����Դ���͵�Ȩ��ҳ����Ϣ
	 * @param appID    ��ϵͳID
	 * @param nResourceType    ��Դ����
	 * 
	 */
	public AppPermission get(int appID, String resourceType) throws E5Exception;

	/**
	 * ����Ȩ��ҳ����Ϣ
	 * @param appPermission    appPermission
	 * 
	 */
	public void update(AppPermission appPermission) throws E5Exception;

	/**
	 * ɾ��Ȩ��ҳ����Ϣ
	 * @param appID    ��ϵͳID
	 * 
	 */
	public void delete(int appID) throws E5Exception;
	
	/**
	 * ɾ��Ȩ��ҳ����Ϣ
	 * @param appID    ��ϵͳID
	 * @param session hibernate session ����
	 */
	public void delete(int appID, Session session) throws E5Exception;

}