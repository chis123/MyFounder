package com.founder.e5.flow;

import java.util.ArrayList;
import java.util.List;

import com.founder.e5.context.E5Exception;

/**
 * @created 04-8-2005 14:16:41
 * @author Gong Lijie
 * @version 1.0
 */
class ProcReaderImpl implements ProcReader {
	private ProcFlow getProc(int flowNodeID, int procType) throws E5Exception
	{
		ProcFlow[] arr = getProcArr();
		if (arr == null) return null;

		for (int i = 0; i < arr.length; i++)
			if ((arr[i].getFlowNodeID() == flowNodeID)
					&& (arr[i].getProcType() == procType))
				return arr[i];
		return null;
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.flow.ProcReader#getBack(int)
	 */
	public ProcFlow getBack(int flowNodeID) throws E5Exception
	{
		return getProc(flowNodeID, Proc.PROC_BACK);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.flow.ProcReader#getDo(int)
	 */
	public ProcFlow getDo(int flowNodeID) throws E5Exception
	{
		return getProc(flowNodeID, Proc.PROC_DO);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.flow.ProcReader#getGo(int)
	 */
	public ProcFlow getGo(int flowNodeID) throws E5Exception
	{
		return getProc(flowNodeID, Proc.PROC_GO);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.flow.ProcReader#getJump(int)
	 */
	public ProcFlow getJump(int procID) throws E5Exception
	{
		return getProc(procID);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.flow.ProcReader#getJump(int, java.lang.String)
	 */
	public ProcFlow getJump(int flowNodeID, String procName) throws E5Exception
	{
		ProcFlow[] arr = getProcArr();
		if (arr == null) return null;
		for (int i = 0; i < arr.length; i++)
			if ((arr[i].getFlowNodeID() == flowNodeID)
					&& (procName.equals(arr[i].getProcName()))
					&& (Proc.PROC_JUMP == arr[i].getProcType()))
				return arr[i];
		return null;
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.flow.ProcReader#getJumps(int)
	 */
	public ProcFlow[] getJumps(int flowNodeID) throws E5Exception
	{
		ProcFlow[] arr = getProcArr();
		if (flowNodeID == 0) return arr;
		
		List list = new ArrayList();
		if (arr != null)
			for (int i = 0; i < arr.length; i++)
				if ((arr[i].getFlowNodeID() == flowNodeID)
						&& (Proc.PROC_JUMP == arr[i].getProcType()))
					list.add(arr[i]);
		if (list.size() == 0) return null;
		
		return (ProcFlow[])list.toArray(new ProcFlow[0]);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.flow.ProcReader#getUnflow(int)
	 */
	public ProcUnflow getUnflow(int procID) throws E5Exception
	{
		ProcUnflow[] arr = getUnflowArr();
		if (arr != null)
		{
			for (int i = 0; i < arr.length; i++)
				if (arr[i].getProcID() == procID)
					return arr[i];
			return null;
		}
		else
			return FlowHelper.getProcManager().getUnflow(procID);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.flow.ProcReader#getUnflow(int, java.lang.String)
	 */
	public ProcUnflow getUnflow(int docTypeID, String procName) throws E5Exception
	{
		ProcUnflow[] arr = getUnflowArr();
		if (arr != null)
		{
			for (int i = 0; i < arr.length; i++)
				if ((arr[i].getDocTypeID() == docTypeID)
						&& (procName.equals(arr[i].getProcName())))
					return arr[i];
			return null;
		}
		else
			return FlowHelper.getProcManager().getUnflow(docTypeID, procName);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.flow.ProcReader#getUnflows(int)
	 */
	public ProcUnflow[] getUnflows(int docTypeID) throws E5Exception
	{
		ProcUnflow[] arr = getUnflowArr();
		if (docTypeID == 0) return arr;
		
		List list = new ArrayList();
		if (arr != null)
			for (int i = 0; i < arr.length; i++)
				if (arr[i].getDocTypeID() == docTypeID)
					list.add(arr[i]);
		if (list.size() == 0) return null;
		
		return (ProcUnflow[])list.toArray(new ProcUnflow[0]);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.flow.ProcReader#getOperation(int)
	 */
	public Operation getOperation(int opID) throws E5Exception
	{
		Operation[] arr = getOperationArr();
		if (arr != null)
		{
			for (int i = 0; i < arr.length; i++)
				if (arr[i].getID() == opID)
					return arr[i];
			return null;
		}
		else
			return FlowHelper.getProcManager().getOperation(opID);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.flow.ProcReader#getOperations(int)
	 */
	public Operation[] getOperations(int docTypeID) throws E5Exception
	{
		Operation[] arr = getOperationArr();
		if (docTypeID == 0) return arr;
		
		List list = new ArrayList();
		if (arr != null)
			for (int i = 0; i < arr.length; i++)
				if (arr[i].getDocTypeID() == docTypeID)
					list.add(arr[i]);
		if (list.size() == 0) return null;
		
		return (Operation[])list.toArray(new Operation[0]);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.flow.ProcReader#getIcon(int)
	 */
	public Icon getIcon(int iconID) throws E5Exception
	{
		Icon[] arr = getIcons();
		if (arr != null)
		{
			for (int i = 0; i < arr.length; i++)
				if (arr[i].getID() == iconID)
					return arr[i];
			return null;
		}
		else
			return FlowHelper.getProcManager().getIcon(iconID);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.flow.ProcReader#getIcons()
	 */
	public Icon[] getIcons() throws E5Exception
	{
		FlowCache cache = FlowHelper.getFlowCache();
		if (cache != null)
			return cache.getIcons();
		return null;
	}
	
	private ProcFlow[] getProcArr()
	{
		FlowCache cache = FlowHelper.getFlowCache();
		if (cache != null) return cache.getProcs();
		return null;
	}
	private ProcUnflow[] getUnflowArr()
	{
		FlowCache cache = FlowHelper.getFlowCache();
		if (cache != null)
			return cache.getUnflows();
		return null;
	}
	private Operation[] getOperationArr()
	{
		FlowCache cache = FlowHelper.getFlowCache();
		if (cache != null)
			return cache.getOperations();
		return null;
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.flow.ProcReader#getProc(int, java.lang.String)
	 */
	public ProcFlow getProc(int flowNodeID, String procName) throws E5Exception
	{
		ProcFlow[] arr = getProcArr();
		if (arr == null) return null;

		for (int i = 0; i < arr.length; i++)
			if ((arr[i].getFlowNodeID() == flowNodeID)
					&& (procName.equals(arr[i].getProcName())))
				return arr[i];
		return null;
	}

	public ProcFlow getProc(int procID) throws E5Exception
	{
		ProcFlow[] arr = getProcArr();
		if (arr == null) return null;

		for (int i = 0; i < arr.length; i++)
			if (arr[i].getProcID() == procID)
				return arr[i];
		return null;
	}

	public ProcFlow[] getProcs(int flowNodeID) throws E5Exception
	{
		ProcFlow[] arr = getProcArr();
		if (flowNodeID == 0) return arr;
		
		List list = new ArrayList();
		if (arr != null)
			for (int i = 0; i < arr.length; i++)
				if (arr[i].getFlowNodeID() == flowNodeID)
					list.add(arr[i]);
		if (list.size() == 0) return null;
		
		return (ProcFlow[])list.toArray(new ProcFlow[0]);
	}

	public Proc get(int procID) throws E5Exception
	{
		Proc proc = getProc(procID);
		if (proc == null)
			proc = getUnflow(procID);
		return proc;
	}
}