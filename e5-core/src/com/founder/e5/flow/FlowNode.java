package com.founder.e5.flow;

import java.util.Observable;


/**
 * 流程节点类
 * @created 2005-8-4
 * @author Gong Lijie
 * @version 1.0
 * 
 * @updated 2006-7-31
 * @description 把操作DO/GO/BACK从流程节点中剔除，单独管理
 * @author Gong Lijie
 */
public class FlowNode 
extends Observable
//该类继承了Observable，用途是当增加流程节点时，通知排序操作把所有非流程操作加入该节点的排序操作中
{
	private int ID;
	private String name;
	private int flowID;
	private String description;
	private String waitingStatus;
	private String doingStatus;
	private String doneStatus;

	/**前面的流程节点ID，0表示当前是第一个节点*/
	private int preNodeID;
	/**下面的流程节点ID，0表示当前是最后一个节点*/
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
		//当要通知时才注册Observer，而不是在构造方法中注册，
		//避免一般性操作中的注册行为
		addObserver(ProcOrderObserver.getInstance());
		setChanged();
		super.notifyObservers(arg);
	}
}