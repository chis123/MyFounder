/*
 * �������� 2005-7-19
 *
 * TODO Ҫ���Ĵ����ɵ��ļ���ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
 */
package com.founder.e5.app;

/**
 * @author qiming
 *
 * TODO Ҫ���Ĵ����ɵ�����ע�͵�ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
 */
public class AppFactory
{
    
    private static AppManager appManager = null;
    
    private static AppPermissionManager appPermissionManager = null;
    
    private static AppWebAddressManager appWebAddressManager = null;
    
    
    public static AppManager getAppManager()
    {
        if(appManager == null)
            appManager = new AppManagerImpl();
        return appManager;
    }
    
    public static AppPermissionManager getAppPermissionManager()
    {
        if(appPermissionManager == null)
            appPermissionManager = new AppPermissionManagerImpl();
        return appPermissionManager;
    }
    
    public static AppWebAddressManager getAppWebAddressManager()
    {
        if(appWebAddressManager == null)
            appWebAddressManager = new AppWebAddressManagerImpl();
        return appWebAddressManager;
    }
}
