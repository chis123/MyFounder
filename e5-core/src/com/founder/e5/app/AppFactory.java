/*
 * 创建日期 2005-7-19
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package com.founder.e5.app;

/**
 * @author qiming
 *
 * TODO 要更改此生成的类型注释的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
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
