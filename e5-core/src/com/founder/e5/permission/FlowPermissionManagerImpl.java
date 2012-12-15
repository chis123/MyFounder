package com.founder.e5.permission;
import java.util.ArrayList;
import java.util.List;

import com.founder.e5.commons.ArrayUtils;
import com.founder.e5.context.Context;
import com.founder.e5.context.E5Exception;
import com.founder.e5.flow.Flow;
import com.founder.e5.flow.FlowManager;
import com.founder.e5.flow.Proc;
import com.founder.e5.flow.ProcFlow;
import com.founder.e5.flow.ProcUnflow;

/**
 * @created 18-7-2005 10:51:16
 * @author Gong Lijie
 * @version 1.0
 */
class FlowPermissionManagerImpl implements FlowPermissionManager{

	FlowPermissionManagerImpl()
	{
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.permission.FlowPermissionReader#get(int, int, int)
	 */
	public int get(int roleID, int flowID, int flowNodeID) throws E5Exception{
		Permission p = PermissionHelper.getManager().get(roleID, "FLOW" + flowID, "FLOWNODE" + flowNodeID);
		if (p != null)
			return p.getPermission();
		else
			return 0;
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.permission.FlowPermissionManager#save(com.founder.e5.permission.FlowPermission)
	 */
	public void save(FlowPermission permission) throws E5Exception {
		Permission p = null;
		if (permission.getFlowID() == 0)
			p = new Permission(permission.getRoleID(), 
					"UNFLOW", 
					String.valueOf(permission.getDocTypeID()),
					permission.getPermission());
		else
			p = new Permission(permission.getRoleID(), 
				"FLOW" + permission.getFlowID(),
				"FLOWNODE" + permission.getFlowNodeID(),
				permission.getPermission());
		PermissionHelper.getManager().save(p);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.permission.FlowPermissionManager#save(com.founder.e5.permission.FlowPermission[])
	 */
	public void save(FlowPermission[] permissionArray)
	  throws E5Exception{
		Permission[] pArr = new Permission[permissionArray.length];
		for (int i = 0; i < permissionArray.length; i++)
		{
			if (permissionArray[i].getFlowID() == 0)
				pArr[i] = new Permission(permissionArray[i].getRoleID(), 
						"UNFLOW", 
						String.valueOf(permissionArray[i].getDocTypeID()),
						permissionArray[i].getPermission());
			else
				pArr[i] = new Permission(permissionArray[i].getRoleID(), 
					"FLOW" + permissionArray[i].getFlowID(),
					"FLOWNODE" + permissionArray[i].getFlowNodeID(),
					permissionArray[i].getPermission());
		}
		PermissionHelper.getManager().save(pArr);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.permission.FlowPermissionReader#getUnflowPermission(int, int)
	 */
	public int getUnflowPermission(int roleID, int docTypeID)
	  throws E5Exception{
		Permission p = PermissionHelper.getManager().get(roleID, "UNFLOW", String.valueOf(docTypeID));
		if (p != null)
			return p.getPermission();
		else
			return 0;
	}

	private FlowPermission[] getFPArrayByRole(Permission[] pArr, int roleID){
		if (pArr == null) return null;
		
		FlowPermission[] fpArr = new FlowPermission[pArr.length];
		 
		for (int i = 0; i < fpArr.length; i++)
		{
			fpArr[i] = new FlowPermission(roleID, 
					0,
					Integer.parseInt(pArr[i].getResourceType().substring(4)),
					Integer.parseInt(pArr[i].getResource().substring(8)),
					pArr[i].getPermission());
		}
		return fpArr;
	}

	private Permission[] getUnflowPermissions(int docTypeID)
	throws E5Exception
	{
		Permission[] pArr = PermissionHelper.getManager().getPermissions("UNFLOW", String.valueOf(docTypeID));
		return pArr;			
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.permission.FlowPermissionManager#deleteDocType(int)
	 */
	public void deleteDocType(int docTypeID)
		throws E5Exception
	{
		PermissionHelper.getManager().deleteResource("UNFLOW", String.valueOf(docTypeID));
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.permission.FlowPermissionManager#deleteFlow(int)
	 */
	public void deleteFlow(int flowID)
		throws E5Exception
	{
		PermissionHelper.getManager().deleteResourceType("FLOW" + flowID);		
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.permission.FlowPermissionManager#deleteFlowNode(int, int)
	 */
	public void deleteFlowNode(int flowID, int flowNodeID)
	throws E5Exception
	{
		PermissionHelper.getManager().deleteResource("FLOW" + flowID, "FLOWNODE" + flowNodeID);		
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.permission.FlowPermissionManager#deleteProc(com.founder.e5.flow.Proc)
	 */
	public void deleteProc(Proc proc)
	throws E5Exception
	{
		PermissionManager manager = PermissionHelper.getManager(); 
		if (proc.getProcType() == Proc.PROC_UNFLOW){
			Permission[] pArr = getUnflowPermissions(((ProcUnflow)proc).getDocTypeID());
			changePermission(pArr, manager, proc);
		}
		else
		{
			Permission[] pArr = manager.getPermissions("FLOW" + ((ProcFlow)proc).getFlowID(), 
					"FLOWNODE" + ((ProcFlow)proc).getFlowNodeID());
			changePermission(pArr, manager, proc);
		}
	}
	/**
	 * 流程权限的按位存储顺序是：
	 * [JUMP....][GO][DO][BACK][NEW]
	 * 也就是说，第0位表示是否有对该流程节点new的权限，
	 * 第1位表示BACK操作的权限
	 * 第2位表示DO操作的权限
	 * 第3位表示GO操作的权限
	 * 第4位开始表示JUMP操作的权限，按跳转操作的PROCID顺序，从小到大依次存储
	 * 
	 * 非流程操作的权限就是按PROCID顺序
	 * @param pArr
	 * @param manager
	 * @param proc
	 * @throws E5Exception
	 */
	private void changePermission(Permission[] pArr, PermissionManager manager, Proc proc)
	throws E5Exception
	{
		if (pArr == null) return;
		//取每个操作的权限位号
		int nOrder = 0;
		int procType = proc.getProcType();
		if (procType == Proc.PROC_UNFLOW)
			nOrder = getPermissionOrder(proc.getProcType(),
					((ProcUnflow)proc).getDocTypeID(),
					proc.getProcID()); 
		else
			nOrder = getPermissionOrder(proc.getProcType(),
					((ProcFlow)proc).getFlowNodeID(),
					((ProcFlow)proc).getProcID());
		//修改权限
		for (int i = 0; i < pArr.length; i++)
		{
			if (pArr[i].getPermission() == 0) continue;//应该不会有为0的权限

			int nBitDelete = PermissionHelper.getBitDelete(pArr[i].getPermission(), nOrder);
			pArr[i].setPermission(nBitDelete);
			manager.save(pArr[i]);
		}		
	}
	/* (non-Javadoc)
	 * @see com.founder.e5.permission.FlowPermissionReader#hasPermission(int, int, int, int, int)
	 */
	public boolean hasPermission(int roleID, int flowID, int flowNodeID, 
			int procType, int procID) 
	throws E5Exception
	{
		int p = get(roleID, flowID, flowNodeID);
		if (p == 0) return false;
		
		//取每个操作的权限位号
		int nOrder = getPermissionOrder(procType, flowNodeID, procID);
		if (PermissionHelper.getBit(p, nOrder) == 1)
			return true;
		else
			return false;
	}
	/* (non-Javadoc)
	 * @see com.founder.e5.permission.FlowPermissionReader#hasPermission(int, int, java.lang.String)
	 */
	public boolean hasPermission(int roleID, int flowID, int flowNodeID, String procName) 
	throws E5Exception
	{
		Proc proc = PermissionHelper.getProcManager().getProc(flowNodeID, procName);
		if (proc == null) return false;
		
		return hasPermission(roleID, flowID, flowNodeID, proc.getProcType(), 
				proc.getProcID());
	}
	
	/* (non-Javadoc)
	 * @see com.founder.e5.permission.FlowPermissionReader#hasPermission(int, Proc)
	 */
	public boolean hasPermission(int roleID, Proc proc)
	throws E5Exception
	{
		return hasPermission(roleID, ((ProcFlow)proc).getFlowID(), 
				((ProcFlow)proc).getFlowNodeID(), 
				proc.getProcType(), proc.getProcID());
	}
	/* (non-Javadoc)
	 * @see com.founder.e5.permission.FlowPermissionReader#hasUnflowPermission(int, int)
	 */
	public boolean hasUnflowPermission(int roleID, int procID) throws E5Exception
	{
		Proc proc = PermissionHelper.getProcManager().getUnflow(procID);
		if (proc == null) return false;
		return hasUnflowPermission(roleID, proc);
	}
	/* (non-Javadoc)
	 * @see com.founder.e5.permission.FlowPermissionReader#hasUnflowPermission(int, int, java.lang.String)
	 */
	public boolean hasUnflowPermission(int roleID, int docTypeID, String procName) 
	throws E5Exception
	{
		Proc proc = PermissionHelper.getProcManager().getUnflow(docTypeID, procName);
		if (proc == null) return false;
		return hasUnflowPermission(roleID, proc);
	}
	/* (non-Javadoc)
	 * @see com.founder.e5.permission.FlowPermissionReader#hasUnflowPermission(int, Proc)
	 */
	public boolean hasUnflowPermission(int roleID, Proc proc)
	throws E5Exception
	{
		ProcUnflow unflow = (ProcUnflow)proc;
		//非流程权限的权限记录比较特殊，缺省是有权限的，所以0表示有权限，1表示没有权限
		int p = getUnflowPermission(roleID, unflow.getDocTypeID());
		if (p == 0) return true;
		
		int order = getPermissionOrder(Proc.PROC_UNFLOW, unflow.getDocTypeID(), unflow.getProcID());

		if (PermissionHelper.getBit(p, order) == 0) 
			return true;
		else
			return false;
	}
	/* (non-Javadoc)
	 * @see com.founder.e5.permission.FlowPermissionManager#getUnflowPermissionsByDocType(int)
	 */
	public FlowPermission[] getUnflowPermissionsByDocType(int docTypeID) throws E5Exception
	{
		Permission[] pArr = getUnflowPermissions(docTypeID);
		if (pArr == null) return null;
		
		FlowPermission[] fpArr = new FlowPermission[pArr.length];
		 
		for (int i = 0; i < fpArr.length; i++)
		{
			fpArr[i] = new FlowPermission(pArr[i].getRoleID(), 
					docTypeID,
					0,
					0,
					pArr[i].getPermission());
		}
		return fpArr;
	}
	/* 
	 * (non-Javadoc)
	 * @see com.founder.e5.permission.FlowPermissionManager#getPermissionsByFlow(int)
	 */
	public FlowPermission[] getPermissionsByFlow(int flowID) throws E5Exception
	{
		Permission[] pArr = PermissionHelper.getManager().getPermissions("FLOW" + flowID);

		if (pArr == null) return null;
		
		FlowPermission[] fpArr = new FlowPermission[pArr.length];
		 
		for (int i = 0; i < fpArr.length; i++)
		{
			fpArr[i] = new FlowPermission(pArr[i].getRoleID(), 
					0,
					flowID,
					Integer.parseInt(pArr[i].getResource().substring(8)),
					pArr[i].getPermission());
		}
		return fpArr;
	}
	/* (non-Javadoc)
	 * @see com.founder.e5.permission.FlowPermissionManager#getPermissionsByFlowNode(int, int)
	 */
	public FlowPermission[] getPermissionsByFlowNode(int flowID, int flowNodeID) throws E5Exception
	{
		Permission[] pArr = PermissionHelper.getManager().getPermissions("FLOW" + flowID, "FLOWNODE" + flowNodeID);

		if (pArr == null) return null;
		
		FlowPermission[] fpArr = new FlowPermission[pArr.length];
		 
		for (int i = 0; i < fpArr.length; i++)
		{
			fpArr[i] = new FlowPermission(pArr[i].getRoleID(), 
					0,
					flowID,
					flowNodeID,
					pArr[i].getPermission());
		}
		return fpArr;
	}
	/* (non-Javadoc)
	 * @see com.founder.e5.permission.FlowPermissionManager#getPermissionsByRole(int, int)
	 */
	public FlowPermission[] getPermissionsByRole(int roleID, int flowID) throws E5Exception
	{
		Permission[] pArr = PermissionHelper.getManager().getPermissions(roleID, "FLOW" + flowID);
		if (pArr == null)
			return null;
		FlowPermission[] fpArr = new FlowPermission[pArr.length];
		 
		for (int i = 0; i < fpArr.length; i++)
		{
			fpArr[i] = new FlowPermission(roleID, 
					0,
					flowID,
					Integer.parseInt(pArr[i].getResource().substring(8)),
					pArr[i].getPermission());
		}
		return fpArr;
	}
	/* (non-Javadoc)
	 * @see com.founder.e5.permission.FlowPermissionManager#getPermissionsByRole(int)
	 */
	public FlowPermission[] getPermissionsByRole(int roleID) throws E5Exception
	{
		Permission[] pArr = PermissionHelper.getManager().find(roleID, "FLOW%");
		return getFPArrayByRole(pArr, roleID);
	}
	/* (non-Javadoc)
	 * @see com.founder.e5.permission.FlowPermissionManager#getUnflowPermissionsByRole(int)
	 */
	public FlowPermission[] getUnflowPermissionsByRole(int roleID) throws E5Exception
	{
		Permission[] pArr = PermissionHelper.getManager().getPermissions(roleID, "UNFLOW");
		if (pArr == null) return null;

		FlowPermission[] fpArr = new FlowPermission[pArr.length];
		 
		for (int i = 0; i < fpArr.length; i++)
		{
			fpArr[i] = new FlowPermission(roleID, 
					Integer.parseInt(pArr[i].getResource()),
					0,
					0,
					pArr[i].getPermission());
		}
		return fpArr;
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.permission.FlowPermissionManager#deleteRole(int)
	 */
	public void deleteRole(int roleID) throws E5Exception
	{
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.permission.FlowPermissionReader#getFlowsNewable(int)
	 */
	public int[] getFlowsNewable(int roleID) throws E5Exception
	{
		//得到所有的流程的第一个节点（流程新建权限保存在第一个流程节点的第1位）
		FlowManager flowReader = (FlowManager)Context.getBean(FlowManager.class);
		Flow[] flows = flowReader.getFlows(0);
		if (flows == null) return null;
		
		int[] firstNodes = new int[flows.length];
		for (int i = 0; i < firstNodes.length; i++) {
			firstNodes[i] = flows[i].getFirstFlowNodeID();
		}

		//取出角色有权限的所有流程节点
		FlowPermission[] pArr = getPermissionsByRole(roleID);
		if (pArr == null) return null;

		int mask = 2;//第1位
		List result = new ArrayList(firstNodes.length);
		for (int i = 0; i < pArr.length; i++){
			if (!ArrayUtils.contains(firstNodes, pArr[i].getFlowNodeID())) continue;
			
			if (PermissionHelper.hasPermission(pArr[i].getPermission(), mask))
				result.add(new Integer(pArr[i].getFlowID()));
		}
		if (result.size() == 0) return null;
		
		int[] flowArr = new int[result.size()];
		for (int i = 0; i < flowArr.length; i++) {
			flowArr[i] = ((Integer)result.get(i)).intValue();
		}
		return flowArr;
	}
	/* (non-Javadoc)
	 * @see com.founder.e5.permission.FlowPermissionReader#getRolesOfFlowNode(int, int, int[])
	 */
	public int[] getRolesOfFlowNode(int flowID, int flowNodeID, int[] maskArray) throws E5Exception
	{
		Permission[] pArr = PermissionHelper.getManager().getPermissions("FLOW" + flowID, "FLOWNODE" + flowNodeID);
		if (pArr == null)
			return null;

		return PermissionHelper.getRoleByMask(pArr, maskArray);
	}
	/**取一个操作在权限位上的顺序
	 * @param procType 操作的类型
	 * @param parentID 
	 * <BR>对流程操作，parentID就是flowNodeID；
	 * <BR>对非流程操作，parentID就是docTypeID
	 * @param procID
	 * @return
	 * @throws E5Exception
	 */
	private int getPermissionOrder(int procType, int parentID, int procID) throws E5Exception
	{
		int order = 0;
		switch (procType)
		{
			case Proc.PROC_BACK:
				order = 1;
				break;
			case Proc.PROC_DO:
				order = 2;
				break;
			case Proc.PROC_GO:
				order = 3;
				break;
			case Proc.PROC_JUMP:
				order = 4;
				ProcFlow[] jumps = PermissionHelper.getProcManager().getJumps(parentID);
				if (jumps != null)
				{
					for (int i = 0; i < jumps.length; i++)
						if (jumps[i].getProcID() < procID)
							order++;
						else break;
				}
				break;
			case Proc.PROC_UNFLOW:
				ProcUnflow[] unflows = PermissionHelper.getProcManager().getUnflows(parentID);
				if (unflows != null)
				{
					for (int i = 0; i < unflows.length; i++)
						if (unflows[i].getProcID() < procID)
							order++;
						else break;
				}
				break;
			default:
				throw new E5Exception("Unknown Type!" + procType);
		}
		return order;
	}
}