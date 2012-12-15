package com.founder.e5.permission;

/**
 * 分类权限实体类
 * @created 14-7-2005 16:23:46
 * @author Gong Lijie
 * @version 1.0
 */
public class CatPermission {

	private int roleID;
	private int catType;
	private int catID;
	private int permission;

	/**
	 * @param roleID
	 * @param catType
	 * @param catID
	 * @param permission
	 */
	public CatPermission(int roleID, int catType, int catID, int permission)
	{
		super();
		this.roleID = roleID;
		this.catType = catType;
		this.catID = catID;
		this.permission = permission;
	}
	/**
	 * @return Returns the catID.
	 */
	public int getCatID()
	{
		return catID;
	}
	/**
	 * @param catID The catID to set.
	 */
	public void setCatID(int catID)
	{
		this.catID = catID;
	}
	/**
	 * @return Returns the catType.
	 */
	public int getCatType()
	{
		return catType;
	}
	/**
	 * @param catType The catType to set.
	 */
	public void setCatType(int catType)
	{
		this.catType = catType;
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
}