package com.founder.e5.permission;
import java.util.ArrayList;
import java.util.List;

import com.founder.e5.context.Cache;
import com.founder.e5.context.E5Exception;

/**
 * @created 14-7-2005 16:24:48
 * @author Gong Lijie
 * @version 1.0
 */
public class PermissionCache implements Cache {

	private Permission[] permissions;
	private FVPermission[] fvPermissions;
	
	/**
	 * 缓存类主要用在Reader实现类中<br>
	 * 在引用时，注意不要使用构造方法创建缓存类的实例<br>
	 * 请使用如下形式：<br>
	 * <code>
	 * XXXCache cache = (XXXCache)(CacheReader.find(XXXCache.class));
	 * if (cache != null)
	 * 		return cache.get(...);
	 * </code>
	 * 其中XXXCache代表具体的缓存类
	 */
	public PermissionCache(){
		
	}
	public void refresh() throws E5Exception {
		permissions = PermissionHelper.getManager().getAll();
		FVPermissionManager manager = PermissionHelper.getFVPermissionManager();
		fvPermissions = manager.getAll();
	}

	public void reset(){

	}

	/**
	 * 取角色权限
	 * @param roleID 角色ID
	 * @param resourceType 资源类型
	 * @param resource 资源名
	 * @return Permission 当从缓存中找不到对应的记录，则返回null
	 */
	Permission getPermission(int roleID, String resourceType, String resource){
		if (permissions == null) return null;
		for (int i = 0; i < permissions.length; i++){
			if ((permissions[i].getRoleID() == roleID) 
					&& (permissions[i].getResourceType().equals(resourceType))
					&& (permissions[i].getResource().equals(resource)))
				return permissions[i];
		}
		return null;
	}

	/**
	 * 取角色的所有权限
	 * @param roleID
	 */
	List getPermissions(int roleID){
		if (permissions == null) return null;

		List list = new ArrayList(10);
		for (int i = 0; i < permissions.length; i++) {
			if (permissions[i].getRoleID() == roleID) 
				list.add(permissions[i]);
		}
		return list;
	}

	/**
	 * 取角色对某资源类型的所有权限
	 * @param roleID
	 * @param resourceType
	 */
	List getPermissions(int roleID, String resourceType){
		if (permissions == null) return null;

		List list = new ArrayList(10);
		for (int i = 0; i < permissions.length; i++){
			if ((permissions[i].getRoleID() == roleID) 
				&& (permissions[i].getResourceType().equals(resourceType)))
				list.add(permissions[i]);
		}
		return list;
	}

	/**
	 * 取某资源的所有权限
	 * 参数：资源类型、资源名
	 * 
	 * @param resourceType
	 * @param resource
	 */
	List getPermissions(String resourceType, String resource){
		if (permissions == null) return null;

		List list = new ArrayList(10);
		for (int i = 0; i < permissions.length; i++) {
			if (permissions[i].getResourceType().equals(resourceType)
					&& permissions[i].getResource().equals(resource)) 
				list.add(permissions[i]);
		}
		return list;
	}

	/**
	 * 取某资源类型的所有权限
	 * 参数：资源类型
	 * 
	 * @param resourceType
	 */
	List getPermissions(String resourceType){
		if (permissions == null) return null;

		List list = new ArrayList(10);
		for (int i = 0; i < permissions.length; i++) {
			if (permissions[i].getResourceType().equals(resourceType)) 
				list.add(permissions[i]);
		}
		return list;
	}
	/**
	 * 取角色的文件夹权限
	 * 在操作界面上常用。
	 * 
	 * @param roleID 角色ID
	 * @param fvID 文件夹/视图ID
	 */
	int getFVPermission(int roleID, int fvID){
		if (fvPermissions == null) return 0;
		
		for (int i = 0; i < fvPermissions.length; i++)
			if ((fvPermissions[i].getRoleID() == roleID)
					&& (fvPermissions[i].getFvID() == fvID))
				return fvPermissions[i].getPermission();
		return 0;
	}

	/**
	 * 取角色的所有文件夹权限
	 * 日常工作界面的资源树使用。
	 * 
	 * @param roleID 角色ID
	 */
	List getFVPermissionsByRole(int roleID){
		List list = new ArrayList();
		if (fvPermissions == null) return list;
		
		for (int i = 0; i < fvPermissions.length; i++)
			if (fvPermissions[i].getRoleID() == roleID)
				list.add(fvPermissions[i]);
		return list;
	}

	/**
	 * 取某文件夹/视图的所有权限
	 * @param fvID 文件夹视图ID
	 */
	List getFVPermissionsByFolder(int fvID){
		List list = new ArrayList();
		if (fvPermissions == null) return list;
		
		for (int i = 0; i < fvPermissions.length; i++)
			if (fvPermissions[i].getFvID() == fvID)
				list.add(fvPermissions[i]);
		return list;
	}
}