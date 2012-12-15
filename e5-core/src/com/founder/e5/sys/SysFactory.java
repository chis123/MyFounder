/*
 * �������� 2005-7-22
 *
 * TODO Ҫ���Ĵ����ɵ��ļ���ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
 */
package com.founder.e5.sys;

/**
 * @author qiming
 *
 * TODO Ҫ���Ĵ����ɵ�����ע�͵�ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
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
