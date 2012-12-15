package com.founder.e5.flow;

import java.util.Observable;
import java.util.Observer;

/**
 * ProcOrder��Observer
 * �������̽ڵ�����ӡ��Լ����̲����ͷ����̲���������ɾ����
 * ��Щ����¶���Ҫ�޸�����
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
	 * �õ���ģʽ��ʹObservable��ÿ�μ���ʱ������ֻ��һ��ProcOrderObserver
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
			//���ǽڵ㣬��һ���ǽڵ㴴��
			if (o instanceof FlowNode)
				manager.addFlowNode((FlowNode)o);
			else if (!(o instanceof Proc))
				throw new Exception("Unknown Type:" + o.getClass().getName());
			else
			{
				Proc proc = (Proc)o;
				//���Ӳ���
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
