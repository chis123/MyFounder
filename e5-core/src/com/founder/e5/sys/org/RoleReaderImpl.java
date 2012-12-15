package com.founder.e5.sys.org;
import com.founder.e5.context.E5Exception;
import com.founder.e5.sys.org.RoleReader;

/**
 * @version 1.0
 * @created 11-七月-2005 10:31:27
 */
class RoleReaderImpl implements RoleReader {

    private RoleManager roleManager = OrgRoleUserHelper.getRoleManager();
    
	public RoleReaderImpl(){

	}

	/**
	 * @param roleID    roleID
	 * @throws E5Exception
	 * 
	 */
	public Role get(int roleID) throws E5Exception{
	
		OrgRoleUserCache cache =  OrgRoleUserHelper.getCache();
		if(cache!=null)
		{
			return cache.getRole(roleID);
		}
	
		return roleManager.get(roleID);
	}

	/**
	 * 王朝阳 2006-4-19
	 * 获取所有角色对象
	 * @return
	 * @throws E5Exception
	 */

	public Role [] getRoles() throws E5Exception
	{
		OrgRoleUserCache cache =  OrgRoleUserHelper.getCache();
		if(cache!=null)
		{
			return cache.getRoles();
		}

		return roleManager.getRoles();
		
	}
	
	/**
	 * @param userID    userID
	 * @throws E5Exception
	 * 
	 */
	public Role[] getRolesByUser(int userID) throws E5Exception{
		OrgRoleUserCache cache =  OrgRoleUserHelper.getCache();
		if(cache!=null)
		{
			return cache.getRolesByUser(userID);
		}

		return roleManager.getRolesByUser(userID);
	}

	/**
	 * @param orgID
	 * @param roleName    roleName
	 * @throws E5Exception
	 * 
	 */
	public Role getRoleByName(int orgID, String roleName) throws E5Exception{
		OrgRoleUserCache cache =  OrgRoleUserHelper.getCache();
		if(cache!=null)
		{
			return cache.getRoleByName(orgID,roleName);
		}
		return roleManager.getRoleByName(orgID, roleName);
	}

	/**
	 * 获得指定角色的父组织
	 * @param roleID    角色ID
	 * 
	 */
	public Org getParentOrgByRole(int roleID) throws E5Exception
	{
		OrgRoleUserCache cache =  OrgRoleUserHelper.getCache();
		if(cache!=null)
		{
			return cache.getParentOrgByRole(roleID);
		}
	    return roleManager.getParentOrgByRole(roleID);
	}
	
//	新增加两个接口2006-4-14
	/**
	 * 获得指定用户的所有用户角色对象
	 * @param userID    用户ID
	 * 
	 */
	public UserRole[] getUserRoles(int userID) throws E5Exception
	{
		OrgRoleUserCache cache =  OrgRoleUserHelper.getCache();
		if(cache!=null)
		{
			return cache.getUserRoles(userID);
		}
		return roleManager.getUserRoles(userID);
	}

	/**
	 * 获得指定用户和指定角色下的用户角色对象
	 * @param userID    用户ID
	 * @param roleID    角色ID
	 * 
	 */
	public UserRole getUserRole(int userID, int roleID) throws E5Exception
	{
		OrgRoleUserCache cache =  OrgRoleUserHelper.getCache();
		if(cache!=null)
		{
			return cache.getUserRole(userID,roleID);
		}
		 return roleManager.getUserRole(userID,roleID);
	}
	
	/**
	 * 王朝阳增加 2006-4-19
	 * 获得所有用户角色关系
	 * @return
	 * @throws E5Exception
	 */
	public UserRole [] getUserRoles() throws E5Exception
	{
		OrgRoleUserCache cache =  OrgRoleUserHelper.getCache();
		if(cache!=null)
		{
			return cache.getUserRoles();
		}
		 return roleManager.getUserRoles();
		
	}
	
	//王朝阳增加，实现角色查询 2006-3-8
	/**
	 * 根据角色名获取对应的角色
	 * 
	 * 
	 */
	 public Role [] getRolesByName(String roleName) throws E5Exception
	 {
		 return roleManager.getRolesByName(roleName);
	 }

		//王朝阳增加，实现角色查询 2006-3-8
		/*
		 * 根据角色名中的某些词获取对应的角色
		 * 
		 * 
		 */
	 public Role [] getRolesIncludeName(String roleName) throws E5Exception
	 {
		return roleManager.getRolesIncludeName(roleName); 
	 }
	
}