package com.founder.e5.permission;

import java.util.ArrayList;
import java.util.List;

import com.founder.e5.commons.ArrayUtils;
import com.founder.e5.context.Context;
import com.founder.e5.context.E5Exception;
import com.founder.e5.flow.Flow;
import com.founder.e5.flow.FlowReader;
import com.founder.e5.flow.Proc;
import com.founder.e5.flow.ProcFlow;
import com.founder.e5.flow.ProcReader;
import com.founder.e5.flow.ProcUnflow;

/**
 * @created 18-7-2005 10:51:19
 * @author Gong Lijie
 * @version 1.0
 */
class FlowPermissionReaderImpl implements FlowPermissionReader {

	FlowPermissionReaderImpl()
	{
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.permission.FlowPermissionReader#getUnflowPermission(int, int)
	 */
	public int getUnflowPermission(int roleID, int docTypeID)
	throws E5Exception
	{
		PermissionReader reader = PermissionHelper.getReader();
		Permission p = reader.get(roleID, "UNFLOW", String.valueOf(docTypeID));
		if (p != null)
			return p.getPermission();
		else
			return 0;
	}
	/* (non-Javadoc)
	 * @see com.founder.e5.permission.FlowPermissionReader#getPermissionsByRole(int, int)
	 */
	public FlowPermission[] getPermissionsByRole(int roleID, int flowID)
	throws E5Exception
	{
		Permission[] pArr = PermissionHelper.getReader().getPermissions(roleID, "FLOW" + flowID);
		
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
	 * @see com.founder.e5.permission.FlowPermissionReader#getPermissionsByRole(int)
	 */
	public FlowPermission[] getPermissionsByRole(int roleID)
	throws E5Exception
	{
		Permission[] pArr = PermissionHelper.getReader().getPermissions(roleID);
		if (pArr == null) return null;
		
		List list = new ArrayList(pArr.length / 2);
		for (int i = 0; i < pArr.length; i++){
			if (pArr[i].getResourceType().startsWith("FLOW"))
			{
				FlowPermission fp = new FlowPermission(roleID, 
						0,
						Integer.parseInt(pArr[i].getResourceType().substring(4)),
						Integer.parseInt(pArr[i].getResource().substring(8)),
						pArr[i].getPermission());
				list.add(fp);
			}
		}
		if (list.size() == 0) return null;
		return (FlowPermission[])list.toArray(new FlowPermission[0]);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.permission.FlowPermissionReader#getUnflowPermissionsByRole(int)
	 */
	public FlowPermission[] getUnflowPermissionsByRole(int roleID)
	throws E5Exception
	{
		Permission[] pArr = PermissionHelper.getReader().getPermissions(roleID);
		if (pArr == null) return null;
		
		List list = new ArrayList(pArr.length / 2);
		for (int i = 0; i < pArr.length; i++){
			if (pArr[i].getResourceType().equals("UNFLOW"))
			{
				FlowPermission fp = new FlowPermission(roleID, 
						Integer.parseInt(pArr[i].getResource()),
						0,
						0,
						pArr[i].getPermission());
				list.add(fp);
			}
		}
		if (list.size() == 0) return null;
		return (FlowPermission[])list.toArray(new FlowPermission[0]);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.permission.FlowPermissionReader#hasPermission(int, int, int, int, int)
	 */
	public boolean hasPermission(int roleID, int flowID, int flowNodeID, 
			int procType, int procID)
	throws E5Exception
	{
		int p = get(roleID, flowID, flowNodeID);
		//取每个操作的权限位号
		int nOrder = getPermissionOrder(procType, flowNodeID, procID);
		if (PermissionHelper.getBit(p, nOrder) == 1)
			return true;
		else
			return false;
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.permission.FlowPermissionReader#hasPermission(int, int, int, String)
	 */
	public boolean hasPermission(int roleID, int flowID, int flowNodeID, String procName)
	throws E5Exception
	{
		ProcReader procReader = PermissionHelper.getProcReader();
		Proc proc = procReader.getProc(flowNodeID, procName);
		if (proc == null) return false;
		
		return hasPermission(roleID, flowID, flowNodeID, proc.getProcType(), 
				proc.getProcID());
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.permission.FlowPermissionReader#hasUnflowPermission(int, int, String)
	 */
	public boolean hasUnflowPermission(int roleID, int docTypeID, String procName)
	throws E5Exception
	{
		ProcReader procReader = PermissionHelper.getProcReader();
		Proc proc = procReader.getUnflow(docTypeID, procName);
		return hasUnflowPermission(roleID, proc);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.permission.FlowPermissionReader#hasUnflowPermission(int, int)
	 */
	public boolean hasUnflowPermission(int roleID, int procID)
	throws E5Exception
	{
		ProcReader procReader = PermissionHelper.getProcReader();
		Proc proc = procReader.getUnflow(procID);
		return hasUnflowPermission(roleID, proc);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.permission.FlowPermissionReader#get(int, int, int)
	 */
	public int get(int roleID, int flowID, int flowNodeID) 
	throws E5Exception
	{
		Permission p = PermissionHelper.getReader().get(roleID, "FLOW" + flowID, "FLOWNODE" + flowNodeID);
		if (p != null)
			return p.getPermission();
		else
			return 0;
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.permission.FlowPermissionReader#hasUnflowPermission(int, com.founder.e5.flow.Proc)
	 */
	public boolean hasUnflowPermission(int roleID, Proc proc)
	throws E5Exception
	{
		ProcUnflow unflow = (ProcUnflow)proc;
		//非流程权限的权限记录比较特殊，缺省是有权限的，所以0表示有权限，1表示没有权限
		int p = getUnflowPermission(roleID, unflow.getDocTypeID());
		int order = getPermissionOrder(Proc.PROC_UNFLOW, unflow.getDocTypeID(), unflow.getProcID());

		if (((p == 0)) || (PermissionHelper.getBit(p, order) == 0)) 
			return true;
		else
			return false;
	}
	/* (non-Javadoc)
	 * @see com.founder.e5.permission.FlowPermissionReader#hasPermission(int, com.founder.e5.flow.Proc)
	 */
	public boolean hasPermission(int roleID, Proc proc) throws E5Exception
	{
		return hasPermission(roleID, ((ProcFlow)proc).getFlowID(), 
				((ProcFlow)proc).getFlowNodeID(), 
				proc.getProcType(), proc.getProcID());
	}
	/* (non-Javadoc)
	 * @see com.founder.e5.permission.FlowPermissionReader#getFlowsNewable(int)
	 */
	public int[] getFlowsNewable(int roleID) throws E5Exception
	{
		//得到所有的流程的第一个节点（流程新建权限保存在第一个流程节点的第1位）
		FlowReader flowReader = (FlowReader)Context.getBean(FlowReader.class);
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
		Permission[] pArr = PermissionHelper.getReader().getPermissions("FLOW" + flowID, "FLOWNODE" + flowNodeID);
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
				ProcReader procReader = PermissionHelper.getProcReader();
				ProcFlow[] jumps = procReader.getJumps(parentID);
				if (jumps != null)
				{
					for (int i = 0; i < jumps.length; i++)
						if (jumps[i].getProcID() < procID)
							order++;
						else break;
				}
				break;
			case Proc.PROC_UNFLOW:
				ProcReader procReader1 = PermissionHelper.getProcReader();
				ProcUnflow[] unflows = procReader1.getUnflows(parentID);
				if (unflows != null)
				{
					for (int i = 0; i < unflows.length; i++)
						if (unflows[i].getProcID() < procID)
							order++;
						else break;
					break;
				}
			default:
				throw new E5Exception("Unknown Type!" + procType);
		}
		return order;
	}
}