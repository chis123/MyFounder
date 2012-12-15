/*
 * 创建日期 2005-7-18
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package com.founder.e5.sys.org;

/**
 * @author qiming
 *
 * TODO 要更改此生成的类型注释的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
public class OrgImplFactory
{
    
    private static OrgReader orgReader;
    
    private static OrgManager orgManager;
    
    private static UserReader userReader;
    
    private static UserManager userManager;
    
    private static RoleReader roleReader;
    
    private static RoleManager roleManager;
    
    private static OrgTypeReader typeReader;
    
    public static OrgReader getOrgReader()
    {
        if(orgReader == null)
            orgReader = new OrgReaderImpl();
        return orgReader;
    }
    
    public static OrgManager getOrgManager()
    {
        if(orgManager == null)
            orgManager = new OrgManagerImpl();
        return orgManager;
    }
    
    public static UserReader getUserReader()
    {
        if(userReader == null)
            userReader = new UserReaderImpl();
        return userReader;
    }
    
    public static UserManager getUserManager()
    {
        if(userManager == null)
            userManager = new UserManagerImpl();
        return userManager;
    }
    
    public static RoleReader getRoleReader()
    {
        if(roleReader == null)
            roleReader = new RoleReaderImpl();
        return roleReader;
    }
    
    public static RoleManager getRoleManager()
    {
        if(roleManager == null)
            roleManager = new RoleManagerImpl();
        return roleManager;
    }
    
    public static OrgTypeReader getOrgTypeReader()
    {
        if(typeReader == null)
            typeReader = new OrgTypeReaderImpl();
        return typeReader;
    }
}
