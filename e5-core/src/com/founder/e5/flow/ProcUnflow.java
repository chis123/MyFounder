package com.founder.e5.flow;

/**
 * �����̲�����
 * @created 04-8-2005 14:16:45
 * @author Gong Lijie
 * @version 1.0
 */
public class ProcUnflow extends Proc {

	private int docTypeID;

	public String toString () {
		return (new StringBuffer()
				.append(super.toString())
				.append("[docTypeID:").append(docTypeID)
				.append("]")
				).toString();
	}
	//���췽�������ò������͹̶�ΪUNFLOW
	public ProcUnflow()
	{
		this.procType = Proc.PROC_UNFLOW;
	}
	/**
	 * @return Returns the docTypeID.
	 */
	public int getDocTypeID()
	{
		return docTypeID;
	}
	/**
	 * �ĵ�����
	 * ֻ�ڵ�һ�δ���ʱ��ֵ�������޸�
	 * @param docTypeID �ĵ�����ID
	 */
	public void setDocTypeID(int docTypeID)
	{
		this.docTypeID = docTypeID;
	}
}