package com.founder.e5.app;
import org.hibernate.Session;

import com.founder.e5.app.AppPermission;
import com.founder.e5.context.E5Exception;

/**
 * @created 04-七月-2005 16:12:56
 * @version 1.0
 * @updated 11-七月-2005 12:45:16
 */
public interface AppPermissionManager {

	/**
	 * 创建子系统权限页面信息
	 * @param appPermission    子系统权限页面信息
	 * 
	 */
	public void create(AppPermission appPermission) throws E5Exception;
	
	/**
	 * 创建子系统权限页面信息
	 * @param appPermission    子系统权限页面信息
	 * 
	 */
	public void create(AppPermission appPermission, Session session) throws E5Exception;

	/**
	 * 获得所有的子系统权限页面信息
	 */
	public AppPermission[] get() throws E5Exception;

	/**
	 * 获得指定子系统的所有权限页面信息
	 * @param appID    子系统ID
	 * 
	 */
	public AppPermission[] get(int appID) throws E5Exception;

	/**
	 * 根据资源类型得到子系统权限页面信息
	 * 
	 * @param nResourceType    资源类型
	 */
	public AppPermission[] get(String resourceType) throws E5Exception;

	/**
	 * 获得指定子系统，指定资源类型的权限页面信息
	 * @param appID    子系统ID
	 * @param nResourceType    资源类型
	 * 
	 */
	public AppPermission get(int appID, String resourceType) throws E5Exception;

	/**
	 * 更新权限页面信息
	 * @param appPermission    appPermission
	 * 
	 */
	public void update(AppPermission appPermission) throws E5Exception;

	/**
	 * 删除权限页面信息
	 * @param appID    子系统ID
	 * 
	 */
	public void delete(int appID) throws E5Exception;
	
	/**
	 * 删除权限页面信息
	 * @param appID    子系统ID
	 * @param session hibernate session 对象
	 */
	public void delete(int appID, Session session) throws E5Exception;

}