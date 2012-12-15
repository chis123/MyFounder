package com.founder.e5.flow;

import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.type.Type;

import com.founder.e5.context.BaseDAO;
import com.founder.e5.context.DAOHelper;
import com.founder.e5.context.E5Exception;
import com.founder.e5.context.EUID;

/**
 * @created 04-8-2005 14:16:26
 * @author Gong Lijie
 * @version 1.0
 */
class ProcManagerImpl implements ProcManager 
{
	/**
	 * DO/GO/BACK操作每个节点最多只有一个，
	 * 所以当已经有该类型的操作时，
	 * 执行的不是增加动作，而是修改
	 * @param flowNodeID
	 * @param proc
	 * @param procType
	 * @throws E5Exception
	 */
	private void addProc(int flowNodeID, Proc proc, int procType) 
	throws E5Exception
	{
		//取出已有的操作
		ProcFlow procFlow = getProc(procType, flowNodeID);
		//若没有，则新加
		if (procFlow == null)
		{
			procFlow = new ProcFlow();
			procFlow.setDescription(proc.getDescription());
			procFlow.setFlowNodeID(flowNodeID);
			procFlow.setIconID(proc.getIconID());
			procFlow.setOpID(proc.getOpID());
			procFlow.setProcName(proc.getProcName());
	
			procFlow.setProcType(procType);
			createProc(procFlow);
		}
		else//否则修改
		{
			procFlow.setDescription(proc.getDescription());
			procFlow.setIconID(proc.getIconID());
			procFlow.setOpID(proc.getOpID());
			procFlow.setProcName(proc.getProcName());
	
			procFlow.setProcType(procType);
			updateProc(procFlow);
		}
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.flow.ProcManager#addBack(int, com.founder.e5.flow.Proc)
	 */
	public void addBack(int flowNodeID, Proc proc) throws E5Exception
	{
		addProc(flowNodeID, proc, Proc.PROC_BACK);
	}
	/* (non-Javadoc)
	 * @see com.founder.e5.flow.ProcManager#addDo(int, com.founder.e5.flow.Proc)
	 */
	public void addDo(int flowNodeID, Proc proc) throws E5Exception
	{
		addProc(flowNodeID, proc, Proc.PROC_DO);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.flow.ProcManager#addGo(int, com.founder.e5.flow.Proc)
	 */
	public void addGo(int flowNodeID, Proc proc) throws E5Exception
	{
		addProc(flowNodeID, proc, Proc.PROC_GO);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.flow.ProcManager#createJump(int, com.founder.e5.flow.ProcFlow)
	 */
	public void createJump(int flowNodeID, ProcFlow proc) throws E5Exception
	{
		proc.setFlowNodeID(flowNodeID);
		proc.setProcType(Proc.PROC_JUMP);
		
		createProc(proc);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.flow.ProcManager#createUnflow(int, com.founder.e5.flow.ProcUnflow)
	 */
	public void createUnflow(int docTypeID, ProcUnflow proc) throws E5Exception
	{
		proc.setDocTypeID(docTypeID);
		int id = (int)EUID.getID("ProcID");
		proc.setProcID(id);
		try
		{
			BaseDAO dao = new BaseDAO();
			dao.save(proc);
		}
		catch (HibernateException e)
		{
			throw new E5Exception("[ProcUnflowCreate]", e);
		}

		/**
		 * 发通知给监听器，传递参数：
		 * 操作类型（ADD/UPDATE/DELETE)
		 */
		proc.setDocTypeID(docTypeID);
		proc.setProcType(Proc.PROC_UNFLOW);
		proc.notifyObservers(ProcOrderObserver.ADD);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.flow.ProcManager#deleteBack(int)
	 */
	public void deleteBack(int flowNodeID) throws E5Exception
	{
		deleteProc(getBack(flowNodeID));
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.flow.ProcManager#deleteDo(int)
	 */
	public void deleteDo(int flowNodeID) throws E5Exception
	{
		deleteProc(getDo(flowNodeID));
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.flow.ProcManager#deleteGo(int)
	 */
	public void deleteGo(int flowNodeID) throws E5Exception
	{
		deleteProc(getGo(flowNodeID));
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.flow.ProcManager#deleteJump(int, int)
	 */
	public void deleteJump(int procID) throws E5Exception
	{
		deleteProc(procID);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.flow.ProcManager#deleteUnflow(int)
	 */
	public void deleteUnflow(int procID) throws E5Exception
	{
		BaseDAO dao = new BaseDAO();
		Session s = null;
		Transaction t = null;
		
		ProcUnflow proc;
		try
		{
			try
			{
				s = dao.getSession();
				t = dao.beginTransaction(s);
				proc = (ProcUnflow)s.get(ProcUnflow.class, new Integer(procID));
				if (proc != null) s.delete(proc);
				t.commit();
			}
			catch (HibernateException e)
			{
				t.rollback();
				throw e;
			}
			finally
			{
				dao.closeSession(s);
			}
		}
		catch (HibernateException e)
		{
			throw new E5Exception("[ProcUnflowDelete]", e);
		}
		
		/**
		 * 发通知给监听器，传递参数：
		 * 操作类型（ADD/UPDATE/DELETE)
		 */
		proc.setProcType(Proc.PROC_UNFLOW);
		proc.notifyObservers(ProcOrderObserver.DELETE);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.flow.ProcManager#deleteUnflows(int)
	 */
	public void deleteUnflows(int docTypeID) throws E5Exception
	{
		DAOHelper.delete("delete from ProcUnflow as j where j.docTypeID=:id", 
				new Integer(docTypeID), 
				Hibernate.INTEGER);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.flow.ProcManager#updateJump(com.founder.e5.flow.ProcFlow)
	 */
	public void updateJump(ProcFlow proc) throws E5Exception
	{
		proc.setProcType(Proc.PROC_JUMP);
		updateProc(proc);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.flow.ProcManager#updateUnflow(com.founder.e5.flow.ProcUnflow)
	 */
	public void updateUnflow(ProcUnflow proc) throws E5Exception
	{
		try
		{
			BaseDAO dao = new BaseDAO();
			dao.update(proc);
		}
		catch (HibernateException e)
		{
			throw new E5Exception("[ProcUnflowUpdate]", e);
		}

		/**
		 * 发通知给监听器，传递参数：
		 * 操作类型（ADD/UPDATE/DELETE)
		 */
		proc.setProcType(Proc.PROC_UNFLOW);
		proc.notifyObservers(ProcOrderObserver.UPDATE);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.flow.ProcManager#createOperation(int, com.founder.e5.flow.Operation)
	 */
	public void createOperation(int docTypeID, Operation op) throws E5Exception
	{
		op.setDocTypeID(docTypeID);
		int id = (int)EUID.getID("OperationID");
		op.setID(id);
		try
		{
			BaseDAO dao = new BaseDAO();
			dao.save(op);
		}
		catch (HibernateException e)
		{
			throw new E5Exception("[OperationCreate]", e);
		}
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.flow.ProcManager#updateOperation(com.founder.e5.flow.Operation)
	 */
	public void updateOperation(Operation op) throws E5Exception
	{
		try
		{
			BaseDAO dao = new BaseDAO();
			dao.update(op);
		}
		catch (HibernateException e)
		{
			throw new E5Exception("[OperationUpdate]", e);
		}
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.flow.ProcManager#deleteOperation(int)
	 */
	public void deleteOperation(int opID) throws E5Exception
	{
		if (opUsed(opID))
			throw new E5Exception(1001, "Operation used!");
		
		DAOHelper.delete("delete from Operation as j where j.ID=:id", 
				new Integer(opID), 
				Hibernate.INTEGER);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.flow.ProcManager#create(com.founder.e5.flow.Icon)
	 */
	public void createIcon(Icon icon) throws E5Exception
	{
		int id = (int)EUID.getID("IconID");
		icon.setID(id);
		try
		{
			BaseDAO dao = new BaseDAO();
			dao.save(icon);
		}
		catch (HibernateException e)
		{
			throw new E5Exception("[IconCreate]", e);
		}
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.flow.ProcManager#update(com.founder.e5.flow.Icon)
	 */
	public void updateIcon(Icon icon) throws E5Exception
	{
		try
		{
			BaseDAO dao = new BaseDAO();
			dao.update(icon);
		}
		catch (HibernateException e)
		{
			throw new E5Exception("[IconUpdate]", e);
		}
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.flow.ProcManager#delete(int)
	 */
	public void deleteIcon(int iconID) throws E5Exception
	{
		if (iconUsed(iconID))
			throw new E5Exception(1002, "Icon used!");
		
		DAOHelper.delete("delete from Icon as j where j.ID=:id", 
				new Integer(iconID), 
				Hibernate.INTEGER);
	}
	
	/* (non-Javadoc)
	 * @see com.founder.e5.flow.ProcReader#getBack(int)
	 */
	public ProcFlow getBack(int flowNodeID) throws E5Exception
	{
		return getProc(Proc.PROC_BACK, flowNodeID);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.flow.ProcReader#getDo(int)
	 */
	public ProcFlow getDo(int flowNodeID) throws E5Exception
	{
		return getProc(Proc.PROC_DO, flowNodeID);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.flow.ProcReader#getGo(int)
	 */
	public ProcFlow getGo(int flowNodeID) throws E5Exception
	{
		return getProc(Proc.PROC_GO, flowNodeID);
	}
	private ProcFlow getProc(int procType, int flowNodeID) throws E5Exception
	{
		try
		{
			List list = getProcList(procType, flowNodeID);
			if (list == null || list.size() == 0) return null;
			return (ProcFlow)list.get(0);
		}
		catch (HibernateException e)
		{
			throw new E5Exception("[ProcGet]", e);
		}
	}
	private List getProcList(int procType, int flowNodeID) throws E5Exception
	{
		try
		{
			return DAOHelper.find("from ProcFlow where flowNodeID=? and procType=? order by procID", 
					new Object[]{new Integer(flowNodeID), new Integer(procType)});
		}
		catch (HibernateException e)
		{
			throw new E5Exception("[ProcGet]", e);
		}
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
		return getProc(flowNodeID, procName);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.flow.ProcReader#getJumps(int)
	 */
	public ProcFlow[] getJumps(int flowNodeID) throws E5Exception
	{
		List list;
		if (flowNodeID != 0)
			list = getProcList(Proc.PROC_JUMP, flowNodeID);
		else
			list = DAOHelper.find("from ProcFlow where procType=? order by j.flowNodeID, j.procID", 
					new Object[]{new Integer(Proc.PROC_JUMP)});
		if (list == null || list.size() == 0) return null;
		
		return (ProcFlow[])list.toArray(new ProcFlow[0]);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.flow.ProcReader#getUnflow(int)
	 */
	public ProcUnflow getUnflow(int procID) throws E5Exception
	{
		try
		{
			BaseDAO dao = new BaseDAO();
			return (ProcUnflow)dao.get(ProcUnflow.class, new Integer(procID));
		}
		catch (HibernateException e)
		{
			throw new E5Exception("[ProcUnflowGet]", e);
		}
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.flow.ProcReader#getUnflow(int, java.lang.String)
	 */
	public ProcUnflow getUnflow(int docTypeID, String procName) throws E5Exception
	{
		List list = DAOHelper.find("from ProcUnflow as j where j.docTypeID=:nodeID and j.procName=:name", 
				new String[]{"nodeID", "name"},
				new Object[]{new Integer(docTypeID), procName}, 
				new Type[]{Hibernate.INTEGER, Hibernate.STRING});
		if (list.size() == 0)
			return null;
		
		return (ProcUnflow)list.get(0);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.flow.ProcReader#getUnflows(int)
	 */
	public ProcUnflow[] getUnflows(int docTypeID) throws E5Exception
	{
		List list;
		if (docTypeID != 0)
			list = DAOHelper.find("from ProcUnflow as j where j.docTypeID=:nodeID order by j.procID", 
				new Integer(docTypeID), 
				Hibernate.INTEGER);
		else
			list = DAOHelper.find("from ProcUnflow as j order by j.docTypeID, j.procID");
		if (list.size() == 0)
			return null;
		
		return (ProcUnflow[])list.toArray(new ProcUnflow[0]);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.flow.ProcReader#getOperation(int)
	 */
	public Operation getOperation(int opID) throws E5Exception
	{
		try
		{
			BaseDAO dao = new BaseDAO();
			return (Operation)dao.get(Operation.class, new Integer(opID));
		}
		catch (HibernateException e)
		{
			throw new E5Exception("[OperationGet]", e);
		}
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.flow.ProcReader#getOperations(int)
	 */
	public Operation[] getOperations(int docTypeID) throws E5Exception
	{
		List list;
		if (docTypeID != 0)
			list = DAOHelper.find("from Operation as j where j.docTypeID=:nodeID order by j.ID", 
				new Integer(docTypeID), 
				Hibernate.INTEGER);
		else
			list = DAOHelper.find("from Operation as j order by j.docTypeID, j.ID");
		if (list.size() == 0)
			return null;
		
		return (Operation[])list.toArray(new Operation[0]);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.flow.ProcReader#get(int)
	 */
	public Icon getIcon(int iconID) throws E5Exception
	{
		try
		{
			BaseDAO dao = new BaseDAO();
			return (Icon)dao.get(Icon.class, new Integer(iconID));
		}
		catch (HibernateException e)
		{
			throw new E5Exception("[IconGet]", e);
		}
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.flow.ProcReader#getIcons()
	 */
	public Icon[] getIcons() throws E5Exception
	{
		List list = DAOHelper.find("from Icon as icon order by icon.ID");
		if (list.size() == 0)
			return null;
		return (Icon[])list.toArray(new Icon[0]);
	}
	
	/**
	 * 判断一个图标是否已经被用于流程设置
	 * @param id
	 * @return boolean
	 * @throws E5Exception
	 */
	private boolean iconUsed(int id) throws E5Exception
	{
		//1.判断流程操作中使用情况
		List list = DAOHelper.find("from ProcFlow as fn where fn.iconID=?",
				new Object[]{new Integer(id)});
		if (list != null && list.size() > 0) return true;
		
		//2.判断非流程操作中使用情况
		list = DAOHelper.find("from ProcUnflow as fn where fn.iconID=?",
				new Object[]{new Integer(id)});
		if (list != null && list.size() > 0) return true;

		return false;
	}
	
	/**
	 * 判断一个操作是否已经被用于流程设置
	 * @param id
	 * @return boolean
	 * @throws E5Exception
	 */
	private boolean opUsed(int id) throws E5Exception
	{
		//1.判断跳转操作中使用情况
		List list = DAOHelper.find("from ProcFlow as fn where fn.opID=?",
				new Object[]{new Integer(id)});
		if (list != null && list.size() > 0) return true;
		//2.判断非流程操作中使用情况
		list = DAOHelper.find("from ProcUnflow as fn where fn.opID=?",
				new Object[]{new Integer(id)});
		if (list != null && list.size() > 0) return true;

		return false;
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.flow.ProcManager#deleteOperations(int)
	 */
	public void deleteOperations(int docTypeID) throws E5Exception
	{
		DAOHelper.delete("delete from Operation as j where j.docTypeID=:id", 
				new Integer(docTypeID), 
				Hibernate.INTEGER);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.flow.ProcReader#getProc(int, java.lang.String)
	 */
	public ProcFlow getProc(int flowNodeID, String procName) 
	throws E5Exception
	{
		List list = DAOHelper.find("from ProcFlow as j where j.flowNodeID=? and j.procName=?", 
				new Object[]{new Integer(flowNodeID), procName});
		if (list == null || list.size() == 0) return null;
		
		return (ProcFlow)list.get(0);
	}

	public ProcFlow getProc(int procID) throws E5Exception
	{
		try
		{
			BaseDAO dao = new BaseDAO();
			return (ProcFlow)dao.get(ProcFlow.class, new Integer(procID));
		}
		catch (HibernateException e)
		{
			throw new E5Exception("[ProcGet]", e);
		}
	}

	public void createProc(int flowNodeID, ProcFlow proc) throws E5Exception
	{
		proc.setFlowNodeID(flowNodeID);
		createProc(proc);
	}
	public void createProc(ProcFlow proc) throws E5Exception
	{
		if (proc.getProcType() < 1) throw new E5Exception("[ProcManager.createProc]ProcType not set!");
		if (proc.getFlowNodeID() < 1) throw new E5Exception("[ProcManager.createProc]FlowNodeID not set!");
		
		//查找流程ID，赋值
		if (proc.getFlowID() < 1)
		{
			FlowManager manager = FlowHelper.getFlowManager();
			FlowNode node = manager.getFlowNode(proc.getFlowNodeID());
			if (node != null) proc.setFlowID(node.getFlowID());
		}
		
		int id = (int)EUID.getID("ProcID");
		proc.setProcID(id);		
		try
		{
			BaseDAO dao = new BaseDAO();
			dao.save(proc);
		}
		catch (HibernateException e)
		{
			throw new E5Exception("[ProcManager.createProc]", e);
		}

		/**
		 * 发通知给监听器，传递参数：
		 * 操作类型（ADD/UPDATE/DELETE)
		 */
		proc.notifyObservers(ProcOrderObserver.ADD);
	}

	public void deleteProc(int procID) throws E5Exception
	{
		deleteProc(getProc(procID));
	}
	private void deleteProc(ProcFlow proc) throws E5Exception
	{
		if (proc == null) return;
		
		try
		{
			DAOHelper.delete("delete from ProcFlow where procID=?", 
					new Object[]{new Integer(proc.getProcID())});
		}
		catch (HibernateException e)
		{
			throw new E5Exception("[ProcDelete]", e);
		}
		
		/**
		 * 发通知给监听器，传递参数：
		 * 操作类型（ADD/UPDATE/DELETE)
		 */
		proc.notifyObservers(ProcOrderObserver.DELETE);
	}
	
	/* (non-Javadoc)
	 * @see com.founder.e5.flow.ProcManager#updateProc(com.founder.e5.flow.ProcFlow)
	 */
	public void updateProc(ProcFlow proc) throws E5Exception
	{
		try
		{
			BaseDAO dao = new BaseDAO();
			dao.update(proc);
		}
		catch (HibernateException e)
		{
			throw new E5Exception("[ProcUpdate]", e);
		}

		/**
		 * 发通知给监听器，传递参数：
		 * 操作类型（ADD/UPDATE/DELETE)
		 */
		proc.notifyObservers(ProcOrderObserver.UPDATE);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.flow.ProcReader#getProcs(int)
	 */
	public ProcFlow[] getProcs(int flowNodeID) throws E5Exception
	{
		List list;
		if (flowNodeID != 0)
			list = DAOHelper.find("from ProcFlow as j where j.flowNodeID=? order by j.procID", 
				new Object[]{new Integer(flowNodeID)});
		else
			list = DAOHelper.find("from ProcFlow as j order by j.flowNodeID, j.procID");
		if (list == null || list.size() == 0) return null;
		
		return (ProcFlow[])list.toArray(new ProcFlow[0]);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.flow.ProcReader#get(int)
	 */
	public Proc get(int procID) throws E5Exception
	{
		Proc proc = getProc(procID);
		if (proc == null) proc = getUnflow(procID);
		
		return proc;
	}
}