package com.founder.e5.app;

import java.io.Serializable;




/**
 * @created 21-����-2005 14:41:32
 * @version 1.0
 */
public class App implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int hashCode = Integer.MIN_VALUE;
	/**
	 * ��ϵͳ��
	 */
	private String name;
	/**
	 * ��ϵͳ�汾������
	 */
	private String absVersion;
	/**
	 * ��ϵͳ�汾��
	 */
	private String version;
	/**
	 * ��ϵͳ�ṩ��
	 */
	private String provider;
	/**
	 * ��ϵͳID
	 */
	private int appID;
	/**
	 * ���ڱ�����ϵͳװ�صĳ�ʼ������Ϣ
	 */
	private String initInfo;
	
	
	public App(){

	}

	
    /**
     * @return ���� appAbsVersion��
     */
    public String getAbsVersion()
    {
        return absVersion;
    }
    /**
     * @return ���� appID��
     */
    public int getAppID()
    {
        return appID;
    }
    /**
     * @return ���� appName��
     */
    public String getName()
    {
        return name;
    }
    /**
     * @return ���� appProvider��
     */
    public String getProvider()
    {
        return provider;
    }
    /**
     * @return ���� appVersion��
     */
    public String getVersion()
    {
        return version;
    }
    /**
     * @return ���� initInfo��
     */
    public String getInitInfo()
    {
        return initInfo;
    }
    /**
     * @param appAbsVersion Ҫ���õ� appAbsVersion��
     */
    public void setAbsVersion(String absVersion)
    {
        this.absVersion = absVersion;
    }
    /**
     * @param appID Ҫ���õ� appID��
     */
    public void setAppID(int appID)
    {
        this.appID = appID;
    }
    /**
     * @param appName Ҫ���õ� appName��
     */
    public void setName(String name)
    {
        this.name = name;
    }
    /**
     * @param appProvider Ҫ���õ� appProvider��
     */
    public void setProvider(String provider)
    {
        this.provider = provider;
    }
    /**
     * @param appVersion Ҫ���õ� appVersion��
     */
    public void setVersion(String version)
    {
        this.version = version;
    }
    /**
     * @param initInfo Ҫ���õ� initInfo��
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