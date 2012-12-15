package com.founder.e5.sys;

import java.io.Serializable;



/**
 * @version 1.0
 * @created 08-七月-2005 15:06:07
 */
public class SysConfig implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2093489902708711513L;
	private int hashCode = Integer.MIN_VALUE;
	/**
	 * 系统配置ID
	 */
	private int sysConfigID;
	/**
	 * 子系统ID
	 */
	private int appID;
	/**
	 * 项目名
	 */
	private String project;
	/**
	 * 配置名
	 */
	private String item;
	/**
	 * 配置值
	 */
	private String value;
	/**
	 * 注释
	 */
	private String note;

	public SysConfig(){

	}

	

    /**
     * @return 返回 appID。
     */
    public int getAppID()
    {
        return appID;
    }
    /**
     * @return 返回 item。
     */
    public String getItem()
    {
        return item;
    }
    /**
     * @return 返回 note。
     */
    public String getNote()
    {
        return note;
    }
    /**
     * @return 返回 project。
     */
    public String getProject()
    {
        return project;
    }
    /**
     * @return 返回 sysConfigID。
     */
    public int getSysConfigID()
    {
        return sysConfigID;
    }
    /**
     * @return 返回 value。
     */
    public String getValue()
    {
        return value;
    }
    /**
     * @param appID 要设置的 appID。
     */
    public void setAppID(int appID)
    {
        this.appID = appID;
    }
    /**
     * @param item 要设置的 item。
     */
    public void setItem(String item)
    {
        this.item = item;
    }
    /**
     * @param note 要设置的 note。
     */
    public void setNote(String note)
    {
        this.note = note;
    }
    /**
     * @param project 要设置的 project。
     */
    public void setProject(String project)
    {
        this.project = project;
    }
    /**
     * @param sysConfigID 要设置的 sysConfigID。
     */
    public void setSysConfigID(int sysConfigID)
    {
        this.sysConfigID = sysConfigID;
    }
    /**
     * @param value 要设置的 value。
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