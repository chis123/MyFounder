package com.founder.e5.flow;

import com.founder.e5.context.E5Exception;

/**
 * 排序操作读取器
 * @created 04-8-2005 15:05:35
 * @author Gong Lijie
 * @version 1.0
 */
public interface ProcOrderReader {

	/**
	 * 取排序后的操作<br/>
	 * 为了兼容性保留了这个方法，实际上等同于接口中的另外一个方法。<br/>
	 * 其flowID参数没有用处，固定传入0即可。
	 * @param docTypeID 文档类型ID
	 * @param flowID	流程ID
	 * @param flowNodeID 流程节点ID
	 */
	public ProcOrder[] getProcOrders(int docTypeID, int flowID, int flowNodeID)
	throws E5Exception;

	/**
	 * 取排序后的操作
	 * <br/>取某一个流程节点上的排序后操作，或者某一个文档类型的非流程排序操作
	 * <br/>当flowNodeID是0时，取的是相应文档类型下的非流程操作的排序结果
	 * @param docTypeID 文档类型ID
	 * @param flowNodeID 流程节点ID
	 */
	public ProcOrder[] getProcOrders(int docTypeID, int flowNodeID)
	throws E5Exception;
}