package com.founder.e5.permission;

import com.founder.e5.context.E5Exception;

/**
 * 系统管理中文件夹权限设置和读取的管理接口
 * 取角色权限时不带继承。
 * @created 14-7-2005 16:22:55
 * @author Gong Lijie
 * @version 1.0
 */
public interface FVPermissionManager extends FVPermissionReader {

	/**
	 * 保存权限
	 * 
	 * @param permission
	 */
	public void save(FVPermission permission) throws E5Exception;

	/**
	 * 保存多个权限
	 * 
	 * @param permissionArray
	 */
	public void save(FVPermission[] permissionArray) throws E5Exception;

	/**
	 * 删除一个文件夹视图时删除所有对其权限
	 * 
	 * @param fvID 文件夹视图ID
	 */
	public void deleteFolder(int fvID) throws E5Exception;
	
	/**
	 * 删除一个角色时删除它所有的文件夹权限
	 * @param roleID
	 * @throws E5Exception
	 */
	public void deleteRole(int roleID) throws E5Exception;

	/**
	 * 一次性取出所有文件夹权限
	 * @throws E5Exception
	 */
	public FVPermission[] getAll() throws E5Exception;

	/**
	 * 参照一个角色的权限来设置另外一个角色的文件夹权限
	 * 即角色的文件夹权限复制
	 * 注意，该角色原来的文件夹权限会先被全部清除
	 * @param srcRoleID
	 * @param destRoleID
	 * @throws E5Exception
	 */
	public void copy(int srcRoleID, int destRoleID) throws E5Exception;
}