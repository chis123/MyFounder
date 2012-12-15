package com.founder.e5.app;

import java.io.Serializable;




/**
 * @created 21-六月-2005 14:41:32
 * @version 1.0
 */
public class App implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int hashCode = Integer.MIN_VALUE;
	/**
	 * 子系统名
	 */
	private String name;
	/**
	 * 子系统版本缩略名
	 */
	private String absVersion;
	/**
	 * 子系统版本名
	 */
	private String version;
	/**
	 * 子系统提供者
	 */
	private String provider;
	/**
	 * 子系统ID
	 */
	private int appID;
	/**
	 * 用于保存子系统装载的初始化类信息
	 */
	private String initInfo;
	
	
	public App(){

	}

	
    /**
     * @return 返回 appAbsVersion。
     */
    public String getAbsVersion()
    {
        return absVersion;
    }
    /**
     * @return 返回 appID。
     */
    public int getAppID()
    {
        return appID;
    }
    /**
     * @return 返回 appName。
     */
    public String getName()
    {
        return name;
    }
    /**
     * @return 返回 appProvider。
     */
    public String getProvider()
    {
        return provider;
    }
    /**
     * @return 返回 appVersion。
     */
    public String getVersion()
    {
        return version;
    }
    /**
     * @return 返回 initInfo。
     */
    public String getInitInfo()
    {
        return initInfo;
    }
    /**
     * @param appAbsVersion 要设置的 appAbsVersion。
     */
    public void setAbsVersion(String absVersion)
    {
        this.absVersion = absVersion;
    }
    /**
     * @param appID 要设置的 appID。
     */
    public void setAppID(int appID)
    {
        this.appID = appID;
    }
    /**
     * @param appName 要设置的 appName。
     */
    public void setName(String name)
    {
        this.name = name;
    }
    /**
     * @param appProvider 要设置的 appProvider。
     */
    public void setProvider(String provider)
    {
        this.provider = provider;
    }
    /**
     * @param appVersion 要设置的 appVersion。
     */
    public void setVersion(String version)
    {
        this.version = version;
    }
    /**
     * @param initInfo 要设置的 initInfo。
     */
    public void setInitInfo(String initInfo)
    {
        this.initInfo = initInfo;
    }
    
    public boolean equals (Object obj) 
    {
        if(obj == null) return false;
        if(! (obj instanceof App))
            return false;
        else
        {
            App app = (App)obj;
            if(app.getName() != null && app.getName().equals(this.name) && app.getVersion() == this.version)
                return true;
        }
        return false;
    }
    
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		if (Integer.MIN_VALUE == this.hashCode) 
		{
			StringBuffer sb = new StringBuffer(100);
			sb.append(name).append(':').append(version);
			this.hashCode = sb.toString().hashCode();
		}
		return this.hashCode;
	}
    
}