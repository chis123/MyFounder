package com.founder.e5.sys.org;
import com.founder.e5.context.E5Exception;
import com.founder.e5.sys.org.Role;
import com.founder.e5.sys.org.UserRole;
import com.founder.e5.sys.org.RoleReader;

/**
 * @created 04-七月-2005 15:01:26
 * @version 1.0
 * @updated 11-七月-2005 12:42:55
 */
public interface RoleManager extends RoleReader {

	/**
	 * 添加角色
	 * @param orgID    父组织ID
	 * @param roleName    角色名
	 * 
	 */
	public int create(int orgID, String roleName) throws E5Exception;

	/**
	 * 更新角色
	 * @param role    角色对象
	 * 
	 */
	public void update(Role role) throws E5Exception;

	/**
	 * 删除角色
	 * @param roleID    角色ID
	 * 
	 */
	public void delete(int roleID) throws E5Exception;

	/**
	 * 获得指定用户的所有用户角色对象
	 * @param userID    用户ID
	 * 
	 */
	public UserRole[] getUserRoles(int userID) throws E5Exception;

	/**
	 * 获得指定用户和指定角色下的用户角色对象
	 * @param userID    用户ID
	 * @param roleID    角色ID
	 * 
	 */
	public UserRole getUserRole(int userID, int roleID) throws E5Exception;

	/**
	 * 将角色授予某个用户
	 * @param userID    用户ID
	 * @param userRole    用户角色对象
	 * 
	 */
	public void grantRole(int userID, UserRole userRole) throws E5Exception;

	/**
	 * 将某个角色从指定用户上取消
	 * @param userID    用户ID
	 * @param roleID    角色ID
	 * 
	 */
	public void revokeRole(int userID, int roleID) throws E5Exception;

	/**
	 * 按参数指定的顺序对角色进行排序
	 * @param roleIDs    角色ID数组
	 */
	public void sortRoles(int[] roleIDs) throws E5Exception;
}