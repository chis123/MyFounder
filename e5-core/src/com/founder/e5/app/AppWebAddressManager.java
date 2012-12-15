package com.founder.e5.app;
import org.hibernate.Session;

import com.founder.e5.app.AppWebAddress;
import com.founder.e5.context.E5Exception;

/**
 * @created 04-����-2005 16:15:14
 * @version 1.0
 * @updated 11-����-2005 12:45:34
 */
public interface AppWebAddressManager {

	/**
	 * ������ϵͳ����ҳ�����
	 * @param appWebAddress    ��ϵͳ����ҳ�����
	 * 
	 */
	public void create(AppWebAddress appWebAddress) throws E5Exception;
	
	/**
	 * ������ϵͳ����ҳ�����
	 * @param appWebAddress    ��ϵͳ����ҳ�����
	 * 
	 */
	public void create(AppWebAddress appWebAddress, Session session) throws E5Exception;

	/**
	 * ������е���ϵͳ����ҳ����Ϣ
	 */
	public AppWebAddress[] get() throws E5Exception;

	/**
	 * ���ָ����ϵͳ�Ĺ���ҳ�����
	 * @param appID    ��ϵͳID
	 * 
	 */
	public AppWebAddress[] get(int appID) throws E5Exception;

	/**
	 * ���ָ����ϵͳ�ģ�ָ��ҳ�����ƵĹ���ҳ����Ϣ
	 * @param appID    ��ϵͳID
	 * @param webName    ����ҳ����
	 * 
	 */
	public AppWebAddress get(int appID, String webName) throws E5Exception;

	/**
	 * ������ϵͳ����ҳ����Ϣ
	 * @param appWebAddress    ��ϵͳ����ҳ�����
	 * 
	 */
	public void update(AppWebAddress appWebAddress) throws E5Exception;

	/**
	 * ɾ����ϵͳ����ҳ�����
	 * @param appID    ��ϵͳID
	 * 
	 */
	public void delete(int appID) throws E5Exception;
	
	/**
	 * ɾ����ϵͳ����ҳ�����
	 * @param appID    ��ϵͳID
	 * @param session hibernate session ����
	 */
	public void delete(int appID, Session session) throws E5Exception;

}