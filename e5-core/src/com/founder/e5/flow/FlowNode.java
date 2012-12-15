package com.founder.e5.flow;

import java.util.Observable;


/**
 * ���̽ڵ���
 * @created 2005-8-4
 * @author Gong Lijie
 * @version 1.0
 * 
 * @updated 2006-7-31
 * @description �Ѳ���DO/GO/BACK�����̽ڵ����޳�����������
 * @author Gong Lijie
 */
public class FlowNode 
extends Observable
//����̳���Observable����;�ǵ��������̽ڵ�ʱ��֪ͨ������������з����̲�������ýڵ�����������
{
	private int ID;
	private String name;
	private int flowID;
	private String description;
	private String waitingStatus;
	private String doingStatus;
	private String doneStatus;

	/**ǰ������̽ڵ�ID��0��ʾ��ǰ�ǵ�һ���ڵ�*/
	private int preNodeID;
	/**��������̽ڵ�ID��0��ʾ��ǰ�����һ���ڵ�*/
	private int nextNodeID;

	/**
	 * @return Returns the description.
	 */
	public String getDescription()
	{
		return description;
	}
	/**
	 * @param description The description to set.
	 */
	public void setDescription(String description)
	{
		this.description = description;
	}
	/**
	 * @return Returns the doingStatus.
	 */
	public String getDoingStatus()
	{
		return doingStatus;
	}
	/**
	 * @param doingStatus The doingStatus to set.
	 */
	public void setDoingStatus(String doingStatus)
	{
		this.doingStatus = doingStatus;
	}
	/**
	 * @return Returns the doneStatus.
	 */
	public String getDoneStatus()
	{
		return doneStatus;
	}
	/**
	 * @param doneStatus The doneStatus to set.
	 */
	public void setDoneStatus(String doneStatus)
	{
		this.doneStatus = doneStatus;
	}
	/**
	 * @return Returns the flowID.
	 */
	public int getFlowID()
	{
		return flowID;
	}
	/**
	 * @param flowID The flowID to set.
	 */
	public void setFlowID(int flowID)
	{
		this.flowID = flowID;
	}
	/**
	 * @return Returns the iD.
	 */
	public int getID()
	{
		return ID;
	}
	/**
	 * @param id The iD to set.
	 */
	void setID(int id)
	{
		ID = id;
	}
	/**
	 * @return Returns the name.
	 */
	public String getName()
	{
		return name;
	}
	/**
	 * @param name The name to set.
	 */
	public void setName(String name)
	{
		this.name = name;
	}
	/**
	 * @return Returns the nextNodeID.
	 */
	public int getNextNodeID()
	{
		return nextNodeID;
	}
	/**
	 * @param nextNodeID The nextNodeID to set.
	 */
	public void setNextNodeID(int nextNodeID)
	{
		this.nextNodeID = nextNodeID;
	}
	/**
	 * @return Returns the preNodeID.
	 */
	public int getPreNodeID()
	{
		return preNodeID;
	}
	/**
	 * @param preNodeID The preNodeID to set.
	 */
	public void setPreNodeID(int preNodeID)
	{
		this.preNodeID = preNodeID;
	}
	/**
	 * @return Returns the waitingStatus.
	 */
	public String getWaitingStatus()
	{
		return waitingStatus;
	}
	/**
	 * @param waitingStatus The waitingStatus to set.
	 */
	public void setWaitingStatus(String waitingStatus)
	{
		this.waitingStatus = waitingStatus;
	}
	
	/* (non-Javadoc)
	 * @see java.util.Observable#notifyObservers(java.lang.Object)
	 */
	public void notifyObservers(Object arg)
	{
		//��Ҫ֪ͨʱ��ע��Observer���������ڹ��췽����ע�ᣬ
		//����һ���Բ����е�ע����Ϊ
		addObserver(ProcOrderObserver.getInstance());
		setChanged();
		super.notifyObservers(arg);
	}
}