package com.founder.e5.flow;

import java.util.ArrayList;
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
 * @created on 2005-8-4
 * @author Gong Lijie
 * @version 1.0
 */
class FlowManagerImpl implements FlowManager {

	/* (non-Javadoc)
	 * @see com.founder.e5.flow.FlowManager#create(int, com.founder.e5.flow.Flow)
	 */
	public void create(int docTypeID, Flow flow) throws E5Exception
	{
		flow.setDocTypeID(docTypeID);
		int id = (int)EUID.getID("FlowID");
		create(flow, id);
	}
	void create(Flow flow, int id) 
	throws E5Exception
	{
		flow.setID(id);
		try
		{
			BaseDAO dao = new BaseDAO();
			dao.save(flow);
		}
		catch (HibernateException e)
		{
			throw new E5Exception("[FlowCreate]", e);
		}
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.flow.FlowManager#update(com.founder.e5.flow.WorkFlow)
	 */
	public void update(Flow flow) throws E5Exception
	{
		BaseDAO dao = new BaseDAO();
		Session s = null;
		Transaction t = null;
		try
		{
			try
			{
				s = dao.getSession();
				t = dao.beginTransaction(s);
				update(flow, s);
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
			throw new E5Exception("[FlowUpdate]", e);
		}
	}
	void update(Flow flow, Session s) throws HibernateException
	{
		s.update(flow);
	}
	/* (non-Javadoc)
	 * @see com.founder.e5.flow.FlowManager#deleteFlow(int)
	 */
	public void deleteFlow(int flowID) throws E5Exception
	{
		BaseDAO dao = new BaseDAO();
		Session s = null;
		Transaction t = null;
		try
		{
			s = dao.getSession();
			t = dao.beginTransaction(s);
			//1.ɾ������
			DAOHelper.delete("delete from Flow f where f.ID=:id", 
					new Integer(flowID), Hibernate.INTEGER, s);
			//2.ɾ�����̽ڵ㣨ͬʱɾ��DO/BACK/GO��
			DAOHelper.delete("delete from FlowNode fn where fn.flowID=:id", 
					new Integer(flowID),Hibernate.INTEGER, s);
			//3.ɾ����ת����
			DAOHelper.delete("delete from ProcFlow f where f.flowID=:id", 
					new Integer(flowID), Hibernate.INTEGER, s);
			//4.ɾ�������������ʱ��ռ��2�����ݿ�����
			ProcOrderManager orderManager = FlowHelper.getProcOrderManager();
			orderManager.deleteFlow(flowID);
			t.commit();
		} catch (HibernateException e) {
			t.rollback();
			throw new E5Exception("[FlowDelete]", e);
		} finally {
			dao.closeSession(s);
		}
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.flow.FlowManager#append(int, com.founder.e5.flow.FlowNode)
	 */
	public void append(int flowID, FlowNode flowNode) throws E5Exception
	{
		//1.��ȡ�����̵����һ���ڵ㡣ע��Ҫ������һ���ٱ����½ڵ�
		FlowNode lastNode = getLastNode(flowID);
		
		BaseDAO dao = new BaseDAO();
		Session s = null;
		Transaction t = null;
		try
		{
			try
			{
				s = dao.getSession();
				t = dao.beginTransaction(s);
				//2.�����һ���ڵ����������������½ڵ�
				flowNode.setFlowID(flowID);
				flowNode.setNextNodeID(0);
				if (lastNode != null)
					flowNode.setPreNodeID(lastNode.getID());
				else
					flowNode.setPreNodeID(0);
				create(flowNode, s);
				//3.��ԭ�������ڵ�����½ڵ�
				if (lastNode != null)
				{
					lastNode.setNextNodeID(flowNode.getID());
					update(lastNode, s);
				}
				//4.������ԭ��û�нڵ㣬���������̵ĵ�һ�ڵ�
				else
				{
					Flow flow = getFlow(flowID);
					flow.setFirstFlowNodeID(flowNode.getID());
					update(flow, s);
				}
				t.commit();
			}
			catch (HibernateException e)
			{
				t.rollback();
				throw e;
			}
			catch (E5Exception e)
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
			throw new E5Exception("[FlowNodeAppend]", e);
		}

		/**
		 * ��֪ͨ�������������ݲ�����
		 * �������ͣ�ADD/UPDATE/DELETE)
		 */
		flowNode.notifyObservers(ProcOrderObserver.ADD);
	}
	
	private FlowNode getLastNode(int flowID) throws E5Exception
	{
		List list = DAOHelper.find("from FlowNode f where f.flowID=:flow and f.nextNodeID=0",
				new Integer(flowID),
				Hibernate.INTEGER);
		if (list.size() == 0) return null;
		
		return (FlowNode)list.get(0);
	}
	private void create(FlowNode flowNode, Session s) 
	throws HibernateException, E5Exception
	{
		int id = (int)EUID.getID("FlowNodeID");
		flowNode.setID(id);
		s.save(flowNode);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.flow.FlowManager#insert(int, com.founder.e5.flow.FlowNode, int)
	 */
	public void insert(int flowID, FlowNode flowNode, int position) 
	throws E5Exception
	{
		//1.��ȡ������ڵ�
		FlowNode nextNode = getFlowNode(position);
		if (nextNode == null)
			throw new E5Exception("Insert position invalid!");
		
		BaseDAO dao = new BaseDAO();
		Session s = null;
		Transaction t = null;
		try
		{
			try
			{
				s = dao.getSession();
				t = dao.beginTransaction(s);
				//2.�����ڵ����������������½ڵ�
				flowNode.setFlowID(flowID);
				flowNode.setPreNodeID(nextNode.getPreNodeID());
				flowNode.setNextNodeID(nextNode.getID());
				create(flowNode, s);

				//3.��ǰ��ڵ�����½ڵ�
				if (flowNode.getPreNodeID() != 0)
				{
					FlowNode preNode = getFlowNode(flowNode.getPreNodeID());
					preNode.setNextNodeID(flowNode.getID());
					update(preNode, s);
				}

				//4.�Ѻ���ڵ�����½ڵ�
				nextNode.setPreNodeID(flowNode.getID());
				update(nextNode, s);
				
				t.commit();
			}
			catch (HibernateException e)
			{
				t.rollback();
				throw e;
			}
			catch (E5Exception e)
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
			throw new E5Exception("[FlowNodeAppend]", e);
		}

		/**
		 * ��֪ͨ�������������ݲ�����
		 * �������ͣ�ADD/UPDATE/DELETE)
		 */
		flowNode.notifyObservers(ProcOrderObserver.ADD);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.flow.FlowManager#update(com.founder.e5.flow.FlowNode)
	 */
	public void update(FlowNode flowNode) throws E5Exception
	{
		BaseDAO dao = new BaseDAO();
		Session s = null;
		Transaction t = null;
		try
		{
			try
			{
				s = dao.getSession();
				t = dao.beginTransaction(s);
				update(flowNode, s);
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
			throw new E5Exception("[FlowNodeUpdate]", e);
		}
	}

	void update(FlowNode flowNode, Session s) throws HibernateException
	{
		s.update(flowNode);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.flow.FlowManager#deleteFlowNode(int)
	 */
	public void deleteFlowNode(int flowNodeID) throws E5Exception
	{
		FlowNode node = getFlowNode(flowNodeID);
		if (node == null) return;
		
		BaseDAO dao = new BaseDAO();
		Session s = null;
		Transaction t = null;
		try
		{
			try
			{
				s = dao.getSession();
				t = dao.beginTransaction(s);
				//1.���ǵ�һ�����̽ڵ㣬Ҫ�޸�����
				if (node.getPreNodeID() == 0)
				{
					Flow flow = getFlow(node.getFlowID());
					flow.setFirstFlowNodeID(node.getNextNodeID());
					update(flow, s);
				}
				//2.��ǰ��ڵ���������
				if (node.getPreNodeID() != 0)
				{
					FlowNode preNode = getPreFlowNode(flowNodeID);
					preNode.setNextNodeID(node.getNextNodeID());
					update(preNode, s);
				}
				if (node.getNextNodeID() != 0)
				{
					FlowNode nextNode = getNextFlowNode(flowNodeID);
					nextNode.setPreNodeID(node.getPreNodeID());
					update(nextNode, s);
				}
				//3.ɾ�����̽ڵ㣨ͬʱɾ��DO/BACK/GO��
				DAOHelper.delete("delete from FlowNode fn where fn.ID=:id", 
						new Integer(flowNodeID),Hibernate.INTEGER, s);
				//4.ɾ����ת����
				DAOHelper.delete("delete from ProcFlow f where f.flowNodeID=:id", 
						new Integer(flowNodeID),Hibernate.INTEGER, s);
				//5.���ɾ�������������ʱ��ռ��2�����ݿ�����
				ProcOrderManager orderManager = FlowHelper.getProcOrderManager();
				orderManager.deleteFlowNode(flowNodeID);

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
			throw new E5Exception("[FlowDelete]", e);
		}
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.flow.FlowManager#deleteDocType(int)
	 */
	public void deleteDocType(int docTypeID) throws E5Exception
	{
		/**
		 * ɾ���ĵ�����ʱɾ�����е�����
		 * ������һ���Ե�Ҫ�󲻸ߣ�������������������ݣ����Բ�����ͬһ������
		 * �ò������ٽ��У����ؿ������ݿ�Ч��
		 * 
		 */
		Flow[] arr = getFlows(docTypeID);
		if (arr == null) return;
		
		for (int i = 0; i < arr.length; i++)
			deleteFlow(arr[i].getID());
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.flow.FlowReader#getFlow(int)
	 */
	public Flow getFlow(int flowID) throws E5Exception
	{
		BaseDAO dao = new BaseDAO();
		try
		{
			return (Flow)dao.get(Flow.class, new Integer(flowID));
		}
		catch (HibernateException e)
		{
			throw new E5Exception("[FlowGet]", e);
		}
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.flow.FlowReader#getFlow(int, java.lang.String)
	 */
	public Flow getFlow(int docTypeID, String flowName) throws E5Exception
	{
		List list = DAOHelper.find("from Flow f where f.docTypeID=:type and f.name=:name",
					new String[]{"type", "name"}, 
					new Object[]{new Integer(docTypeID), flowName},
					new Type[]{Hibernate.INTEGER, Hibernate.STRING});
		if (list.size() == 0) return null;
		
		return (Flow)list.get(0);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.flow.FlowReader#getFlows(int)
	 */
	public Flow[] getFlows(int docTypeID) throws E5Exception
	{
		List list;
		if (docTypeID != 0)
			list = DAOHelper.find("from Flow f where f.docTypeID=:docType order by f.ID",
				new Integer(docTypeID),	Hibernate.INTEGER);
		else
			list = DAOHelper.find("from Flow f order by f.docTypeID, f.ID");
		if (list.size() == 0) return null;
		
		return (Flow[])list.toArray(new Flow[0]);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.flow.FlowReader#getFlowNode(int, java.lang.String)
	 */
	public FlowNode getFlowNode(int flowID, String flowNodeName) throws E5Exception
	{
		List list = DAOHelper.find("from FlowNode f where f.flowID=:flow and f.name=:name",
				new String[]{"flow", "name"},
				new Object[]{new Integer(flowID), flowNodeName},
				new Type[]{Hibernate.INTEGER, Hibernate.STRING});
		if (list.size() == 0) return null;
		
		return (FlowNode)list.get(0);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.flow.FlowReader#getFlowNodes(int)
	 */
	public FlowNode[] getFlowNodes(int flowID) throws E5Exception
	{
		List list;
		if (flowID != 0)
			list = DAOHelper.find("from FlowNode f where f.flowID=:flow order by f.flowID, f.ID",
				new Integer(flowID),
				Hibernate.INTEGER);
		else
			list = DAOHelper.find("from FlowNode f order by f.flowID, f.ID");
		
		int size = list.size();
		if (size == 0) return null;
		
		FlowNode[] nodeArr = new FlowNode[size];
		list.toArray(nodeArr);
		
		List orderList = new ArrayList(size);
		FlowNode node;
		for (int i = 0; i < size; i++)
		{
			if (nodeArr[i].getPreNodeID() == 0)
			{
				node = nodeArr[i];
				int nextID;
				while (node != null)
				{
					orderList.add(node);
					nextID = node.getNextNodeID();
					
					node = null;
					for (int j = 0; j < size; j++)
					{
						if (nodeArr[j].getID() == nextID)
							node = nodeArr[j];
					}
				}
			}
		}
		FlowNode[] orderArr = new FlowNode[orderList.size()];
		orderList.toArray(orderArr);
		
		return orderArr;
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.flow.FlowReader#getFlowNode(int)
	 */
	public FlowNode getFlowNode(int flowNodeID) throws E5Exception
	{
		BaseDAO dao = new BaseDAO();
		try
		{
			return (FlowNode)dao.get(FlowNode.class, new Integer(flowNodeID));
		}
		catch (HibernateException e)
		{
			throw new E5Exception("[FlowNodeGet]", e);
		}
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.flow.FlowReader#getPreFlowNode(int)
	 */
	public FlowNode getPreFlowNode(int flowNodeID) throws E5Exception
	{
		FlowNode node = getFlowNode(flowNodeID);
		if (node == null) return null;
		
		return getFlowNode(node.getPreNodeID());		
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.flow.FlowReader#getNextFlowNode(int)
	 */
	public FlowNode getNextFlowNode(int flowNodeID) throws E5Exception
	{
		FlowNode node = getFlowNode(flowNodeID);
		if (node == null) return null;
		
		return getFlowNode(node.getNextNodeID());		
	}
}