package com.founder.e5.flow;

import java.util.ArrayList;
import java.util.List;

import com.founder.e5.context.E5Exception;

/**
 * @created 04-8-2005 14:15:25
 * @author Gong Lijie
 * @version 1.0
 */
class FlowReaderImpl implements FlowReader {

	/* (non-Javadoc)
	 * @see com.founder.e5.flow.FlowReader#getFlow(int)
	 */
	public Flow getFlow(int flowID) throws E5Exception
	{
		Flow[] arr = getFlows();
		if (arr != null)
		{
			for (int i = 0; i < arr.length; i++)
			{
				if (arr[i].getID() == flowID)
					return arr[i];
			}
			return null;
		}
		else
			return FlowHelper.getFlowManager().getFlow(flowID);
	}
	
	/* (non-Javadoc)
	 * @see com.founder.e5.flow.FlowReader#getFlow(int, java.lang.String)
	 */
	public Flow getFlow(int docTypeID, String flowName) throws E5Exception
	{
		Flow[] arr = getFlows();
		if (arr != null)
		{
			for (int i = 0; i < arr.length; i++)
			{
				if ((arr[i].getDocTypeID() == docTypeID)
						&& (flowName.equals(arr[i].getName())))
					return arr[i];
			}
			return null;
		}
		else
			return FlowHelper.getFlowManager().getFlow(docTypeID, flowName);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.flow.FlowReader#getFlows(int)
	 */
	public Flow[] getFlows(int docTypeID) throws E5Exception
	{
		if (docTypeID == 0) return getFlows();
		
		List list = new ArrayList();
		Flow[] arr = getFlows();
		if (arr != null)
			for (int i = 0; i < arr.length; i++)
				if (arr[i].getDocTypeID() == docTypeID)
					list.add(arr[i]);
		if (list.size() == 0)
			 return null;
		return (Flow[])list.toArray(new Flow[0]);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.flow.FlowReader#getFlowNode(int, java.lang.String)
	 */
	public FlowNode getFlowNode(int flowID, String flowNodeName) throws E5Exception
	{
		if (flowNodeName == null) return null;
		
		FlowNode[] arr = getFlowNodes();
		if (arr != null)
		{
			for (int i = 0; i < arr.length; i++)
			{
				if ((arr[i].getFlowID() == flowID)
						&& (flowNodeName.equals(arr[i].getName())))
					return arr[i];
			}
			return null;
		}
		else
			return FlowHelper.getFlowManager().getFlowNode(flowID, flowNodeName);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.flow.FlowReader#getFlowNodes(int)
	 */
	public FlowNode[] getFlowNodes(int flowID) throws E5Exception
	{
		if (flowID == 0) return getFlowNodes();
		
		Flow flow = getFlow(flowID);
		if (flow == null) return null;
		
		int firstFlowNodeID = flow.getFirstFlowNodeID();
		if (firstFlowNodeID < 1) return null;
		
		FlowNode node  = getFlowNode(firstFlowNodeID);
		if (node == null) return null;
		
		List list = new ArrayList();
		while (true)
		{
			list.add(node);
			int nodeID = node.getNextNodeID();
			if (nodeID < 1) break;
			
			node = getFlowNode(nodeID);
			if (node == null) break;
		}
		return (FlowNode[])list.toArray(new FlowNode[0]);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.flow.FlowReader#getFlowNode(int)
	 */
	public FlowNode getFlowNode(int flowNodeID) throws E5Exception
	{
		FlowNode[] arr = getFlowNodes();
		if (arr != null)
		{
			for (int i = 0; i < arr.length; i++)
			{
				if (arr[i].getID() == flowNodeID)
					return arr[i];
			}
			return null;
		}
		else
			return FlowHelper.getFlowManager().getFlowNode(flowNodeID);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.flow.FlowReader#getPreFlowNode(int)
	 */
	public FlowNode getPreFlowNode(int flowNodeID) throws E5Exception
	{
		FlowNode node = getFlowNode(flowNodeID);
		if (node == null) return null;

		return getFlowNode(node.getPreNodeID());
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.flow.FlowReader#getNextFlowNode(int)
	 */
	public FlowNode getNextFlowNode(int flowNodeID) 
	throws E5Exception
	{
		FlowNode node = getFlowNode(flowNodeID);
		if (node == null) return null;

		return getFlowNode(node.getNextNodeID());
	}
	private Flow[] getFlows()
	{
		FlowCache cache = FlowHelper.getFlowCache();
		if (cache != null)
			return cache.getFlows();
		return null;
	}
	private FlowNode[] getFlowNodes()
	{
		FlowCache cache = FlowHelper.getFlowCache();
		if (cache != null)
			return cache.getNodes();
		return null;
	}
}