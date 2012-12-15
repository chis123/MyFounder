package com.founder.e5.permission;
import java.util.ArrayList;
import java.util.List;

import com.founder.e5.context.Cache;
import com.founder.e5.context.E5Exception;

/**
 * @deprecated 2006-5-17
 * 已经被合并在PermissionCache类中
 * 
 * @created 14-7-2005 16:24:43
 * @author Gong Lijie
 * @version 1.0
 */
public class FVPermissionCache implements Cache {

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
	public FVPermissionCache(){
		
	}
	public void refresh() throws E5Exception{
		FVPermissionManager manager = PermissionHelper.getFVPermissionManager();
		fvPermissions = manager.getAll();
	}

	public void reset(){
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