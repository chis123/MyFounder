package com.founder.e5.sys.org;

import java.io.Serializable;

/**
 * @version 1.0
 * @updated 11-����-2005 10:17:27
 */
public class Role implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4556834727405488994L;
	private int hashCode = Integer.MIN_VALUE;
	/**
	 * ��֯ID
	 */
	private int orgID;
	/**
	 * ��ɫID
	 */
	private int roleID;
	/**
	 * ��ɫ����
	 */
	private String roleName;
	/**
	 * ����ID
	 */
	private int orderID;

	public Role(){

	}

	

    /**
     * @return ���� nodeID��
     */
    public int getOrgID()
    {
        return orgID;
    }
    /**
     * @return ���� orderID��
     */
    public int getOrderID()
    {
        return orderID;
    }
    /**
     * @return ���� roleID��
     */
    public int getRoleID()
    {
        return roleID;
    }
    /**
     * @return ���� roleName��
     */
    public String getRoleName()
    {
        return roleName;
    }
    /**
     * @param nodeID Ҫ���õ� nodeID��
     */
    public void setOrgID(int orgID)
    {
        this.orgID = orgID;
    }
    /**
     * @param orderID Ҫ���õ� orderID��
     */
    public void setOrderID(int orderID)
    {
        this.orderID = orderID;
    }
    /**
     * @param roleID Ҫ���õ� roleID��
     */
    public void setRoleID(int roleID)
    {
        this.roleID = roleID;
    }
    /**
     * @param roleName Ҫ���õ� roleName��
     */
    public void setRoleName(String roleName)
    {
        this.roleName = roleName;
    }
    
    public boolean equals(Object obj)
    {
        if(obj == null) return false;
        if(! (obj instanceof Role))
            return false;
        else
        {
            Role role = (Role)obj;
            if(role.getRoleName().equals(this.roleName) && role.getOrgID() == this.orgID)
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
			sb.append(orgID).append(':').append(roleName);
			this.hashCode = sb.toString().hashCode();
		}
		return this.hashCode;
	}
}