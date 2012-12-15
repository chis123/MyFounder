/*
 * 创建日期 2005-7-22
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package com.founder.e5.sys;

/**
 * @author qiming
 *
 * TODO 要更改此生成的类型注释的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
public class SysFactory
{
    private static SysConfigManager sysConfigManager = null;
    private static SysConfigReader sysConfigReader = null;
    
    private static StorageDeviceManager storageDeviceManager;
    
    private static StorageDeviceReader storageDeviceReader;
    
    private static LoginUserManager loginUserManager;
    
    public static SysConfigManager getSysConfigManager()
    {
        if(sysConfigManager == null)
            sysConfigManager = new SysConfigManagerImpl();
        return sysConfigManager;
    }
    
    public static SysConfigReader getSysConfigReader()
    {
        if(sysConfigReader == null)
        	sysConfigReader = new SysConfigReaderImpl();
        return sysConfigReader;
    }
    
    public static StorageDeviceManager getStorageDeviceManager()
    {
        if(storageDeviceManager == null)
            storageDeviceManager = new StorageDeviceManagerImpl();
        return storageDeviceManager;
    }
    
    public static StorageDeviceReader getStorageDeviceReader()
    {
        if(storageDeviceReader == null)
            storageDeviceReader = new StorageDeviceReaderImpl();
        return storageDeviceReader;
    }
    
    public static LoginUserManager getLoginUserManager()
    {
        if(loginUserManager == null)
            loginUserManager = new LoginUserManagerImpl();
        return loginUserManager;
    }
}
