package com.founder.e5.flow;

import java.util.Observable;
import java.util.Observer;

/**
 * ProcOrder的Observer
 * 用于流程节点的增加、以及流程操作和非流程操作的增、删、改
 * 这些情况下都需要修改排序
 * @created on 2005-8-9
 * @author Gong Lijie
 * @version 1.0
 */
class ProcOrderObserver implements Observer
{
	static Integer ADD = new Integer(1);
	static Integer UPDATE = new Integer(2);
	static Integer DELETE = new Integer(3);
	
	private static ProcOrderObserver instance = null;
	private ProcOrderObserver()
	{
	}
	
	/**
	 * 用单例模式，使Observable类每次加入时都保持只有一个ProcOrderObserver
	 * @return
	 */
	static synchronized ProcOrderObserver getInstance()
	{
		if (instance == null)
			instance = new ProcOrderObserver();
		return instance;
	}
	/* (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	public void update(Observable o, Object arg)
	{
		ProcOrderManager manager = FlowHelper.getProcOrderManager();
		try
		{
			//若是节点，则一定是节点创建
			if (o instanceof FlowNode)
				manager.addFlowNode((FlowNode)o);
			else if (!(o instanceof Proc))
				throw new Exception("Unknown Type:" + o.getClass().getName());
			else
			{
				Proc proc = (Proc)o;
				//增加操作
				Integer opType = (Integer)arg; 
				if (opType.equals(ADD))
					manager.append(proc);
				else if (opType.equals(DELETE))
					manager.delete(proc);
				else
					manager.update(proc);
			}
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}
}
