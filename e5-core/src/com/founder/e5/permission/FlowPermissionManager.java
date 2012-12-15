package com.founder.e5.permission;
import com.founder.e5.context.E5Exception;
import com.founder.e5.flow.Proc;

/**
 * 系统管理中流程权限设置和读取的管理接口 取角色权限时不带继承。
 * @created 14-7-2005 16:24:03
 * @author Gong Lijie
 * @version 1.0
 */
public interface FlowPermissionManager extends FlowPermissionReader {

	/**
	 * 保存流程权限，包括流程节点权限和非流程操作权限
	 * 
	 * @param permission    permission
	 * @exception E5Exception
	 */
	public void save(FlowPermission permission)
	throws E5Exception;

	/**
	 * 保存流程权限，包括流程节点权限和非流程操作权限
	 * 
	 * @param permissionArray    permissionArray
	 * @exception E5Exception
	 */
	public void save(FlowPermission[] permissionArray)
	throws E5Exception;

	/**
	 * 取对某个文档类型下所有非流程操作的所有权限
	 * @param docTypeID
	 * @return
	 * @throws E5Exception
	 */
	public FlowPermission[] getUnflowPermissionsByDocType(int docTypeID)
	throws E5Exception;

	/**
	 * 取对某流程的所有权限，包括对其所有流程节点的权限
	 * @param flowID
	 * @return
	 * @throws E5Exception
	 */
	public FlowPermission[] getPermissionsByFlow(int flowID)
	throws E5Exception;
	
	/**
	 * 取对某流程节点的所有权限
	 * @param flowID
	 * @param flowNodeID
	 * @return
	 * @throws E5Exception
	 */
	public FlowPermission[] getPermissionsByFlowNode(int flowID, int flowNodeID)
	throws E5Exception;

	/**
	 * 删除文档类型时，把非流程权限删除
	 * 
	 * @param docTypeID    文档类型ID
	 * @exception E5Exception
	 */
	public void deleteDocType(int docTypeID)
	throws E5Exception;

	/**
	 * 删除一个流程时，把所有流程操作权限删除
	 * 
	 * @param flowID     流程ID
	 * @exception E5Exception
	 */
	public void deleteFlow(int flowID)
	throws E5Exception;

	/**
	 * 删除一个流程节点时，把所有节点操作权限删除
	 * @param flowID 流程ID
	 * @param flowNodeID    流程节点ID
	 * @exception E5Exception
	 */
	public void deleteFlowNode(int flowID, int flowNodeID)
	throws E5Exception;

	/**
	 * 删除一个处理模块的时候，把对应的权限删除
	 * 
	 * @param proc 处理模块，可以是任何非流程操作、流程操作
	 * @exception E5Exception
	 */
	public void deleteProc(Proc proc)
	throws E5Exception;

	/**
	 * 该方法已废除
	 * 因为删除角色时所有权限都删除，包括流程权限
	 * 应该调用PermissionManager.deleteRole
	 * @deprecated
	 * @param roleID
	 * @throws E5Exception
	 */
	public void deleteRole(int roleID)
	throws E5Exception;
}