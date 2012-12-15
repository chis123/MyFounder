package com.founder.e5.flow;

/**
 * ���̲�����
 * @created 2005-8-4
 * @updated 2006-8-1
 * @author Gong Lijie
 * @version 2.0
 */
public class ProcFlow extends Proc 
{
	private int flowID;			//����ID
	private int flowNodeID;		//���̽ڵ�ID
	private int nextFlowID;		//��ת������ID
	private int nextFlowNodeID;	//��ת�����̽ڵ�ID
	
	public String toString () {
		return (new StringBuffer()
				.append(super.toString())
				.append("[flowID:").append(flowID)
				.append(",flowNodeID:").append(flowNodeID)
				.append(",nextFlowID:").append(nextFlowID)
				.append(",nextFlowNodeID:").append(nextFlowNodeID)
				.append("]")
				).toString();
	}
	
	public int getFlowID()
	{
		return flowID;
	}

	public void setFlowID(int flowID)
	{
		this.flowID = flowID;
	}

	/**
	 * @return Returns the flowNodeID.
	 */
	public int getFlowNodeID()
	{
		return flowNodeID;
	}
	/**
	 * �������̽ڵ�ID
	 * <BR>���̽ڵ�IDֻ�����½�ʱ����һ�Σ������޸�
	 * @param flowNodeID The flowNodeID to set.
	 */
	public void setFlowNodeID(int flowNodeID)
	{
		this.flowNodeID = flowNodeID;
	}
	/**
	 * @return Returns the nextFlowID.
	 */
	public int getNextFlowID()
	{
		return nextFlowID;
	}
	/**
	 * @param nextFlowID The nextFlowID to set.
	 */
	public void setNextFlowID(int nextFlowID)
	{
		this.nextFlowID = nextFlowID;
	}
	/**
	 * @return Returns the nextFlowNodeID.
	 */
	public int getNextFlowNodeID()
	{
		return nextFlowNodeID;
	}
	/**
	 * @param nextFlowNodeID The nextFlowNodeID to set.
	 */
	public void setNextFlowNodeID(int nextFlowNodeID)
	{
		this.nextFlowNodeID = nextFlowNodeID;
	}
}