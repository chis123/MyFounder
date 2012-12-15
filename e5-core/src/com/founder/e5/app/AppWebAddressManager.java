package com.founder.e5.app;
import org.hibernate.Session;

import com.founder.e5.app.AppWebAddress;
import com.founder.e5.context.E5Exception;

/**
 * @created 04-七月-2005 16:15:14
 * @version 1.0
 * @updated 11-七月-2005 12:45:34
 */
public interface AppWebAddressManager {

	/**
	 * 创建子系统管理页面对象
	 * @param appWebAddress    子系统管理页面对象
	 * 
	 */
	public void create(AppWebAddress appWebAddress) throws E5Exception;
	
	/**
	 * 创建子系统管理页面对象
	 * @param appWebAddress    子系统管理页面对象
	 * 
	 */
	public void create(AppWebAddress appWebAddress, Session session) throws E5Exception;

	/**
	 * 获得所有的子系统管理页面信息
	 */
	public AppWebAddress[] get() throws E5Exception;

	/**
	 * 获得指定子系统的管理页面对象
	 * @param appID    子系统ID
	 * 
	 */
	public AppWebAddress[] get(int appID) throws E5Exception;

	/**
	 * 获得指定子系统的，指定页面名称的管理页面信息
	 * @param appID    子系统ID
	 * @param webName    管理页面名
	 * 
	 */
	public AppWebAddress get(int appID, String webName) throws E5Exception;

	/**
	 * 更新子系统管理页面信息
	 * @param appWebAddress    子系统管理页面对象
	 * 
	 */
	public void update(AppWebAddress appWebAddress) throws E5Exception;

	/**
	 * 删除子系统管理页面对象
	 * @param appID    子系统ID
	 * 
	 */
	public void delete(int appID) throws E5Exception;
	
	/**
	 * 删除子系统管理页面对象
	 * @param appID    子系统ID
	 * @param session hibernate session 对象
	 */
	public void delete(int appID, Session session) throws E5Exception;

}