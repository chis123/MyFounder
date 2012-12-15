package com.founder.e5.sys;

/**
 * @version 1.0
 * @created 08-七月-2005 15:07:06
 */
public class StorageDevice {

    /**
	 * 设备类型  NFS
	 */
    public static final int DEVICE_TYPE_NFS = 1;
    /**
	 * 设备类型  NTFS
	 */
    public static final int DEVICE_TYPE_NTFS = 2;
    /**
	 * 设备类型  FTP
	 */
    public static final int DEVICE_TYPE_FTP = 3;
    /**
	 * 设备类型  HTTP
	 */
    public static final int DEVICE_TYPE_HTTP = 4;
    
	/**
	 * 设备名
	 */
	private String deviceName;
	/**
	 * 设备类型
	 */
	private int deviceType;
	/**
	 * ftp设备URL
	 */
	private String ftpDeviceURL;
	/**
	 * http设备URL
	 */
	private String httpDeviceURL;
	/**
	 * nft设备URL
	 */
	private String nfsDevicePath;
	/**
	 * ntfs设备URL
	 */
	private String ntfsDevicePath;
	/**
	 * 注释
	 */
	private String notes;
	/**
	 * 设备的访问用户名
	 */
	private String userName;
	/**
	 * 设备的访问代码
	 */
	private String userPassword;

	
	public StorageDevice(){

	}

	
    /**
     * @return 返回 deviceName。
     */
    public String getDeviceName()
    {
        return deviceName;
    }
    /**
     * @return 返回 deviceType。
     */
    public int getDeviceType()
    {
        return deviceType;
    }
    /**
     * @return 返回 ftpDeviceURL。
     */
    public String getFtpDeviceURL()
    {
        return ftpDeviceURL;
    }
    /**
     * @return 返回 httpDeviceURL。
     */
    public String getHttpDeviceURL()
    {
        return httpDeviceURL;
    }
    /**
     * @return 返回 nfsDevicePath。
     */
    public String getNfsDevicePath()
    {
        return nfsDevicePath;
    }
    /**
     * @return 返回 notes。
     */
    public String getNotes()
    {
        return notes;
    }
    /**
     * @return 返回 ntfsDevicePath。
     */
    public String getNtfsDevicePath()
    {
        return ntfsDevicePath;
    }
    /**
     * @return 返回 userName。
     */
    public String getUserName()
    {
        return userName;
    }
    /**
     * @return 返回 userPassword。
     */
    public String getUserPassword()
    {
        return userPassword;
    }
    /**
     * @param deviceName 要设置的 deviceName。
     */
    public void setDeviceName(String deviceName)
    {
        this.deviceName = deviceName;
    }
    /**
     * @param deviceType 要设置的 deviceType。
     */
    public void setDeviceType(int deviceType)
    {
        this.deviceType = deviceType;
    }
    /**
     * @param ftpDeviceURL 要设置的 ftpDeviceURL。
     */
    public void setFtpDeviceURL(String ftpDeviceURL)
    {
        this.ftpDeviceURL = ftpDeviceURL;
    }
    /**
     * @param httpDeviceURL 要设置的 httpDeviceURL。
     */
    public void setHttpDeviceURL(String httpDeviceURL)
    {
        this.httpDeviceURL = httpDeviceURL;
    }
    /**
     * @param nfsDevicePath 要设置的 nfsDevicePath。
     */
    public void setNfsDevicePath(String nfsDevicePath)
    {
        this.nfsDevicePath = nfsDevicePath;
    }
    /**
     * @param notes 要设置的 notes。
     */
    public void setNotes(String notes)
    {
        this.notes = notes;
    }
    /**
     * @param ntfsDevicePath 要设置的 ntfsDevicePath。
     */
    public void setNtfsDevicePath(String ntfsDevicePath)
    {
        this.ntfsDevicePath = ntfsDevicePath;
    }
    /**
     * @param userName 要设置的 userName。
     */
    public void setUserName(String userName)
    {
        this.userName = userName;
    }
    /**
     * @param userPassword 要设置的 userPassword。
     */
    public void setUserPassword(String userPassword)
    {
        this.userPassword = userPassword;
    }
}