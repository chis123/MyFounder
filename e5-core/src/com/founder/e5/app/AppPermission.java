package com.founder.e5.app;

import java.io.Serializable;

/**
 * @version 1.0
 * @created 08-七月-2005 15:17:56
 */
public class AppPermission implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2191653102267752548L;
	private int hashCode = Integer.MIN_VALUE;
	/**
	 * 子系统ID
	 */
	private int appID;
	/**
	 * 资源类型名
	 */
	private String resourceType;
	/**
	 * 权限设定页面的URL
	 */
	private String commonURL;
	/**
	 * 注释
	 */
	private String notes;

	
	public AppPermission(){

	}

    /**
     * @return 返回 appID。
     */
    public int getAppID()
    {
        return appID;
    }
    /**
     * @return 返回 commonURL。
     */
    public String getCommonURL()
    {
        return commonURL;
    }
    /**
     * @return 返回 notes。
     */
    public String getNotes()
    {
        return notes;
    }
    /**
     * @return 返回 resourceType。
     */
    public String getResourceType()
    {
        return resourceType;
    }
    /**
     * @param appID 要设置的 appID。
     */
    public void setAppID(int appID)
    {
        this.appID = appID;
    }
    /**
     * @param commonURL 要设置的 commonURL。
     */
    public void setCommonURL(String commonURL)
    {
        this.commonURL = commonURL;
    }
    /**
     * @param notes 要设置的 notes。
     */
    public void setNotes(String notes)
    {
        this.notes = notes;
    }
    /**
     * @param resourceType 要设置的 resourceType。
     */
    public void setResourceType(String resourceType)
    {
        this.resourceType = resourceType;
    }
    
    public boolean equals(Object obj)
    {
        if(obj == null)
            return false;
        if(!(obj instanceof AppPermission))
            return false;
        else
        {
            AppPermission appPerm = (AppPermission)obj;
            if(appPerm.getAppID() == this.appID && appPerm.getResourceType().equals(this.resourceType))
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
			sb.append(appID).append(':').append(resourceType);
			this.hashCode = sb.toString().hashCode();
		}
		return this.hashCode;
	}
}