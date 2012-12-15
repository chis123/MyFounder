package com.founder.e5.permission;

import com.founder.e5.context.E5Exception;

/**
 * 文件夹视图权限的读取器
 * 
 * @updated 18-7-2005 15:32:17
 * @author Gong Lijie
 * @version 1.0
 */
public interface FVPermissionReader {

	/**
	 * 取一个角色对一个文件夹/视图的权限
	 * 
	 * @param roleID    角色ID
	 * @param fvID    文件夹/视图ID
	 * @return int	权限值
	 * @throws E5Exception
	 */
	public int get(int roleID, int fvID) throws E5Exception;

	/**
	 * 取一个角色的所有的文件夹/视图权限
	 * @param roleID    角色ID
	 * @return FVPermission[]
	 * @throws E5Exception
	 */
	public FVPermission[] getPermissionsByRole(int roleID) throws E5Exception;

	/**
	 * 取某个文件夹的所有的权限记录，包括所有角色的权限记录
	 * @param fvID 文件夹ID
	 * @return FVPermission[]
	 * @throws E5Exception
	 */
	public FVPermission[] getPermissionsByFolder(int fvID) throws E5Exception;

	/**
	 * 取一个角色有某权限的所有文件夹/视图
	 * @param roleID    角色ID
	 * @param permissionArray    权限标记
	 * <br/>权限标记是一个数组，只要拥有该数组中列出的其中一个权限标记，即认为匹配成功，可以返回该文件夹/视图ID。
	 * <br/>当这个参数为空时，任何权限都可。
	 * <p>
	 * <li/>取角色5有读权限的所有文件夹:<br/>
	 * 		getFoldersOfRole(5,{FVPermission.PERMISSION_READ})
	 * <li/>取角色5有读或者处理权限的所有文件夹:<br/>
	 * 		getFoldersOfRole(5,{FVPermission.PERMISSION_READ, FVPermission.PERMISSION_PROCESS})
	 * <li/>取角色5有读并且处理权限的所有文件夹:<br/>
	 * 		getFoldersOfRole(5,{FVPermission.PERMISSION_READ & FVPermission.PERMISSION_PROCESS})
	 * </p>
	 */
	public int[] getFoldersOfRole(int roleID, int[] permissionArray) throws E5Exception;

	/**
	 * 取对某文件夹有某权限的所有角色
	 * 
	 * @param fvID    文件夹视图ID
	 * @param permissionArray    权限标记
	 * <br/>权限标记是一个数组，只要拥有该数组中列出的其中一个权限标记，即认为匹配成功，可以返回该角色ID。
	 * <br/>当这个参数为空时，任何权限都可。
	 * <p>
	 * <li/>取对文件夹5有读权限的所有角色:<br/>
	 * 		getRolesOfFolder(5,{FVPermission.PERMISSION_READ})
	 * <li/>取对文件夹5有读或者处理权限的所有角色:<br/>
	 * 		getRolesOfFolder(5,{FVPermission.PERMISSION_READ, FVPermission.PERMISSION_PROCESS})
	 * <li/>取对文件夹5有读并且处理权限的所有角色:<br/>
	 * 		getRolesOfFolder(5,{FVPermission.PERMISSION_READ & FVPermission.PERMISSION_PROCESS})
	 * </p>
	 */
	public int[] getRolesOfFolder(int fvID, int[] permissionArray) throws E5Exception;

}