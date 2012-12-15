package com.founder.e5.sys;

/**
 * @version 1.0
 * @created 08-����-2005 15:07:06
 */
public class StorageDevice {

    /**
	 * �豸����  NFS
	 */
    public static final int DEVICE_TYPE_NFS = 1;
    /**
	 * �豸����  NTFS
	 */
    public static final int DEVICE_TYPE_NTFS = 2;
    /**
	 * �豸����  FTP
	 */
    public static final int DEVICE_TYPE_FTP = 3;
    /**
	 * �豸����  HTTP
	 */
    public static final int DEVICE_TYPE_HTTP = 4;
    
	/**
	 * �豸��
	 */
	private String deviceName;
	/**
	 * �豸����
	 */
	private int deviceType;
	/**
	 * ftp�豸URL
	 */
	private String ftpDeviceURL;
	/**
	 * http�豸URL
	 */
	private String httpDeviceURL;
	/**
	 * nft�豸URL
	 */
	private String nfsDevicePath;
	/**
	 * ntfs�豸URL
	 */
	private String ntfsDevicePath;
	/**
	 * ע��
	 */
	private String notes;
	/**
	 * �豸�ķ����û���
	 */
	private String userName;
	/**
	 * �豸�ķ��ʴ���
	 */
	private String userPassword;

	
	public StorageDevice(){

	}

	
    /**
     * @return ���� deviceName��
     */
    public String getDeviceName()
    {
        return deviceName;
    }
    /**
     * @return ���� deviceType��
     */
    public int getDeviceType()
    {
        return deviceType;
    }
    /**
     * @return ���� ftpDeviceURL��
     */
    public String getFtpDeviceURL()
    {
        return ftpDeviceURL;
    }
    /**
     * @return ���� httpDeviceURL��
     */
    public String getHttpDeviceURL()
    {
        return httpDeviceURL;
    }
    /**
     * @return ���� nfsDevicePath��
     */
    public String getNfsDevicePath()
    {
        return nfsDevicePath;
    }
    /**
     * @return ���� notes��
     */
    public String getNotes()
    {
        return notes;
    }
    /**
     * @return ���� ntfsDevicePath��
     */
    public String getNtfsDevicePath()
    {
        return ntfsDevicePath;
    }
    /**
     * @return ���� userName��
     */
    public String getUserName()
    {
        return userName;
    }
    /**
     * @return ���� userPassword��
     */
    public String getUserPassword()
    {
        return userPassword;
    }
    /**
     * @param deviceName Ҫ���õ� deviceName��
     */
    public void setDeviceName(String deviceName)
    {
        this.deviceName = deviceName;
    }
    /**
     * @param deviceType Ҫ���õ� deviceType��
     */
    public void setDeviceType(int deviceType)
    {
        this.deviceType = deviceType;
    }
    /**
     * @param ftpDeviceURL Ҫ���õ� ftpDeviceURL��
     */
    public void setFtpDeviceURL(String ftpDeviceURL)
    {
        this.ftpDeviceURL = ftpDeviceURL;
    }
    /**
     * @param httpDeviceURL Ҫ���õ� httpDeviceURL��
     */
    public void setHttpDeviceURL(String httpDeviceURL)
    {
        this.httpDeviceURL = httpDeviceURL;
    }
    /**
     * @param nfsDevicePath Ҫ���õ� nfsDevicePath��
     */
    public void setNfsDevicePath(String nfsDevicePath)
    {
        this.nfsDevicePath = nfsDevicePath;
    }
    /**
     * @param notes Ҫ���õ� notes��
     */
    public void setNotes(String notes)
    {
        this.notes = notes;
    }
    /**
     * @param ntfsDevicePath Ҫ���õ� ntfsDevicePath��
     */
    public void setNtfsDevicePath(String ntfsDevicePath)
    {
        this.ntfsDevicePath = ntfsDevicePath;
    }
    /**
     * @param userName Ҫ���õ� userName��
     */
    public void setUserName(String userName)
    {
        this.userName = userName;
    }
    /**
     * @param userPassword Ҫ���õ� userPassword��
     */
    public void setUserPassword(String userPassword)
    {
        this.userPassword = userPassword;
    }
}