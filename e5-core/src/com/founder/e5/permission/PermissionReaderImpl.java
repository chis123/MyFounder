package com.founder.e5.permission;

import java.util.ArrayList;
import java.util.List;

import com.founder.e5.context.E5Exception;

/**
 * @created 14-7-2005 16:25:29
 * @author Gong Lijie
 * @version 1.0
 */
class PermissionReaderImpl implements PermissionReader {

	PermissionReaderImpl()
	{
	}
	/**
	 * 取角色权限
	 * 参数：角色ID、资源类型、资源名
	 * 
	 * @param roleID
	 * @param resourceType
	 * @param resource
	 */
	public Permission get(int roleID, String resourceType, String resource) throws E5Exception {
		PermissionCache cache = PermissionHelper.getCache();
		if (cache != null)
			return cache.getPermission(roleID, resourceType, resource);
		return PermissionHelper.getManager().get(roleID, resourceType, resource);
	}

	/**
	 * 取角色的所有权限
	 * 参数：角色ID
	 * 
	 * @param roleID
	 */
	public Permission[] getPermissions(int roleID) throws E5Exception {
		PermissionCache cache = PermissionHelper.getCache();
		if (cache != null){
			List list = cache.getPermissions(roleID);
			if ((list == null) || (list.size() == 0))
				return null;
			else
				return (Permission[])list.toArray(new Permission[0]);
		}
		return PermissionHelper.getManager().getPermissions(roleID);
	}

	/**
	 * 取角色的某资源类型的权限
	 * 参数：角色ID，资源类型
	 * 
	 * @param roleID
	 * @param resourceType
	 */
	public Permission[] getPermissions(int roleID, String resourceType) throws E5Exception{
		PermissionCache cache = PermissionHelper.getCache();
		if (cache != null){
			List list = cache.getPermissions(roleID, resourceType);
			if ((list == null) || (list.size() == 0))
				return null;
			else
				return (Permission[])list.toArray(new Permission[0]);
		}
		return PermissionHelper.getManager().getPermissions(roleID, resourceType);
	}

	/**
	 * 取某资源类型的所有权限
	 * @param resourceType 资源类型
	 */
	public Permission[] getPermissions(String resourceType) throws E5Exception{
		PermissionCache cache = PermissionHelper.getCache();
		if (cache != null){
			List list = cache.getPermissions(resourceType);
			if ((list == null) || (list.size() == 0))
				return null;
			else
				return (Permission[])list.toArray(new Permission[0]);
		}
		return PermissionHelper.getManager().getPermissions(resourceType);
	}

	/**
	 * 取某资源的所有权限
	 * 参数：资源类型，资源名
	 * 
	 * @param resourceType
	 * @param resource
	 */
	public Permission[] getPermissions(String resourceType, String resource) 
		throws E5Exception
	{
		PermissionCache cache = PermissionHelper.getCache();
		if (cache != null){
			List list = cache.getPermissions(resourceType, resource);
			if ((list == null) || (list.size() == 0))
				return null;
			else
				return (Permission[])list.toArray(new Permission[0]);
		}
		return PermissionHelper.getManager().getPermissions(resourceType, resource);
	}

	/**
	 * 取某角色有某权限的所有资源
	 * @param roleID 角色ID
	 * @param resourceType 资源类型
	 * @param maskArray 权限标记
	 */
	public String[] getResourcesOfRole(int roleID, String resourceType, int[] maskArray)
		throws E5Exception
	{
		Permission[] pArr = getPermissions(roleID, resourceType);
		
		if (pArr == null) return null;
		if (maskArray == null) {
			String[] resArr = new String[pArr.length];
			for (int i = 0; i < pArr.length; i++) {
				resArr[i] = pArr[i].getResource();
			}
			return resArr;
		}

		List list = new ArrayList(pArr.length);
		for (int i = 0; i < pArr.length; i++) {
			if (PermissionHelper.hasPermission(pArr[i], maskArray))
				list.add(pArr[i].getResource());
		}
		if ((list == null) || (list.size() == 0))
			return null;
		else
			return (String[])(list.toArray(new String[0]));
	}

	/**
	 * 取对某资源有某种权限的所有角色
	 * 参数：资源类型、资源名、权限标记
	 * 
	 * @param resourceType
	 * @param resource
	 * @param maskArray
	 */
	public int[] getRolesOfResources(String resourceType, String resource, int[] maskArray)
		throws E5Exception
	{
		Permission[] pArr = getPermissions(resourceType, resource);
		if (pArr == null) return null;
	
		if (maskArray == null) {
			int[] roleArr = new int[pArr.length];
			for (int i = 0; i < pArr.length; i++) {
				roleArr[i] = pArr[i].getRoleID();
			}
			return roleArr;
		}
	
		//比较权限，得到合适的角色
		List list = new ArrayList(pArr.length);
		for (int i = 0; i < pArr.length; i++) {
			if (PermissionHelper.hasPermission(pArr[i], maskArray))
				list.add(new Integer(pArr[i].getRoleID()));
		}
		
		int size = list.size();
		if (size == 0) return null;
		int[] roleArr = new int[size];
		for (int i = 0; i < size; i++)
			roleArr[i] = ((Integer)list.get(i)).intValue();
		return roleArr;
	}

}