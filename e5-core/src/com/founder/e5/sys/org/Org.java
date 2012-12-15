package com.founder.e5.sys.org;

import java.io.Serializable;

/**
 * @version 1.0
 * @updated 11-七月-2005 10:17:35
 */
public class Org implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 组织对象ID
	 */
	private int orgID;
	/**
	 * 组织名称
	 */
	private String name;
	/**
	 * 组织类型ID
	 */
	private int type;
	/**
	 * 父组织的ID
	 */
	private int parentID;
	/**
	 * 组织编码
	 */
	private String code;
	/**
	 * 备用的属性1
	 */
	private String property1;
	/**
	 * 备用的属性2
	 */
	private String property2;
	/**
	 * 备用的属性3
	 */
	private String property3;
	/**
	 * 备用的属性4
	 */
	private String property4;
	/**
	 * 备用的属性5
	 */
	private String property5;
	/**
	 * 备用的属性6
	 */
	private String property6;
	/**
	 * 备用的属性7
	 */
	private String property7;
	/**
	 * 排序ID
	 */
	private int orderID;

	
	public Org(){

	}

	
    /**
     * @return 返回 nodeCode。
     */
    public String getCode()
    {
        return code;
    }
    /**
     * @return 返回 nodeID。
     */
    public int getOrgID()
    {
        return orgID;
    }
    /**
     * @return 返回 nodeName。
     */
    public String getName()
    {
        return name;
    }
    /**
     * @return 返回 nodeType。
     */
    public int getType()
    {
        return type;
    }
    /**
     * @return 返回 orderID。
     */
    public int getOrderID()
    {
        return orderID;
    }
    /**
     * @return 返回 parentID。
     */
    public int getParentID()
    {
        return parentID;
    }
    /**
     * @return 返回 property1。
     */
    public String getProperty1()
    {
        return property1;
    }
    /**
     * @return 返回 property2。
     */
    public String getProperty2()
    {
        return property2;
    }
    /**
     * @return 返回 property3。
     */
    public String getProperty3()
    {
        return property3;
    }
    /**
     * @return 返回 property4。
     */
    public String getProperty4()
    {
        return property4;
    }
    /**
     * @return 返回 property5。
     */
    public String getProperty5()
    {
        return property5;
    }
    /**
     * @return 返回 property6。
     */
    public String getProperty6()
    {
        return property6;
    }
    /**
     * @return 返回 property7。
     */
    public String getProperty7()
    {
        return property7;
    }
    /**
     * @param nodeCode 要设置的 nodeCode。
     */
    public void setCode(String code)
    {
        this.code = code;
    }
    /**
     * @param nodeID 要设置的 nodeID。
     */
    public void setOrgID(int orgID)
    {
        this.orgID = orgID;
    }
    /**
     * @param nodeName 要设置的 nodeName。
     */
    public void setName(String name)
    {
        this.name = name;
    }
    /**
     * @param nodeType 要设置的 nodeType。
     */
    public void setType(int type)
    {
        this.type = type;
    }
    /**
     * @param orderID 要设置的 orderID。
     */
    public void setOrderID(int orderID)
    {
        this.orderID = orderID;
    }
    /**
     * @param parentID 要设置的 parentID。
     */
    public void setParentID(int parentID)
    {
        this.parentID = parentID;
    }
    /**
     * @param property1 要设置的 property1。
     */
    public void setProperty1(String property1)
    {
        this.property1 = property1;
    }
    /**
     * @param property2 要设置的 property2。
     */
    public void setProperty2(String property2)
    {
        this.property2 = property2;
    }
    /**
     * @param property3 要设置的 property3。
     */
    public void setProperty3(String property3)
    {
        this.property3 = property3;
    }
    /**
     * @param property4 要设置的 property4。
     */
    public void setProperty4(String property4)
    {
        this.property4 = property4;
    }
    /**
     * @param property5 要设置的 property5。
     */
    public void setProperty5(String property5)
    {
        this.property5 = property5;
    }
    /**
     * @param property6 要设置的 property6。
     */
    public void setProperty6(String property6)
    {
        this.property6 = property6;
    }
    /**
     * @param property7 要设置的 property7。
     */
    public void setProperty7(String property7)
    {
        this.property7 = property7;
    }
    
    public boolean equals (Object obj) 
    {
        if(obj == null) return false;
        if(! (obj instanceof Org))
            return false;
        else
        {
            Org org = (Org)obj;
            if(org.getName() != null && org.getName().equals(this.name) && org.getParentID() == this.parentID)
                return true;
        }
        return false;
    }
}