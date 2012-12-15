package com.founder.e5.permission;

import java.io.Serializable;

/**
 * 文件夹视图权限实体类
 * @created 14-7-2005 16:23:09
 * @author Gong Lijie
 * @version 1.0
 */
public class FVPermission implements Serializable {
	/**文件夹权限－读*/
	public static final short PERMISSION_READ = 1;	
	/**文件夹权限－传递*/
	public static final short PERMISSION_TRANSFER = 2;
	/**文件夹权限－处理*/
	public static final short PERMISSION_PROCESS = 4;
	
	private int roleID;
	private int fvID;
	private int permission;
	
	private static final long serialVersionUID = -5229781235393882549L;
	public String toString(){
		return (new StringBuffer()
				.append("[roleID:").append(roleID)
				.append(",fvID:").append(fvID)
				.append(",permission:").append(permission)
				.append("]")
				).toString();
	}
	private int hashCode = Integer.MIN_VALUE;

	public FVPermission(){
	}
	public FVPermission(int _roleID, int _fvID){
		roleID = _roleID;
		fvID = _fvID;
	}

	public FVPermission(int _roleID, int _fvID, int _p){
		roleID = _roleID;
		fvID = _fvID;
		permission = _p;
	}

	/**
	 * @return Returns the fvID.
	 */
	public int getFvID()
	{
		return fvID;
	}
	/**
	 * @param fvID The fvID to set.
	 */
	public void setFvID(int fvID)
	{
		this.fvID = fvID;
	}
	/**
	 * @return Returns the permission.
	 */
	public int getPermission()
	{
		return permission;
	}
	/**
	 * @param permission The permission to set.
	 */
	public void setPermission(int permission)
	{
		this.permission = permission;
	}
	/**
	 * @return Returns the roleID.
	 */
	public int getRoleID()
	{
		return roleID;
	}
	/**
	 * @param roleID The roleID to set.
	 */
	public void setRoleID(int roleID)
	{
		this.roleID = roleID;
	}
	
	public boolean equals (Object obj) 
	{
		if (null == obj) return false;
		
		if (!(obj instanceof FVPermission)) 
			return false;
		else 
		{
			FVPermission mObj = (FVPermission) obj;
			if ((getRoleID() != mObj.getRoleID())
					|| (getFvID() != mObj.getFvID()))
				return false;
			else
				return true;
		}
	}

	public int hashCode() 
	{
		if (Integer.MIN_VALUE == this.hashCode) 
		{
			StringBuffer sb = new StringBuffer();
			sb.append(roleID).append(":").append(fvID);

			hashCode = sb.toString().hashCode();
		}
		return hashCode;
	}
}