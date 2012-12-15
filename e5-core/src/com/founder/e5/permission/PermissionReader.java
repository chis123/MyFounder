package com.founder.e5.permission;

import com.founder.e5.context.E5Exception;

/**
 * 权限的读取器
 * @created 14-7-2005 16:25:16
 * @author Gong Lijie
 * @version 1.0
 */
public interface PermissionReader {

	/**
	 * 取角色权限
	 * @param roleID 角色ID
	 * @param resourceType 资源类型
	 * @param resource 资源名
	 */
	public Permission get(int roleID, String resourceType, String resource)
		throws E5Exception;

	/**
	 * 取角色的所有权限
	 * 参数：角色ID
	 * 
	 * @param roleID
	 */
	public Permission[] getPermissions(int roleID)throws E5Exception;

	/**
	 * 取角色的某资源类型的权限
	 * 参数：角色ID，资源类型
	 * 
	 * @param roleID
	 * @param resourceType
	 */
	public Permission[] getPermissions(int roleID, String resourceType)throws E5Exception;

	/**
	 * 取某资源类型的所有权限
	 * 参数：资源类型
	 * 
	 * @param resourceType
	 */
	public Permission[] getPermissions(String resourceType)throws E5Exception;

	/**
	 * 取某资源的所有权限
	 * 参数：资源类型，资源名
	 * 
	 * @param resourceType
	 * @param resource
	 */
	public Permission[] getPermissions(String resourceType, String resource)throws E5Exception;

	/**
	 * 取某角色有某些权限的所有资源的名称
	 * @param roleID 角色ID
	 * @param resourceType 资源类型
	 * @param maskArray 权限标记
	 * <br/>权限标记是一个数组，只要拥有该数组中列出的其中一个权限标记，即认为匹配成功，可以返回该资源。
	 * <br/>当这个参数为空时，任何权限都可。
	 * <p>
	 * <li/>取角色5对某资源类型有读权限的所有资源:<br/>
	 * 		getResourcesOfRole(5, resourceTypeName,{PERMISSION_READ})
	 * <li/>取角色5对某资源类型有读或者处理权限的所有资源:<br/>
	 * 		getResourcesOfRole(5, resourceTypeName,{PERMISSION_READ, PERMISSION_PROCESS})
	 * <li/>取角色5对某资源类型有读并且处理权限的所有资源:<br/>
	 * 		getResourcesOfRole(5, resourceTypeName,{PERMISSION_READ & PERMISSION_PROCESS})
	 * </p>
	 */
	public String[] getResourcesOfRole(int roleID, String resourceType, int[] maskArray)throws E5Exception;

	/**
	 * 取对某资源有某些权限的所有角色的ID
	 * @param resourceType 资源类型
	 * @param resource 资源名
	 * @param permissionArray 权限标记
	 * <br/>权限标记是一个数组，只要拥有该数组中列出的其中一个权限标记，即认为匹配成功，可以返回该角色ID。
	 * <br/>当这个参数为空时，任何权限都可。
	 * <p>
	 * <li/>取对某资源有读权限的所有角色:<br/>
	 * 		getRolesOfResources(resourceTypeName, resourceName,{PERMISSION_READ})
	 * <li/>取对某资源有读或者处理权限的所有角色:<br/>
	 * 		getRolesOfResources(resourceTypeName, resourceName,{PERMISSION_READ, PERMISSION_PROCESS})
	 * <li/>取对某资源有读并且处理权限的所有角色:<br/>
	 * 		getRolesOfResources(resourceTypeName, resourceName,{PERMISSION_READ & PERMISSION_PROCESS})
	 * </p>
	 */
	public int[] getRolesOfResources(String resourceType, String resource, int[] permissionArray)throws E5Exception;
	
}