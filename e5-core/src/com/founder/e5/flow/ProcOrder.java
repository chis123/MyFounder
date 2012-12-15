package com.founder.e5.flow;

import java.io.Serializable;

/**ÅÅÐò²Ù×÷Àà
 * @created 04-8-2005 15:05:25
 * @author Gong Lijie
 * @updated 2006-8-1 ²»¼Ì³ÐProc
 * @version 2.0
 */
public class ProcOrder implements Serializable{

	private static final long serialVersionUID = 7606539612873975011L;
	private int docTypeID;
	private int flowID;
	private int flowNodeID;
	private int procID;
	private int order;
	
	private int hashCode = Integer.MIN_VALUE;
	
	public String toString () {
		return (new StringBuffer()
				.append("[docTypeID:").append(docTypeID)
				.append(",flowID:").append(flowID)
				.append(",flowNodeID:").append(flowNodeID)
				.append(",procID:").append(procID)
				.append(",order:").append(order)
				.append("]")
				).toString();
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj)
	{
		if (null == obj) return false;
		if (!(obj instanceof ProcOrder)) return false;
		else {
			ProcOrder mObj = (ProcOrder) obj;
			return ((mObj.getDocTypeID() == docTypeID)
					&& (mObj.getFlowID() == flowID)
					&& (mObj.getFlowNodeID() == flowNodeID)
					&& (mObj.getProcID() == procID));
		}
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		if (Integer.MIN_VALUE == this.hashCode) 
		{
			StringBuffer sb = new StringBuffer(100);
			sb.append(docTypeID).append(':')
				.append(flowID).append(':')
				.append(flowNodeID).append(':')
				.append(procID).append(':');
			this.hashCode = sb.toString().hashCode();
		}
		return this.hashCode;
	}

	public int getFlowID()
	{
		return flowID;
	}
	public void setFlowID(int flowID)
	{
		this.flowID = flowID;
	}
	public int getFlowNodeID()
	{
		return flowNodeID;
	}
	public void setFlowNodeID(int flowNodeID)
	{
		this.flowNodeID = flowNodeID;
	}
	public int getProcID()
	{
		return procID;
	}
	public void setProcID(int procID)
	{
		this.procID = procID;
	}
	/**
	 * @return Returns the docTypeID.
	 */
	public int getDocTypeID()
	{
		return docTypeID;
	}
	/**
	 * @param docTypeID The docTypeID to set.
	 */
	public void setDocTypeID(int docTypeID)
	{
		this.docTypeID = docTypeID;
	}
	/**
	 * @return Returns the order.
	 */
	public int getOrder()
	{
		return order;
	}
	/**
	 * @param order The order to set.
	 */
	public void setOrder(int order)
	{
		this.order = order;
	}
}