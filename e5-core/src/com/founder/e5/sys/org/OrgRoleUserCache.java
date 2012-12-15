package com.founder.e5.sys.org;

import java.util.ArrayList;
import java.util.List;

import com.founder.e5.context.Cache;
import com.founder.e5.context.E5Exception;

public class OrgRoleUserCache implements Cache {

	/**
	 * 缓存类主要用在Reader实现类中<br>
	 * 在引用时，注意不要使用构造方法创建缓存类的实例<br>
	 * 请使用如下形式：<br>
	 * <code>
	 * XXXCache cache = (XXXCache)(CacheReader.find(XXXCache.class));
	 * if (cache != null)
	 * 		return cache.get(...);
	 * </code>
	 * 其中XXXCache代表具体的缓存类
	 */
	public OrgRoleUserCache() {
		super();
	}
	private Org [] orgs;
	private User [] users;
	private Role [] roles;
	private UserRole [] userRoles;
	private UserFolder [] userFolders;
	private DefaultFolder[] defaultFolders;

	public void refresh() throws E5Exception{
		OrgManager orgManager = OrgRoleUserHelper.getOrgManager();
		RoleManager roleManager = OrgRoleUserHelper.getRoleManager();
		UserManager userManager = OrgRoleUserHelper.getUserManager(); 

		try {
			orgs = orgManager.get();
			roles = roleManager.getRoles();
			users = userManager.getUsers();
			userRoles = roleManager.getUserRoles();
			userFolders = userManager.getUserFolders();

			//若系统没有缺省工作目录（机构）的相关设置，则会报错
			defaultFolders = orgManager.getDefaultFolders();
		} catch (E5Exception e) {
			//使缓存刷新时不报错
			e.printStackTrace();
		}
	}
	
	public void reset(){
	}
	
	public Org getOrg(int orgID)
	{
		if(orgs==null)
		{
			return null;
		}
		for(int i=0;i<orgs.length;i++)
		{
			if(orgs[i].getOrgID()==orgID)
			{
				return orgs[i]; 
			}
		}	
		return null;
	}
	
	public Org getParentOrg(int orgID)
	{
		if(orgs==null)
		{
			return null;
		}

		Org org = getOrg(orgID);
		if(org==null)
		{
			return null;
		}
		return getOrg(org.getParentID());
	}
	
	public Org [] getNextChildOrgs(int orgID)
	{
		if(orgs==null)
		{
			return null;
		}

	  Org [] temporgs=null;
	  List list=getNextChildOrgList(orgID);
      if(list.size() > 0)
       {
    	  temporgs = new Org[list.size()];
    	   list.toArray(temporgs);
    	   return temporgs;
       }
       return null;
	}
	
	public Org [] getOrgs()
	{
		return orgs;
	}
	
	public Org [] getChildOrgs(int orgID)
	{
		if(orgs==null)
		{
			return null;
		}

		Org [] tempOrgs=null;
		List list = getChildOrgList(orgID);
	    if(list.size() > 0)
	    {
	    	tempOrgs = new Org[list.size()];	    	
	   	   list.toArray(tempOrgs);
    	   return tempOrgs;
	    }
	    return null;
		
	}
	
	public Role [] getRolesByOrg(int orgID)
	{
		if(roles==null)
		{
			return null;
		}

		Role [] tempRoles=null;
		List list=getNextRoleList(orgID);
       if(list.size() > 0)
        {
    	   tempRoles = new Role[list.size()];
    	  list.toArray(tempRoles);
    	  return tempRoles;
        }
       return null;
	}
	
	public Role[] getRoles(int orgID)
	{
		Role [] tempRoles=null;
		List list=getRoleList(orgID);
       if(list.size() > 0)
        {
    	   tempRoles = new Role[list.size()];
    	   list.toArray(tempRoles);
    	   return tempRoles;
        }
       return null;
		
	}
	
	private List getNextChildOrgList(int orgID)
	{
		List list= new ArrayList();
		for(int i=0;i<orgs.length;i++)
		{
			if(orgs[i].getParentID()==orgID)
			{
				list.add(orgs[i]);
			}
		}
		return list;
 	}
	private List getChildOrgList(int orgID)
	{
		  List list=getNextChildOrgList(orgID);
		  for(int i=0;i<list.size();i++)
		  {
			  Org org = (Org)list.get(i);
			  list.addAll(getChildOrgList(org.getOrgID()));
		  }
		  return list;
	}
	private List getNextRoleList(int orgID)
	{
		List list= new ArrayList();
		for(int i=0;i<roles.length;i++)
		{
			if(roles[i].getOrgID()==orgID)
			{
				list.add(roles[i]);
			}
		}
		return list;
	}
	private List getRoleList(int orgID)
	{
		List list = getNextRoleList(orgID);
		Org [] orgs =getChildOrgs(orgID);
		for(int i=0;i<orgs.length;i++)
		{
			list.addAll(getNextRoleList(orgs[i].getOrgID()));
		}
		return list;
		
	}
	
//以下是针对角色操作的缓存
	public Role getRole(int roleid)
	{
		if(roles==null)
		{
			return null;
		}
		for(int i=0;i<roles.length;i++)
		{
			if(roles[i].getRoleID()==roleid)
			{
				return roles[i];
			}
		}
		return null;
	}
	
	public Role[] getRolesByUser(int userid)
	{
//没有成员返回空数组		
		if(userRoles==null || roles==null)
		{
			return new Role[0];
		}
		
		List list= new ArrayList();
		for(int i=0;i<userRoles.length;i++)
		{
			if(userRoles[i].getUserID()==userid)
			{
				//有可能由垃圾数据
				Role role = getRole(userRoles[i].getRoleID()); 
				if(role!=null)
				{
					list.add(role);
				}
			}
		}
		Role[] tempRoles=new Role[list.size()];
		list.toArray(tempRoles);
		return tempRoles;
	}
	
	public Role[] getRoles()
	{
		if(roles==null)
		{
			return new Role[0];
		}
		return roles;
	}
	
	public Org getParentOrgByRole(int roleID)
	{
		if(roles==null)
		{
			return null;
		}
		
		for(int i=0;i<roles.length;i++)
		{
			if(roles[i].getRoleID()==roleID)
			{
				return getOrg(roles[i].getOrgID());
			}
		}
		return null;
	}
	
	public UserRole[] getUserRoles()
	{
//		没有成员返回空数组		
		if(userRoles==null )
		{
			return new UserRole[0];
		}
		return userRoles;
	}
	
	public UserRole[] getUserRoles(int userID)
	{
//		没有成员返回空数组		
		if(userRoles==null )
		{
			return new UserRole[0];
		}

		List list= new ArrayList();
		for(int i=0;i<userRoles.length;i++)
		{
			if(userRoles[i].getUserID()==userID)
			{
				list.add(userRoles[i]);
			}
		}
		UserRole[] tempUserRoles=new UserRole[list.size()];
		list.toArray(tempUserRoles);
		return tempUserRoles;
	}
	
	public UserRole getUserRole(int userID, int roleID)
	{
		if(userRoles==null )
		{
			return null;
		}

		for(int i=0;i<userRoles.length;i++)
		{
			if(userRoles[i].getUserID()==userID && userRoles[i].getRoleID()==roleID)
			{
				return userRoles[i];
			}
		}
		return null;
	}
	
	public Role getRoleByName(int orgID, String roleName)
	{
		if(roles==null)
		{
			return null;
		}
		for(int i=0;i<roles.length;i++)
		{
			if(roles[i].getOrgID()==orgID && roles[i].getRoleName().equals(roleName))
			{
				return roles[i];
			}
		}
		return null;
		
	}

//以下对用户的缓存
	public User[] getUsers()
	{
		return users;
	}
	public User getUserByID(int userID)
	{
		if(users==null)
		{
			return null;
		}
		
		for(int i=0;i<users.length;i++)
		{
			if(users[i].getUserID()==userID)
			{
				return users[i];
			}
				
		}
		return null;
	}
	public User getUserByCode(String userCode)
	{
		if(users==null)
		{
			return null;
		}
		for(int i=0;i<users.length;i++)
		{
			if(users[i].getUserCode().equals(userCode))
			{
				return users[i];
			}
				
		}
		return null;
	}
	
	public User[] getUsersByOrg(int orgID)
	{
//		没有成员返回空数组		
		if(users==null)
		{
			return new User[0];
		}
		List list= new ArrayList();
		for(int i=0;i<users.length;i++)
		{
			if(users[i].getOrgID()==orgID)
			{
				list.add(users[i]);
			}
		}
		return getUsersFromList(list);
	}
	
	public User[] getUsersByRole(int roleID) 
	{
//		没有成员返回空数组		
		if(userRoles==null)
		{
			return new User[0];
		}
		
		List list= new ArrayList();
		for(int i=0;i<userRoles.length;i++)
		{
			if(userRoles[i].getRoleID()==roleID)
			{
				//有可能由垃圾数据
				User user = getUserByID(userRoles[i].getUserID());
				if(user!=null)
				{
					list.add(user);
				}
			}
		}
		return getUsersFromList(list);
	}
	
	private User [] getUsersFromList(List list)
	{
		User [] tempUsers=new User[list.size()];
		list.toArray(tempUsers);
		return tempUsers;
		
	}
	
	public UserFolder[] getUserFolders(int userID)
	{
		if (userFolders == null) {
			return new UserFolder[0];
		}
		List list = new ArrayList();
		for (int i = 0; i < userFolders.length; i++) {
			if (userFolders[i].getUserID() == userID) {
				list.add(userFolders[i]);
			}
		}
		return getUserFoldersFromList(list);
	}
	
	public UserFolder [] getUserFolders()
	{
		if (userFolders == null) {
			return new UserFolder[0];
		}
		return userFolders;
	}
	
	public UserFolder [] getUserFolders(int docTypeID, int userID)
	{
		// 没有成员返回空数组
		if (userFolders == null) {
			return new UserFolder[0];
		}
		List list = new ArrayList();
		for (int i = 0; i < userFolders.length; i++) {
			if (userFolders[i].getUserID() == userID
					&& userFolders[i].getDocTypeID() == docTypeID) {
				list.add(userFolders[i]);
			}
		}
		return getUserFoldersFromList(list);
	}
	
	public UserFolder getUserFolder(int userID, int libID)
	{
		//				
		if (userFolders == null) {
			return null;
		}
		for (int i = 0; i < userFolders.length; i++) {
			if (userFolders[i].getUserID() == userID
					&& userFolders[i].getLibID() == libID) {
				return userFolders[i];
			}
		}
		return null;
	}
	
	private UserFolder[] getUserFoldersFromList(List list) {
		UserFolder[] tempUserFolders = new UserFolder[list.size()];
		list.toArray(tempUserFolders);
		return tempUserFolders;
	}
			
	private DefaultFolder[] getDefaultFoldersFromList(List list) {
		DefaultFolder[] tempFolders = new DefaultFolder[list.size()];
		list.toArray(tempFolders);
		return tempFolders;
	}

	public DefaultFolder[] getDefaultFolders(int orgID) throws E5Exception {
		if (defaultFolders == null) {
			return new DefaultFolder[0];
		}
		List list = new ArrayList();
		for (int i = 0; i < defaultFolders.length; i++) {
			if (defaultFolders[i].getId() == orgID) {
				list.add(defaultFolders[i]);
			}
		}
		return getDefaultFoldersFromList(list);
	}

	public DefaultFolder getDefaultFolder(int orgID, int docTypeID)
	throws E5Exception {
		if (defaultFolders == null) return null;
		
		for (int i = 0; i < defaultFolders.length; i++) {
			if (defaultFolders[i].getId() == orgID
					&& defaultFolders[i].getDocTypeID() == docTypeID) {
				return defaultFolders[i];
			}
		}
		return null;
	}

	public DefaultFolder[] getDefaultFolders() throws E5Exception {
		if (defaultFolders == null) {
			return new DefaultFolder[0];
		}
		return defaultFolders;
	}
}
