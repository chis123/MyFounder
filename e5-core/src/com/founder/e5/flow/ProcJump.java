package com.founder.e5.flow;

/**
 * ��ת������
 * <br/>����ProcFlow��
 * @deprecated 2006-8-2
 * @created 04-8-2005 14:16:31
 * @author Gong Lijie
 * @version 1.0
 */
public class ProcJump extends Proc 
{
	private int nextFlowID;//��ת������ID
	private int nextFlowNodeID;//��ת�����̽ڵ�ID
	
	public String toString () {
		return (new StringBuffer()
				.append(super.toString())
				.append("[nextFlowID:").append(nextFlowID)
				.append(",nextFlowNodeID:").append(nextFlowNodeID)
				.append("]")
				).toString();
	}
	/**
	 * ���췽�������ò������͹̶�ΪJUMP
	 */
	public ProcJump()
	{
		this.procType = Proc.PROC_JUMP;
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