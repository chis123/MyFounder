package com.founder.e5.sys;

import java.io.Serializable;



/**
 * @version 1.0
 * @created 08-����-2005 15:06:07
 */
public class SysConfig implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2093489902708711513L;
	private int hashCode = Integer.MIN_VALUE;
	/**
	 * ϵͳ����ID
	 */
	private int sysConfigID;
	/**
	 * ��ϵͳID
	 */
	private int appID;
	/**
	 * ��Ŀ��
	 */
	private String project;
	/**
	 * ������
	 */
	private String item;
	/**
	 * ����ֵ
	 */
	private String value;
	/**
	 * ע��
	 */
	private String note;

	public SysConfig(){

	}

	

    /**
     * @return ���� appID��
     */
    public int getAppID()
    {
        return appID;
    }
    /**
     * @return ���� item��
     */
    public String getItem()
    {
        return item;
    }
    /**
     * @return ���� note��
     */
    public String getNote()
    {
        return note;
    }
    /**
     * @return ���� project��
     */
    public String getProject()
    {
        return project;
    }
    /**
     * @return ���� sysConfigID��
     */
    public int getSysConfigID()
    {
        return sysConfigID;
    }
    /**
     * @return ���� value��
     */
    public String getValue()
    {
        return value;
    }
    /**
     * @param appID Ҫ���õ� appID��
     */
    public void setAppID(int appID)
    {
        this.appID = appID;
    }
    /**
     * @param item Ҫ���õ� item��
     */
    public void setItem(String item)
    {
        this.item = item;
    }
    /**
     * @param note Ҫ���õ� note��
     */
    public void setNote(String note)
    {
        this.note = note;
    }
    /**
     * @param project Ҫ���õ� project��
     */
    public void setProject(String project)
    {
        this.project = project;
    }
    /**
     * @param sysConfigID Ҫ���õ� sysConfigID��
     */
    public void setSysConfigID(int sysConfigID)
    {
        this.sysConfigID = sysConfigID;
    }
    /**
     * @param value Ҫ���õ� value��
     */
    public void setValue(String value)
    {
        this.value = value;
    }
    
    public boolean equals(Object obj)
    {
        if(obj == null) return false;
        if(! (obj instanceof SysConfig))
            return false;
        else
        {
            SysConfig sysConfig = (SysConfig)obj;
            if(sysConfig.getItem().equals(this.item) && sysConfig.getProject() == this.project && sysConfig.getAppID() == this.appID)
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
			sb.append(appID).append(':').append(project).append(':').append(item);
			this.hashCode = sb.toString().hashCode();
		}
		return this.hashCode;
	}
    
}