package com.founder.e5.sys.org;

import com.founder.e5.context.E5Exception;


/**
 * @version 1.0
 * @created 11-����-2005 12:41:58
 */
class UserReaderImpl implements UserReader {

    private UserManager userManager = OrgRoleUserHelper.getUserManager();
    
	public UserReaderImpl(){

	}

	
	public User[] getUsers() throws E5Exception{
		OrgRoleUserCache cache =  OrgRoleUserHelper.getCache();
		if(cache!=null)
		{
			return cache.getUsers();
		}
		
		return userManager.getUsers();
	}

	/**
	 * 
	 * @param userID    userID
	 * @throws E5Exception
	 */
	public User getUserByID(int userID) throws E5Exception{
		OrgRoleUserCache cache =  OrgRoleUserHelper.getCache();
		if(cache!=null)
		{
			return cache.getUserByID(userID);
		}
		return userManager.getUserByID(userID);
	}

	/**
	 * 
	 * @param userCode    userCode
	 * @throws E5Exception
	 */
	public User getUserByCode(String userCode) throws E5Exception{
		OrgRoleUserCache cache =  OrgRoleUserHelper.getCache();
		if(cache!=null)
		{
			return cache.getUserByCode(userCode);
		}
		return userManager.getUserByCode(userCode);
	}

	/**
	 * 
	 * @param orgID    orgID
	 * @throws E5Exception
	 */
	public User[] getUsersByOrg(int orgID) throws E5Exception{
		OrgRoleUserCache cache =  OrgRoleUserHelper.getCache();
		if(cache!=null)
		{
			return cache.getUsersByOrg(orgID);
		}
		return userManager.getUsersByOrg(orgID);
	}

	/**
	 * 
	 * @param roleID    roleID
	 * @throws E5Exception
	 */
	public User[] getUsersByRole(int roleID) throws E5Exception{
		OrgRoleUserCache cache =  OrgRoleUserHelper.getCache();
		if(cache!=null)
		{
			return cache.getUsersByRole(roleID);
		}
		return userManager.getUsersByRole(roleID);
	}

	/**
	 * 
	 * @param userID
	 * @throws E5Exception
	 */
	public UserFolder[] getUserFolders(int userID) throws E5Exception{
		OrgRoleUserCache cache =  OrgRoleUserHelper.getCache();
		if(cache!=null)
		{
			return cache.getUserFolders(userID);
		}
		return userManager.getUserFolders(userID);
	}

	/**
	 * 
	 * @param docTypeID
	 * @param userID
	 * @throws E5Exception
	 */
	public UserFolder[] getUserFolders(int docTypeID, int userID) throws E5Exception{
		OrgRoleUserCache cache =  OrgRoleUserHelper.getCache();
		if(cache!=null)
		{
			return cache.getUserFolders(docTypeID,userID);
		}
		return userManager.getUserFolders(docTypeID, userID);
	}

	
	/**
	 * ���������� 2006-4-19
	 * ��������û��ļ���
	 * 
	 */
	public UserFolder[] getUserFolders() throws E5Exception
	{
		OrgRoleUserCache cache =  OrgRoleUserHelper.getCache();
		if(cache!=null)
		{
			return cache.getUserFolders();
		}
		return userManager.getUserFolders();
		
	}
	
	/**
	 * 
	 * @param userID
	 * @param libID
	 * @throws E5Exception
	 */
	public UserFolder getUserFolder(int userID, int libID) throws E5Exception{
		OrgRoleUserCache cache =  OrgRoleUserHelper.getCache();
		if(cache!=null)
		{
			return cache.getUserFolder(userID, libID);
		}
		return userManager.getUserFolder(userID, libID);

	}
	
	/**
	 * ���ָ���û��ĸ���֯
	 * @param userID    �û�ID
	 * 
	 */
	public Org getParentOrg(int userID) throws E5Exception
	{
		OrgRoleUserCache cache =  OrgRoleUserHelper.getCache();
		if(cache!=null)
		{
			User user = getUserByID(userID);
			if(user == null) return null;
			
			return cache.getOrg(user.getOrgID());
		}
		return userManager.getParentOrg(userID);
	}
	
	/**
	 * ���ָ���û��ĸ���֯
	 * @param userCode    �û�����
	 * 
	 */
	public Org getParentOrg(String userCode) throws E5Exception
	{
	    return userManager.getParentOrg(userCode);
	}
//	���������ӣ�Ϊ���ж�һ�������ڵ����Ƿ����û��ڵ� 2006-3-10
	public int getUserCountByOrg(int orgID) throws E5Exception
	{
	    return userManager.getUserCountByOrg(orgID);
	}	
//	���������ӣ�ʵ���û���ѯ 2006-3-8
	/*
	 * �����û�����ȡ��Ӧ���û�
	 * 
	 * 
	 */

	 public User [] getUsersByName(String userName) throws E5Exception
	 {
		 return userManager.getUsersByName(userName);
	 }
		//���������ӣ�ʵ���û���ѯ 2006-3-8
		/*
		 * �����û����е�ĳЩ�ʻ�ȡ��Ӧ���û�
		 * 
		 * 
		 */
	 public User [] getUsersIncludeName(String userName) throws E5Exception
	 {
		 return userManager.getUsersIncludeName(userName);
	 }
	 
}