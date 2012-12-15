package com.founder.e5.flow;

import com.founder.e5.context.E5Exception;

/**
 * 操作组读取器
 * @author Gong Lijie
 * @created on 2008-4-29
 * @version 1.0
 */
public interface ProcGroupReader {

	/**
	 * 取操作组
	 * <br/>取某一个流程节点上的操作组，或者某一个文档类型的非流程操作的组
	 * <br/>当flowNodeID是0时，取的是相应文档类型下的非流程操作的组
	 * @param docTypeID 文档类型ID
	 * @param flowNodeID 流程节点ID
	 */
	public ProcGroup[] getProcGroups(int docTypeID, int flowNodeID)
	throws E5Exception;
}
