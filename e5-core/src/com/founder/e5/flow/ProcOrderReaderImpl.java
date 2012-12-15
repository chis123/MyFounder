package com.founder.e5.flow;

import java.util.List;
import java.util.ArrayList;

import com.founder.e5.context.E5Exception;

/**
 * @created on 2005-8-8
 * @author Gong Lijie
 * @version 1.0
 */
class ProcOrderReaderImpl implements ProcOrderReader
{

	/* (non-Javadoc)
	 * @see com.founder.e5.flow.ProcOrderReader#getProcs(int, int, int)
	 */
	public ProcOrder[] getProcOrders(int docTypeID, int flowID, int flowNodeID)
			throws E5Exception
	{
		return getProcOrders(docTypeID, flowNodeID);
	}

	public ProcOrder[] getProcOrders(int docTypeID, int flowNodeID) throws E5Exception
	{
		FlowCache cache = FlowHelper.getFlowCache();
		if (cache == null) return null;
		
		ProcOrder[] orders = cache.getOrders();
		if (orders == null) return null;
		
		List list = new ArrayList();
		for (int i = 0; i < orders.length; i++)
		{
			if ( (orders[i].getDocTypeID() == docTypeID)
					&& (orders[i].getFlowNodeID() == flowNodeID))
				list.add(orders[i]);
			//数组是经过排序的，若发现有>提供参数的，则后面都不会有匹配的，break
			else if ( (orders[i].getDocTypeID() > docTypeID))
				break;
		}
		if (list.size() == 0) return null;
		return (ProcOrder[])list.toArray(new ProcOrder[0]);
	}
}
