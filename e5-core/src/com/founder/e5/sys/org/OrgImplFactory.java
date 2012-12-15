/*
 * �������� 2005-7-18
 *
 * TODO Ҫ���Ĵ����ɵ��ļ���ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
 */
package com.founder.e5.sys.org;

/**
 * @author qiming
 *
 * TODO Ҫ���Ĵ����ɵ�����ע�͵�ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
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
