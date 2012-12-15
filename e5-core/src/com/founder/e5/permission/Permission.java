package com.founder.e5.permission;

import java.io.Serializable;

/**
 * 权限类
 * @created 14-7-2005 16:25:25
 * @author Gong Lijie
 * @version 1.0
 */
public class Permission implements Serializable 
//该类需作为主键传递，因此实现Serializable接口
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3084253081704126036L;
	/** 角色ID*/
	private int roleID;
	private String resourceType;
	private String resource;
	private int permission;

	public String toString(){
		return (new StringBuffer()
				.append("[roleID:").append(roleID)
				.append(",resourceType:").append(resourceType)
				.append(",resource:").append(resource)
				.append(",permission:").append(permission)
				.append("]")
				).toString();
	}
	/**
	 * 缺省构造方法
	 */
	public Permission(){}
	/**
	 * 构造方法
	 * 只提供主键值
	 * @param _roleID
	 * @param _resourceType
	 * @param _resource
	 */
	public Permission(int _roleID, String _resourceType, String _resource){
		setRoleID(_roleID);
		setResourceType(_resourceType);
		setResource(_resource);
	}
	/**
	 * 构造方法
	 * 提供全部属性
	 * @param _roleID
	 * @param _resourceType
	 * @param _resource
	 * @param _permission
	 */
	public Permission(int _roleID, String _resourceType, String _resource, int _permission){
		setRoleID(_roleID);
		setResourceType(_resourceType);
		setResource(_resource);
		setPermission(_permission);
	}
	private int hashCode = Integer.MIN_VALUE;
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
	 * @return Returns the resource.
	 */
	public String getResource()
	{
		return resource;
	}
	/**
	 * @param resource The resource to set.
	 */
	public void setResource(String resource)
	{
		this.resource = resource;
	}
	/**
	 * @return Returns the resourceType.
	 */
	public String getResourceType()
	{
		return resourceType;
	}
	/**
	 * @param resourceType The resourceType to set.
	 */
	public void setResourceType(String resourceType)
	{
		this.resourceType = resourceType;
	}
	/**
	 * @return Returns the roleid.
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

	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof Permission)) return false;
		else {
			Permission mObj = (Permission) obj;
			if (this.getRoleID() != mObj.getRoleID()) {
				return false;
			}
			if (null != this.getResourceType() && null != mObj.getResourceType()) {
				if (!this.getResourceType().equals(mObj.getResourceType())) {
					return false;
				}
			}
			else {
				return false;
			}
			if (null != this.getResource() && null != mObj.getResource()) {
				if (!this.getResource().equals(mObj.getResource())) {
					return false;
				}
			}
			else {
				return false;
			}
			return true;
		}
	}

	public int hashCode () {
		if (Integer.MIN_VALUE == this.hashCode) {
			StringBuffer sb = new StringBuffer();
			sb.append(new java.lang.Integer(this.getRoleID()).hashCode());
			sb.append(":");
			if (null != this.getResourceType()) {
				sb.append(this.getResourceType().hashCode());
				sb.append(":");
			}
			else {
				return super.hashCode();
			}
			if (null != this.getResource()) {
				sb.append(this.getResource().hashCode());
				sb.append(":");
			}
			else {
				return super.hashCode();
			}
			this.hashCode = sb.toString().hashCode();
		}
		return this.hashCode;
	}
}