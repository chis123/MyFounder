package com.founder.e5.sys.org;
import com.founder.e5.context.E5Exception;
import com.founder.e5.sys.org.Role;


/**
 * @created 04-七月-2005 15:01:26
 * @version 1.0
 * @updated 11-七月-2005 12:42:36
 */
public interface RoleReader {

	/**
	 * 获得指定角色ID的角色对象
	 * @param roleID    roleID
	 * 
	 */
	public Role get(int roleID) throws E5Exception;

	/**
	 * 王朝阳 2006-4-19
	 * 获取所有角色对象
	 * @return
	 * @throws E5Exception
	 */

	public Role [] getRoles() throws E5Exception;
	/**
	 * 获得指定用户的所有角色
	 * @param userID    用户ID
	 * 
	 */
	public Role[] getRolesByUser(int userID) throws E5Exception;

	/**
	 * 获得在指定组织下的指定名称下的角色
	 * @param orgID    组织ID
	 * @param roleName    角色名称
	 * 
	 */
	public Role getRoleByName(int orgID, String roleName) throws E5Exception;
	
	
	/**
	 * 获得指定角色的父组织
	 * @param roleID    角色ID
	 * 
	 */
	public Org getParentOrgByRole(int roleID) throws E5Exception;

//新增加两个接口2006-4-14
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
	 * 王朝阳增加 2006-4-19
	 * 获得所有用户角色关系
	 * @return
	 * @throws E5Exception
	 */
	public UserRole [] getUserRoles() throws E5Exception;
	
	
	//王朝阳增加，实现角色查询 2006-3-8
	/*
	 * 根据角色名获取对应的角色
	 * 
	 * 
	 */
	 public Role [] getRolesByName(String roleName) throws E5Exception;

		//王朝阳增加，实现角色查询 2006-3-8
		/*
		 * 根据角色名中的某些词获取对应的角色
		 * 
		 * 
		 */
	 public Role [] getRolesIncludeName(String roleName) throws E5Exception;

}