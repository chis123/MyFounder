package com.founder.e5.app;

import java.io.Serializable;

/**
 * @version 1.0
 * @updated 08-七月-2005 15:18:14
 */
public class AppWebAddress implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int hashCode = Integer.MIN_VALUE;
	/**
	 * 子系统ID
	 */
	private int appID;
	/**
	 * 子系统管理页面名
	 */
	private String webName;
	/**
	 * 子系统管理页面URL
	 */
	private String webURL;
	/**
	 * 子系统管理页面的图标
	 */
	private String icon;

	public AppWebAddress(){

	}


    /**
     * @return 返回 appID。
     */
    public int getAppID()
    {
        return appID;
    }
    /**
     * @return 返回 icon。
     */
    public String getIcon()
    {
        return icon;
    }
    /**
     * @return 返回 webName。
     */
    public String getWebName()
    {
        return webName;
    }
    /**
     * @return 返回 webURL。
     */
    public String getWebURL()
    {
        return webURL;
    }
    /**
     * @param appID 要设置的 appID。
     */
    public void setAppID(int appID)
    {
        this.appID = appID;
    }
    /**
     * @param icon 要设置的 icon。
     */
    public void setIcon(String icon)
    {
        this.icon = icon;
    }
    /**
     * @param webName 要设置的 webName。
     */
    public void setWebName(String webName)
    {
        this.webName = webName;
    }
    /**
     * @param webURL 要设置的 webURL。
     */
    public void setWebURL(String webURL)
    {
        this.webURL = webURL;
    }
    
    public boolean equals(Object obj)
    {
        if(obj == null)
            return false;
        if(!(obj instanceof AppWebAddress))
            return false;
        else
        {
            AppWebAddress appWeb = (AppWebAddress)obj;
            if(appWeb.getAppID() == this.appID && appWeb.getWebName().equals(this.webName))
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
			sb.append(appID).append(':').append(webName);
			this.hashCode = sb.toString().hashCode();
		}
		return this.hashCode;
	}
}