package com.founder.e5.sys.org;

import java.io.Serializable;

/**
 * @version 1.0
 * @updated 11-����-2005 10:17:35
 */
public class Org implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * ��֯����ID
	 */
	private int orgID;
	/**
	 * ��֯����
	 */
	private String name;
	/**
	 * ��֯����ID
	 */
	private int type;
	/**
	 * ����֯��ID
	 */
	private int parentID;
	/**
	 * ��֯����
	 */
	private String code;
	/**
	 * ���õ�����1
	 */
	private String property1;
	/**
	 * ���õ�����2
	 */
	private String property2;
	/**
	 * ���õ�����3
	 */
	private String property3;
	/**
	 * ���õ�����4
	 */
	private String property4;
	/**
	 * ���õ�����5
	 */
	private String property5;
	/**
	 * ���õ�����6
	 */
	private String property6;
	/**
	 * ���õ�����7
	 */
	private String property7;
	/**
	 * ����ID
	 */
	private int orderID;

	
	public Org(){

	}

	
    /**
     * @return ���� nodeCode��
     */
    public String getCode()
    {
        return code;
    }
    /**
     * @return ���� nodeID��
     */
    public int getOrgID()
    {
        return orgID;
    }
    /**
     * @return ���� nodeName��
     */
    public String getName()
    {
        return name;
    }
    /**
     * @return ���� nodeType��
     */
    public int getType()
    {
        return type;
    }
    /**
     * @return ���� orderID��
     */
    public int getOrderID()
    {
        return orderID;
    }
    /**
     * @return ���� parentID��
     */
    public int getParentID()
    {
        return parentID;
    }
    /**
     * @return ���� property1��
     */
    public String getProperty1()
    {
        return property1;
    }
    /**
     * @return ���� property2��
     */
    public String getProperty2()
    {
        return property2;
    }
    /**
     * @return ���� property3��
     */
    public String getProperty3()
    {
        return property3;
    }
    /**
     * @return ���� property4��
     */
    public String getProperty4()
    {
        return property4;
    }
    /**
     * @return ���� property5��
     */
    public String getProperty5()
    {
        return property5;
    }
    /**
     * @return ���� property6��
     */
    public String getProperty6()
    {
        return property6;
    }
    /**
     * @return ���� property7��
     */
    public String getProperty7()
    {
        return property7;
    }
    /**
     * @param nodeCode Ҫ���õ� nodeCode��
     */
    public void setCode(String code)
    {
        this.code = code;
    }
    /**
     * @param nodeID Ҫ���õ� nodeID��
     */
    public void setOrgID(int orgID)
    {
        this.orgID = orgID;
    }
    /**
     * @param nodeName Ҫ���õ� nodeName��
     */
    public void setName(String name)
    {
        this.name = name;
    }
    /**
     * @param nodeType Ҫ���õ� nodeType��
     */
    public void setType(int type)
    {
        this.type = type;
    }
    /**
     * @param orderID Ҫ���õ� orderID��
     */
    public void setOrderID(int orderID)
    {
        this.orderID = orderID;
    }
    /**
     * @param parentID Ҫ���õ� parentID��
     */
    public void setParentID(int parentID)
    {
        this.parentID = parentID;
    }
    /**
     * @param property1 Ҫ���õ� property1��
     */
    public void setProperty1(String property1)
    {
        this.property1 = property1;
    }
    /**
     * @param property2 Ҫ���õ� property2��
     */
    public void setProperty2(String property2)
    {
        this.property2 = property2;
    }
    /**
     * @param property3 Ҫ���õ� property3��
     */
    public void setProperty3(String property3)
    {
        this.property3 = property3;
    }
    /**
     * @param property4 Ҫ���õ� property4��
     */
    public void setProperty4(String property4)
    {
        this.property4 = property4;
    }
    /**
     * @param property5 Ҫ���õ� property5��
     */
    public void setProperty5(String property5)
    {
        this.property5 = property5;
    }
    /**
     * @param property6 Ҫ���õ� property6��
     */
    public void setProperty6(String property6)
    {
        this.property6 = property6;
    }
    /**
     * @param property7 Ҫ���õ� property7��
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