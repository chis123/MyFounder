package com.founder.e5.permission;

/**
 * 流程权限实体类
 * @created 14-7-2005 16:24:14
 * @author Gong Lijie
 * @version 1.0
 */
public class FlowPermission {

	/**
	 * 可能是节点ID，也可能是角色ID
	 */
	private int roleID;
	private int docTypeID;
	private int flowID;
	private int flowNodeID;
	private int permission;

	/**
	 * @param roleID
	 * @param docTypeID
	 * @param flowID
	 * @param flowNodeID
	 * @param permission
	 */
	public FlowPermission(int roleID, int docTypeID, int flowID,
			int flowNodeID, int permission)
	{
		super();
		this.roleID = roleID;
		this.docTypeID = docTypeID;
		this.flowID = flowID;
		this.flowNodeID = flowNodeID;
		this.permission = permission;
	}
	
	/**
	 * @return Returns the docTypeID.
	 */
	public int getDocTypeID()
	{
		return docTypeID;
	}
	/**
	 * @param docTypeID The docTypeID to set.
	 */
	public void setDocTypeID(int docTypeID)
	{
		this.docTypeID = docTypeID;
	}
	/**
	 * @return Returns the flowID.
	 */
	public int getFlowID()
	{
		return flowID;
	}
	/**
	 * @param flowID The flowID to set.
	 */
	public void setFlowID(int flowID)
	{
		this.flowID = flowID;
	}
	/**
	 * @return Returns the flowNodeID.
	 */
	public int getFlowNodeID()
	{
		return flowNodeID;
	}
	/**
	 * @param flowNodeID The flowNodeID to set.
	 */
	public void setFlowNodeID(int flowNodeID)
	{
		this.flowNodeID = flowNodeID;
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