package com.founder.e5.flow;

/**
 * 非流程操作类
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
	//构造方法中设置操作类型固定为UNFLOW
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
	 * 文档类型
	 * 只在第一次创建时赋值，请勿修改
	 * @param docTypeID 文档类型ID
	 */
	public void setDocTypeID(int docTypeID)
	{
		this.docTypeID = docTypeID;
	}
}