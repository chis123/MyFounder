package com.founder.e5.app;

import org.hibernate.Session;

import com.founder.e5.context.E5Exception;

/**
 * @created 21-六月-2005 14:41:34
 * @version 1.0
 */
public interface AppManager {

	
	/**
	 * 创建子系统
	 * 
	 * @param app    app
	 */
	public void create(App app) throws E5Exception;
	
	/**
	 * 创建子系统
	 * 
	 * @param app    app
	 * @param session  
	 */
	public void create(App app, Session session) throws E5Exception;

	/**
	 * 判断指定的子系统是否已存在
	 * 
	 * @param app    app
	 */
	public boolean exist(App app) throws E5Exception;
	
	/**
	 * 判断指定的子系统是否已存在
	 * 
	 * @param app    app
	 * @param session
	 */
	public boolean exist(App app, Session session) throws E5Exception;

	/**
	 * 获得所有的子系统
	 */
	public App[] get() throws E5Exception;

	/**
	 * 获得指定的子系统ID对应的子系统
	 * 
	 * @param appID    appID
	 */
	public App get(int appID) throws E5Exception;

	/**
	 * 获得指定的子系统
	 * 
	 * @param appVersion    子系统版本号
	 * @param appName    子系统名
	 */
	public App get(String appVersion, String appName) throws E5Exception;

	/**
	 * 更新子系统
	 * 
	 * @param app    子系统对象
	 */
	public void update(App app) throws E5Exception;
	
	/**
	 * 更新子系统
	 * 
	 * @param app    子系统对象
	 * @param session  hibernate session 对象
	 */
	public void update(App app, Session session) throws E5Exception;

	/**
	 * 
	 * @param appID    子系统ID
	 */
	public void delete(int appID) throws E5Exception;
	
	/**
	 * 
	 * @param appID    子系统ID
	 * @param session  hibernate session 对象
	 */
	public void delete(int appID, Session session) throws E5Exception;



}