package com.founder.e5.app;

import org.hibernate.Session;

import com.founder.e5.context.E5Exception;

/**
 * @created 21-����-2005 14:41:34
 * @version 1.0
 */
public interface AppManager {

	
	/**
	 * ������ϵͳ
	 * 
	 * @param app    app
	 */
	public void create(App app) throws E5Exception;
	
	/**
	 * ������ϵͳ
	 * 
	 * @param app    app
	 * @param session  
	 */
	public void create(App app, Session session) throws E5Exception;

	/**
	 * �ж�ָ������ϵͳ�Ƿ��Ѵ���
	 * 
	 * @param app    app
	 */
	public boolean exist(App app) throws E5Exception;
	
	/**
	 * �ж�ָ������ϵͳ�Ƿ��Ѵ���
	 * 
	 * @param app    app
	 * @param session
	 */
	public boolean exist(App app, Session session) throws E5Exception;

	/**
	 * ������е���ϵͳ
	 */
	public App[] get() throws E5Exception;

	/**
	 * ���ָ������ϵͳID��Ӧ����ϵͳ
	 * 
	 * @param appID    appID
	 */
	public App get(int appID) throws E5Exception;

	/**
	 * ���ָ������ϵͳ
	 * 
	 * @param appVersion    ��ϵͳ�汾��
	 * @param appName    ��ϵͳ��
	 */
	public App get(String appVersion, String appName) throws E5Exception;

	/**
	 * ������ϵͳ
	 * 
	 * @param app    ��ϵͳ����
	 */
	public void update(App app) throws E5Exception;
	
	/**
	 * ������ϵͳ
	 * 
	 * @param app    ��ϵͳ����
	 * @param session  hibernate session ����
	 */
	public void update(App app, Session session) throws E5Exception;

	/**
	 * 
	 * @param appID    ��ϵͳID
	 */
	public void delete(int appID) throws E5Exception;
	
	/**
	 * 
	 * @param appID    ��ϵͳID
	 * @param session  hibernate session ����
	 */
	public void delete(int appID, Session session) throws E5Exception;



}