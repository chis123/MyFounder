package com.founder.e5.app;

import java.io.Serializable;

/**
 * @version 1.0
 * @created 08-����-2005 15:17:56
 */
public class AppPermission implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2191653102267752548L;
	private int hashCode = Integer.MIN_VALUE;
	/**
	 * ��ϵͳID
	 */
	private int appID;
	/**
	 * ��Դ������
	 */
	private String resourceType;
	/**
	 * Ȩ���趨ҳ���URL
	 */
	private String commonURL;
	/**
	 * ע��
	 */
	private String notes;

	
	public AppPermission(){

	}

    /**
     * @return ���� appID��
     */
    public int getAppID()
    {
        return appID;
    }
    /**
     * @return ���� commonURL��
     */
    public String getCommonURL()
    {
        return commonURL;
    }
    /**
     * @return ���� notes��
     */
    public String getNotes()
    {
        return notes;
    }
    /**
     * @return ���� resourceType��
     */
    public String getResourceType()
    {
        return resourceType;
    }
    /**
     * @param appID Ҫ���õ� appID��
     */
    public void setAppID(int appID)
    {
        this.appID = appID;
    }
    /**
     * @param commonURL Ҫ���õ� commonURL��
     */
    public void setCommonURL(String commonURL)
    {
        this.commonURL = commonURL;
    }
    /**
     * @param notes Ҫ���õ� notes��
     */
    public void setNotes(String notes)
    {
        this.notes = notes;
    }
    /**
     * @param resourceType Ҫ���õ� resourceType��
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