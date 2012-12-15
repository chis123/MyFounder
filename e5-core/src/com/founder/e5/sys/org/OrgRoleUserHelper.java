package com.founder.e5.sys.org;

import com.founder.e5.context.CacheReader;
import com.founder.e5.context.Context;

public class OrgRoleUserHelper {

	public OrgRoleUserHelper() {
		super();
		// TODO Auto-generated constructor stub
	}
	static public OrgManager getOrgManager()
	{
		OrgManager orgManager = (OrgManager) Context.getBean(OrgManager.class);
		if(orgManager==null)
		{
			orgManager = OrgImplFactory.getOrgManager();
		}
		return orgManager;
	}
	static public UserManager getUserManager()
	{
		UserManager userManager = (UserManager) Context.getBean(UserManager.class);
		if(userManager==null)
		{
			userManager = OrgImplFactory.getUserManager();
		}
		return userManager;
		
	}
	
	static public RoleManager getRoleManager()
	{
		RoleManager roleManager = (RoleManager) Context.getBean(RoleManager.class);
		if(roleManager==null)
		{
			roleManager = OrgImplFactory.getRoleManager();
		}
		return roleManager;
		
	}
	static public OrgRoleUserCache getCache()
	{
		return (OrgRoleUserCache)CacheReader.find(OrgRoleUserCache.class);
	}
	
}
