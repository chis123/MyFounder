package com.founder.e5.flow;

import com.founder.e5.context.E5Exception;

/**
 * @created 04-8-2005 14:14:34
 * @author Gong Lijie
 * @version 1.0
 */
public interface FlowManager extends FlowReader {

	/**
	 * 增加流程
	 * @param docTypeID 文档类型ID
	 * @param flow
	 */
	public void create(int docTypeID, Flow flow)
	throws E5Exception;

	/**
	 * 保存某流程的修改
	 * 
	 * @param flow
	 */
	public void update(Flow flow)
	throws E5Exception;

	/**
	 * 删除流程
	 * 删除流程时，同时要删除所有流程节点、操作、排序
	 * 
	 * @param flowID
	 */
	public void deleteFlow(int flowID)
	throws E5Exception;

	/**
	 * 附加一个流程节点
	 * 
	 * @param flowNode
	 */
	public void append(int flowID, FlowNode flowNode)
	throws E5Exception;

	/**
	 * 在某位置前插入一个流程节点
	 * @param flowID 流程ID
	 * @param flowNode 节点
	 * @param position 插入位置（之后的节点ID）
	 */
	public void insert(int flowID, FlowNode flowNode, int position)
	throws E5Exception;

	/**
	 * 修改流程节点
	 * 
	 * @param flowNode
	 */
	public void update(FlowNode flowNode)
	throws E5Exception;

	/**
	 * 删除一个流程节点
	 * 同时要删除流程节点下的所有操作以及排序，并且把前后节点串起来
	 * 
	 * @param flowNodeID
	 */
	public void deleteFlowNode(int flowNodeID)
	throws E5Exception;

	/**
	 * 删除文档类型时的流程部分清理工作
	 * 自动删除它的所有流程、流程节点，操作，排序
	 * 
	 * @param docTypeID
	 */
	public void deleteDocType(int docTypeID)
	throws E5Exception;

}