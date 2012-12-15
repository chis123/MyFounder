package com.founder.e5.flow;

import com.founder.e5.context.E5Exception;

/**
 * 流程信息读取器
 * @created 04-6-2005 14:14:20
 * @author Gong Lijie
 * @version 1.0
 */
public interface FlowReader {

	/**
	 * 根据流程ID得到一个流程
	 * 
	 * @param flowID    流程ID
	 */
	public Flow getFlow(int flowID)
	throws E5Exception;

	/**
	 * 根据文档类型ID和流程名得到流程
	 * 
	 * @param docTypeID    文档类型ID
	 * @param flowName    流程名
	 */
	public Flow getFlow(int docTypeID, String flowName)
	throws E5Exception;

	/**
	 * 取某文档类型的所有流程
	 * 
	 * @param docTypeID    文档类型ID
	 * <br/>当传入的参数是0时，取出所有的流程
	 */
	public Flow[] getFlows(int docTypeID)
	throws E5Exception;

	/**
	 * 按流程ID和流程节点名称取一个流程节点
	 * 
	 * @param flowID    流程ID
	 * @param flowNodeName    流程节点名
	 */
	public FlowNode getFlowNode(int flowID, String flowNodeName)
	throws E5Exception;

	/**
	 * 取一个流程的所有流程节点，按节点的前后顺序排列
	 * @param flowID
	 * <br/>当传入的参数是0时，取出所有的流程节点
	 */
	public FlowNode[] getFlowNodes(int flowID)
	throws E5Exception;
	
	/**
	 * 取一个流程节点
	 * 
	 * @param flowNodeID
	 */
	public FlowNode getFlowNode(int flowNodeID)
	throws E5Exception;

	/**
	 * 取某流程节点的前面的节点
	 * 
	 * @param flowNodeID
	 */
	public FlowNode getPreFlowNode(int flowNodeID)
	throws E5Exception;

	/**
	 * 取某流程节点的后面节点
	 * 
	 * @param flowNodeID
	 */
	public FlowNode getNextFlowNode(int flowNodeID)
	throws E5Exception;

}