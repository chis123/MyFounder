package com.founder.e5.sys.org;
import com.founder.e5.context.E5Exception;
import com.founder.e5.sys.org.RoleReader;

/**
 * @version 1.0
 * @created 11-����-2005 10:31:27
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
	 * ������ 2006-4-19
	 * ��ȡ���н�ɫ����
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
	 * ���ָ����ɫ�ĸ���֯
	 * @param roleID    ��ɫID
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
	
//	�����������ӿ�2006-4-14
	/**
	 * ���ָ���û��������û���ɫ����
	 * @param userID    �û�ID
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
	 * ���ָ���û���ָ����ɫ�µ��û���ɫ����
	 * @param userID    �û�ID
	 * @param roleID    ��ɫID
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
	 * ���������� 2006-4-19
	 * ��������û���ɫ��ϵ
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
	
	//���������ӣ�ʵ�ֽ�ɫ��ѯ 2006-3-8
	/**
	 * ���ݽ�ɫ����ȡ��Ӧ�Ľ�ɫ
	 * 
	 * 
	 */
	 public Role [] getRolesByName(String roleName) throws E5Exception
	 {
		 return roleManager.getRolesByName(roleName);
	 }

		//���������ӣ�ʵ�ֽ�ɫ��ѯ 2006-3-8
		/*
		 * ���ݽ�ɫ���е�ĳЩ�ʻ�ȡ��Ӧ�Ľ�ɫ
		 * 
		 * 
		 */
	 public Role [] getRolesIncludeName(String roleName) throws E5Exception
	 {
		return roleManager.getRolesIncludeName(roleName); 
	 }
	
}