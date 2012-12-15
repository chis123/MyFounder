package com.founder.e5.permission;

import com.founder.e5.context.E5Exception;

/**
 * 分类权限的读取器
 * @created 14-7-2005 16:23:40
 * @author Gong Lijie
 * @version 1.0
 */
public interface CatPermissionReader {

	/**
	 * 取一个角色对一个分类的权限
	 * @param roleID 角色ID
	 * @param catType 分类类型
	 * @param catID 分类ID
	 */
	public int get(int roleID, int catType, int catID) 
	throws E5Exception;

	/**
	 * 取一个角色的所有分类权限
	 * 
	 * @param roleID 角色ID
	 */
	public CatPermission[] getPermissionsByRole(int roleID) 
	throws E5Exception;

	/**
	 * 取一个角色对一个分类类型的所有分类权限
	 * 
	 * @param roleID 角色ID
	 * @param catType 分类类型
	 */
	public CatPermission[] getPermissionsByRole(int roleID, int catType) 
	throws E5Exception;
	
	/**
	 * 取对一个分类类型下所有分类的所有权限
	 * @param catType
	 * @return
	 * @throws E5Exception
	 */
	public CatPermission[] getPermissionsByCatType(int catType) 
	throws E5Exception;

	/**
	 * 取对一个分类的所有权限
	 * @param catType
	 * @param catID
	 * @return
	 * @throws E5Exception
	 */
	public CatPermission[] getPermissionsByCat(int catType, int catID) 
	throws E5Exception;
	
	/**
	 * 取一个角色对一个分类类型有某权限的所有分类
	 * 
	 * @param roleID 角色ID
	 * @param catType 分类类型
	 * @param permissionArray 权限标记
	 * <br/>权限标记是一个数组，只要拥有该数组中列出的其中一个权限标记，即认为匹配成功，可以返回该分类ID。
	 * <br/>当这个参数为空时，任何权限都可。
	 * <p>
	 * <li/>取角色5对分类类型4有读权限的所有分类:<br/>
	 * 		getCatsOfRole(5,4,{PERMISSION_READ})
	 * <li/>取角色5对分类类型4有读或者处理权限的所有分类:<br/>
	 * 		getCatsOfRole(5,4,{PERMISSION_READ, PERMISSION_PROCESS})
	 * <li/>取角色5对分类类型4有读并且处理权限的所有分类:<br/>
	 * 		getCatsOfRole(5,4,{PERMISSION_READ & PERMISSION_PROCESS})
	 * </p>
	 * 当系统只有一种分类权限时，第三个参数可直接传入null
	 */
	public int[] getCatsOfRole(int roleID, int catType, int[] permissionArray)
	throws E5Exception;

	/**
	 * 取一个角色对一个分类有某权限的所有子分类
	 * 该方法在上一层才提供，做adapter
	 */
//	public int[] getSubCategoriesOfRole(int roleID, int catType, int catID, int[] permissioArray)
//	throws E5Exception;

	/**
	 * 取对一个分类有某权限的所有角色
	 * 
	 * @param catType 分类类型
	 * @param catID 分类ID
	 * @param permissionArray 权限标记
	 * <br/>权限标记是一个数组，只要拥有该数组中列出的其中一个权限标记，即认为匹配成功，可以返回该角色ID。
	 * <br/>当这个参数为空时，任何权限都可。
	 * <p>
	 * <li/>取对分类4（分类类型5）有读权限的所有角色:<br/>
	 * 		getRolesOfCat(5,4,{PERMISSION_READ})
	 * <li/>取对分类5（分类类型4）有读或者处理权限的所有角色:<br/>
	 * 		getRolesOfCat(5,4,{PERMISSION_READ, PERMISSION_PROCESS})
	 * <li/>取对分类5（分类类型4）有读并且处理权限的所有角色:<br/>
	 * 		getRolesOfCat(5,4,{PERMISSION_READ & PERMISSION_PROCESS})
	 * </p>
	 * 当系统只有一种分类权限时，第三个参数可直接传入null
	 */
	public int[] getRolesOfCat(int catType, int catID, int[] permissionArray)
	throws E5Exception;

}