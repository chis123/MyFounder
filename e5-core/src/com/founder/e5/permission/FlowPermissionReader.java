package com.founder.e5.permission;

import com.founder.e5.context.E5Exception;
import com.founder.e5.flow.Proc;

/**
 * 流程权限的读取器
 * @created 14-7-2005 16:24:08
 * @author Gong Lijie
 * @version 1.0
 */
public interface FlowPermissionReader {

	/**
	 * 取一个角色的某流程节点权限。
	 * 常用于工作平台中。
	 * 
	 * @param roleID    角色ID
	 * @param flowNodeID    节点ID
	 * @exception E5Exception E5Exception
	 */
	public int get(int roleID, int flowID, int flowNodeID)
	throws E5Exception;

	/**
	 * 取一个角色对某文档类型的非流程操作权限
	 * @param roleID    角色ID
	 * @param docTypeID    文档类型ID
	 * @exception E5Exception E5Exception
	 */
	public int getUnflowPermission(int roleID, int docTypeID)
	throws E5Exception;

	/**
	 * 取一个角色对所有非流程操作的权限
	 * 
	 * @param roleID    角色ID
	 * @exception E5Exception E5Exception
	 */
	public FlowPermission[] getUnflowPermissionsByRole(int roleID)
	throws E5Exception;

	/**
	 * 取一个角色有创建权限的所有流程
	 * <br/>即，该角色在创建文档时，可以选择该文档进入的流程
	 * @param roleID    角色ID
	 * <br/>如：取角色5有新建权限的所有流程 getFlowsNewable(5)
	 * @exception E5Exception E5Exception
	 */
	public int[] getFlowsNewable(int roleID)
	throws E5Exception;

	/**
	 * 取有某流程操作权限的所有角色
	 * @param flowID 流程ID
	 * @param flowNodeID    流程节点ID
	 * @param permissionArray    权限标记 
	 * <br/>权限标记是一个数组，只要拥有该数组中列出的其中一个权限标记，即认为匹配成功，可以返回该角色ID。
	 * <br/>当这个参数为空时，任何权限都可。
	 * <br/>如：取有"提交"权限的所有角色 getRolesOfFlow(5,{1})
	 * @exception E5Exception E5Exception
	 */
	public int[] getRolesOfFlowNode(int flowID, int flowNodeID, int[] maskArray)
	throws E5Exception;

	/**
	 * 取一个角色对一个流程的所有权限
	 * 
	 * @param roleID    角色ID
	 * @param flowID    流程ID
	 * @exception E5Exception E5Exception
	 */
	public FlowPermission[] getPermissionsByRole(int roleID, int flowID)
	throws E5Exception;

	/**
	 * 取一个角色的所有流程权限
	 * 
	 * @param roleID    角色ID
	 * @exception E5Exception E5Exception
	 */
	public FlowPermission[] getPermissionsByRole(int roleID)
	throws E5Exception;
	
	/**
	 * 判断角色对某流程操作是否有权限
	 * 
	 * @param roleID    角色ID
	 * @param flowID	流程ID
	 * @param flowNodeID    流程节点ID
	 * @param procType    操作类型
	 * @param procID    操作ID 
	 * @return boolean
	 * @throws E5Exception
	 */
	public boolean hasPermission(int roleID, int flowID, int flowNodeID, 
			int procType, int procID)
	throws E5Exception;

	/**
	 * 判断角色对某流程操作是否有权限
	 * 
	 * @param roleID    角色ID
	 * @param flowID	流程ID
	 * @param flowNodeID    流程节点ID
	 * @param procName    操作名 
	 * @return boolean
	 * @throws E5Exception
	 */
	public boolean hasPermission(int roleID, int flowID, int flowNodeID, String procName)
	throws E5Exception;
	
	/**
	 * 判断角色对某流程操作是否有权限
	 * @param roleID 角色ID
	 * @param proc   流程操作对象
	 * @return boolean
	 * @throws E5Exception
	 */
	public boolean hasPermission(int roleID, Proc proc)
	throws E5Exception;
	/**
	 * 判断角色对某非流程操作是否有权限
	 * 
	 * @param roleID    角色ID
	 * @param procID    操作ID 
	 * @return boolean
	 * @throws E5Exception
	 */
	public boolean hasUnflowPermission(int roleID, int procID)
	throws E5Exception;

	/**
	 * 判断角色对某非流程操作是否有权限
	 * 
	 * @param roleID    角色ID
	 * @param docTypeID    文档类型ID
	 * @param procName    操作名  
	 * @return boolean
	 * @throws E5Exception
	 */
	public boolean hasUnflowPermission(int roleID, int docTypeID, String procName)
	throws E5Exception;
	
	/**
	 * 判断角色对某非流程操作是否有权限
	 * @param roleID 角色ID
	 * @param proc   非流程操作对象
	 * @return boolean
	 * @throws E5Exception
	 */
	public boolean hasUnflowPermission(int roleID, Proc proc)
	throws E5Exception;
}