package com.founder.e5.permission;

import com.founder.e5.context.E5Exception;

/**
 * @created 14-7-2005 16:25:11
 * @author Gong Lijie
 * @version 1.0
 */
public interface PermissionManager extends PermissionReader {

	/**
	 * 权限保存
	 * 权限为0时，删除表中的权限记录
	 * 
	 * @param permission
	 */
	public void save(Permission permission)throws E5Exception;

	/**
	 * 权限的增量保存
	 * 当标记为增量保存时，与已有的权限做或操作
	 * 权限为0时，删除表中的权限记录
	 * 
	 * @param permission
	 * @param incremental
	 */
	public void save(Permission permission, boolean incremental)throws E5Exception;

	/**
	 * 多个权限保存
	 * 权限为0时，删除表中的权限记录
	 * 
	 * @param permissionArray
	 */
	public void save(Permission[] permissionArray)throws E5Exception;

	/**
	 * 多个权限的增量保存
	 * 当标记为增量保存时，与已有的权限做或操作
	 * 权限为0时，删除表中的权限记录
	 * 
	 * @param permissionArray
	 * @param incremental
	 */
	public void save(Permission[] permissionArray, boolean incremental)throws E5Exception;

	/**
	 * 按角色删除所有权限
	 * 当某个角色被删除时，调用此方法进行角色对应权限的清理
	 * 
	 * @param id 角色ID
	 */
	public void deleteRole(int id)throws E5Exception;

	/**
	 * 按资源类型删除所有权限
	 * 当某类资源被删除时，调用此方法进行相应权限的清理
	 * @param resourceType 资源类型
	 */
	public void deleteResourceType(String resourceType)throws E5Exception;

	/**
	 * 按资源名删除所有权限
	 * 当某个资源被删除时，调用此方法进行相应权限的清理
	 * 
	 * @param resourceType 资源类型
	 * @param resource 资源名
	 */
	public void deleteResource(String resourceType, String resource)throws E5Exception;
	
	/**
	 * 删除某角色对某资源类型的所有权限
	 * 当需要重新设置某个角色的权限时，有时需要按资源类型先把所有对应的权限都清除。这种情况下
	 * 适用此方法
	 * @param roleID 角色ID
	 * @param resourceType 资源类型
	 * @throws E5Exception
	 */
	public void delete(int roleID, String resourceType) throws E5Exception;
	/**
	 * 取出所有的权限记录
	 * @return 权限记录数组
	 * @throws E5Exception
	 */
	public Permission[] getAll() throws E5Exception;
	
	/**
	 * 按一定的资源类型条件查找权限
	 * @param roleID 角色ID
	 * @param resourceTypePattern 资源类型的模式
	 * <p>
	 * 资源类型可以是like中的模式
	 * 如find(1,"FLOW%")表示查找角色1的所有流程权限
	 * find(1,"CATTYPE%")表示查找角色1的所有分类权限
	 * </p>
	 * @return Permission[]
	 * @throws E5Exception
	 */
	public Permission[] find(int roleID, String resourceTypePattern) throws E5Exception;

	/**
	 * 参照一个角色的权限来设置另外一个角色的权限
	 * 即角色的权限复制
	 * 注意，该角色原来的权限会先被全部清除
	 * @param srcRoleID
	 * @param destRoleID
	 * @throws E5Exception
	 */
	public void copy(int srcRoleID, int destRoleID) throws E5Exception;
}