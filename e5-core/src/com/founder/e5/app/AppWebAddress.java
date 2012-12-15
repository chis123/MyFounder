package com.founder.e5.app;

import java.io.Serializable;

/**
 * @version 1.0
 * @updated 08-����-2005 15:18:14
 */
public class AppWebAddress implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int hashCode = Integer.MIN_VALUE;
	/**
	 * ��ϵͳID
	 */
	private int appID;
	/**
	 * ��ϵͳ����ҳ����
	 */
	private String webName;
	/**
	 * ��ϵͳ����ҳ��URL
	 */
	private String webURL;
	/**
	 * ��ϵͳ����ҳ���ͼ��
	 */
	private String icon;

	public AppWebAddress(){

	}


    /**
     * @return ���� appID��
     */
    public int getAppID()
    {
        return appID;
    }
    /**
     * @return ���� icon��
     */
    public String getIcon()
    {
        return icon;
    }
    /**
     * @return ���� webName��
     */
    public String getWebName()
    {
        return webName;
    }
    /**
     * @return ���� webURL��
     */
    public String getWebURL()
    {
        return webURL;
    }
    /**
     * @param appID Ҫ���õ� appID��
     */
    public void setAppID(int appID)
    {
        this.appID = appID;
    }
    /**
     * @param icon Ҫ���õ� icon��
     */
    public void setIcon(String icon)
    {
        this.icon = icon;
    }
    /**
     * @param webName Ҫ���õ� webName��
     */
    public void setWebName(String webName)
    {
        this.webName = webName;
    }
    /**
     * @param webURL Ҫ���õ� webURL��
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