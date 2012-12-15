package com.founder.e5.sys.org;

import java.io.Serializable;

/**
 * @version 1.0
 * @updated 11-七月-2005 10:17:27
 */
public class Role implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4556834727405488994L;
	private int hashCode = Integer.MIN_VALUE;
	/**
	 * 组织ID
	 */
	private int orgID;
	/**
	 * 角色ID
	 */
	private int roleID;
	/**
	 * 角色名称
	 */
	private String roleName;
	/**
	 * 排序ID
	 */
	private int orderID;

	public Role(){

	}

	

    /**
     * @return 返回 nodeID。
     */
    public int getOrgID()
    {
        return orgID;
    }
    /**
     * @return 返回 orderID。
     */
    public int getOrderID()
    {
        return orderID;
    }
    /**
     * @return 返回 roleID。
     */
    public int getRoleID()
    {
        return roleID;
    }
    /**
     * @return 返回 roleName。
     */
    public String getRoleName()
    {
        return roleName;
    }
    /**
     * @param nodeID 要设置的 nodeID。
     */
    public void setOrgID(int orgID)
    {
        this.orgID = orgID;
    }
    /**
     * @param orderID 要设置的 orderID。
     */
    public void setOrderID(int orderID)
    {
        this.orderID = orderID;
    }
    /**
     * @param roleID 要设置的 roleID。
     */
    public void setRoleID(int roleID)
    {
        this.roleID = roleID;
    }
    /**
     * @param roleName 要设置的 roleName。
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