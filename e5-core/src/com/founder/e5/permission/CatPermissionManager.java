package com.founder.e5.permission;

import com.founder.e5.context.E5Exception;

/**
 * @created 14-7-2005 16:23:34
 * @author Gong Lijie
 * @version 1.0
 */
public interface CatPermissionManager extends CatPermissionReader {

	/**
	 * 权限的增量保存
	 * 当标记为增量保存时，与已有的权限做或操作
	 * 
	 * @param permission 权限
	 * @param incremental 是否增量保存
	 */
	public void save(CatPermission permission, boolean incremental) throws E5Exception;

	/**
	 * 多个权限的增量保存
	 * 当标记为增量保存时，与已有的权限做或操作
	 * 
	 * @param permissionArray 权限数组
	 * @param incremental 是否增量保存
	 */
	public void save(CatPermission[] permissionArray, boolean incremental) throws E5Exception;

	/**
	 * 删除一个分类时删除所有对其权限
	 * 
	 * @param catType 分类类型
	 * @param catID 分类
	 */
	public void deleteCat(int catType, int catID) throws E5Exception;

	/**
	 * 删除一个分类类型时删除所有对其权限
	 * 
	 * @param catType 分类类型
	 */
	public void deleteCatType(int catType) throws E5Exception;
}