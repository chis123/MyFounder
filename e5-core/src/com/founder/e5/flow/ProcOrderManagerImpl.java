package com.founder.e5.flow;

import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.founder.e5.context.BaseDAO;
import com.founder.e5.context.DAOHelper;
import com.founder.e5.context.E5Exception;

/**
 * @created on 2005-8-8
 * @updated on 2006-8-1 流程操作变化
 * @author Gong Lijie
 * @version 1.0
 */
class ProcOrderManagerImpl implements ProcOrderManager
{
	/* (non-Javadoc)
	 * @see com.founder.e5.flow.ProcOrderManager#addFlowNode(com.founder.e5.flow.FlowNode)
	 */
	public void addFlowNode(FlowNode node) throws E5Exception
	{
		//1.根据流程节点的流程ID，得到对应的文档类型ID
		int flowID = node.getFlowID();
		FlowManager flowManager = FlowHelper.getFlowManager();
		Flow flow = flowManager.getFlow(flowID);
		if (flow == null)
			throw new E5Exception(1003, "Flow not found!" + node.toString());
		int docTypeID = flow.getDocTypeID();

		//2.得到文档类型的所有非流程操作
		ProcManager procManager = FlowHelper.getProcManager();
		ProcUnflow[] unflows = procManager.getUnflows(docTypeID);
		//3.加入
		reset(docTypeID, flowID, node.getID(), unflows);
	}
	
	/* (non-Javadoc)
	 * @see com.founder.e5.flow.ProcOrderManager#append(com.founder.e5.flow.Proc)
	 */
	public void append(Proc proc) throws E5Exception
	{
		if (proc.getProcType() == Proc.PROC_UNFLOW)
			appendUnflow((ProcUnflow)proc);
		else
			appendFlowProc((ProcFlow)proc);
	}
	/* (non-Javadoc)
	 * @see com.founder.e5.flow.ProcOrderManager#delete(com.founder.e5.flow.Proc)
	 */
	public void delete(Proc proc) throws E5Exception
	{
		DAOHelper.delete("delete from ProcOrder where procID=?", 
				new Object[]{new Integer(proc.getProcID())});
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.flow.ProcOrderManager#deleteDocType(int)
	 */
	public void deleteDocType(int docTypeID) throws E5Exception
	{
		if (docTypeID < 1) return;
		
		DAOHelper.delete("delete from ProcOrder as po where po.docTypeID=:id", 
				new Integer(docTypeID), 
				Hibernate.INTEGER);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.flow.ProcOrderManager#deleteFlow(int)
	 */
	public void deleteFlow(int flowID) throws E5Exception
	{
		if (flowID < 1) return;
		
		DAOHelper.delete("delete from ProcOrder as po where po.flowID=:id", 
				new Integer(flowID), 
				Hibernate.INTEGER);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.flow.ProcOrderManager#deleteFlowNode(int)
	 */
	public void deleteFlowNode(int flowNodeID) throws E5Exception
	{
		if (flowNodeID < 1) return;
		
		DAOHelper.delete("delete from ProcOrder as po where po.flowNodeID=:id", 
				new Integer(flowNodeID), 
				Hibernate.INTEGER);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.flow.ProcOrderManager#getAll()
	 */
	public ProcOrder[] getAll() throws E5Exception
	{
		List list = DAOHelper.find("from ProcOrder as po order by po.docTypeID, po.flowNodeID, po.order");
		if (list.size() == 0) return null;
		
		return (ProcOrder[])list.toArray(new ProcOrder[0]);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.flow.ProcOrderManager#reset(com.founder.e5.flow.ProcOrder[])
	 */
	public void reset(int docTypeID, int flowID, int flowNodeID, Proc[] procs) throws E5Exception
	{
		if (procs == null) return;
		
		Session s = null;
		Transaction t = null;
		BaseDAO dao = new BaseDAO();		
		try
		{
			s = dao.getSession();
			t = dao.beginTransaction(s);
			//先删除旧的排序
			DAOHelper.delete("delete from ProcOrder as p where p.docTypeID=? and p.flowNodeID=?",
					new Object[]{new Integer(docTypeID), new Integer(flowNodeID)}, s);

			//一个一个增加，顺序就是数组的顺序
			for (int i = 0; i < procs.length; i++)
			{
				ProcOrder order = new ProcOrder();
				order.setDocTypeID(docTypeID);
				order.setFlowID(flowID);
				order.setFlowNodeID(flowNodeID);
				order.setProcID(procs[i].getProcID());
				order.setOrder(i);
				
				s.save(order);
			}
			t.commit();
		}
		catch (HibernateException e)
		{
			t.rollback();
			throw new E5Exception("[ProcOrderReset]", e);
		}
		finally
		{
			dao.closeSession(s);
		}
	}
	/* (non-Javadoc)
	 * @see com.founder.e5.flow.ProcOrderManager#update(com.founder.e5.flow.Proc)
	 */
	public void update(Proc proc) throws E5Exception
	{
		//修改时不需要修改ProcOrder
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.flow.ProcOrderReader#getProcs(int, int, int)
	 */
	public ProcOrder[] getProcOrders(int docTypeID, int flowID, int flowNodeID) throws E5Exception
	{
		return getProcOrders(docTypeID, flowNodeID);
	}

	/**
	 * 增加一个非流程操作
	 * 1.为文档类型增加排序操作
	 * 2.为文档类型下的每个流程的每个流程节点，都增加一个排序操作
	 * @param proc
	 * @throws E5Exception
	 */
	private void appendUnflow(ProcUnflow proc) throws E5Exception
	{
		int docTypeID = proc.getDocTypeID();

		ProcOrder order = new ProcOrder();
		order.setDocTypeID(docTypeID);
		order.setProcID(proc.getProcID());
		order.setFlowID(0);
		order.setFlowNodeID(0);
		appendOrder(order);

		FlowManager flowManager = FlowHelper.getFlowManager();
		Flow[] flows = flowManager.getFlows(docTypeID);
		if (flows == null) return;
		
		//对每个流程
		for (int i = 0; i < flows.length; i++)
		{
			FlowNode[] nodes = flowManager.getFlowNodes(flows[i].getID());
			if (nodes == null) continue;
			//对每个流程节点
			for (int j = 0; j < nodes.length; j++)
			{
				order.setFlowID(nodes[j].getFlowID());
				order.setFlowNodeID(nodes[j].getID());
				
				appendOrder(order);
			}
		}
	}

	/**
	 * 增加流程操作，只要按其流程节点ID增加一个即可
	 * @param proc
	 * @throws E5Exception
	 */
	private void appendFlowProc(ProcFlow proc) throws E5Exception
	{
		int nodeID = proc.getFlowNodeID();
		if (proc.getFlowID() == 0)
		{
			FlowNode node = FlowHelper.getFlowManager().getFlowNode(nodeID);
			proc.setFlowID(node.getFlowID());
		}
		int flowID = proc.getFlowID();
		Flow flow = FlowHelper.getFlowManager().getFlow(flowID);
		int docTypeID = flow.getDocTypeID();
		
		ProcOrder order = new ProcOrder();
		order.setProcID(proc.getProcID());
		order.setDocTypeID(docTypeID);
		order.setFlowID(flowID);
		order.setFlowNodeID(nodeID);
		
		appendOrder(order);
	}
	
	/**
	 * 给操作最后确定顺序，然后插入
	 * @param proc
	 * @throws E5Exception
	 */
	private void appendOrder(ProcOrder order) throws E5Exception
	{
		int nodeID = order.getFlowNodeID();
		int docTypeID = order.getDocTypeID();
		
		BaseDAO dao = new BaseDAO();
		try
		{
			List list = dao.find("from ProcOrder as p where p.docTypeID=? and p.flowNodeID=? order by p.order desc",
					new Object[]{new Integer(docTypeID), new Integer(nodeID)});
			if (list.size() == 0)
				order.setOrder(0);
			else
				order.setOrder(((ProcOrder)list.get(0)).getOrder() + 1);
			list.clear();
			dao.save(order);
		}
		catch (HibernateException e)
		{
			throw new E5Exception("[ProcOrderAppend]", e);
		}
	}

	public ProcOrder[] getProcOrders(int docTypeID, int flowNodeID) throws E5Exception
	{
		List list = DAOHelper.find("from ProcOrder as p where p.docTypeID=? and p.flowNodeID=? order by p.order",
				new Object[]{new Integer(docTypeID), new Integer(flowNodeID)});
		if (list.size() == 0) return null;
		
		return (ProcOrder[])list.toArray(new ProcOrder[0]);
	}
}
