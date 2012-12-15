package com.founder.e5.flow;

import com.founder.e5.context.E5Exception;

/**
 * @created 04-8-2005 15:05:30
 * @author Gong Lijie
 * @version 1.0
 */
public interface ProcOrderManager extends ProcOrderReader {

	/**
	 * 当增加一个流程节点时，自动把所有非流程操作加到它的排序操作
	 * <BR>该操作不应该直接调用，而应该由增加流程节点的部分进行控制
	 * @param node
	 * @throws E5Exception 没有找到对应的流程时抛出代码1003的异常
	 */
	public void addFlowNode(FlowNode node)
	throws E5Exception;

	/**
	 * 增加一个流程操作时，附加其排序操作
	 * <BR>该操作不应该直接调用，而应该由增加流程操作的部分进行控制

	 * <BR>对于流程操作，只要在对应的流程节点上增加排序操作即可
	 * <BR>对于非流程操作，需要在文档类型下增加非流程排序，并且在文档类型的所有流程的每个流程节点上增加排序
	 * 
	 * <BR>使用一个Proc类型的参数，包括下面的信息：
	 * <BR>操作类型、文档类型（只对非流程操作有效）、流程信息（只对流程操作有效）、
	 * <BR>操作ID（只对非流程操作和跳转操作有效）、其他基本信息
	 * @param proc
	 */
	public void append(Proc proc)
	throws E5Exception;

	/**
	 * 删除一个流程操作时，同时删除其排序操作
	 * <BR>该操作不应该直接调用，而应该由删除流程操作的部分进行控制
	 * @param proc
	 */
	public void delete(Proc proc)
	throws E5Exception;

	/**
	 * 删除文档类型时把所有相关排序操作删除
	 * 
	 * @param docTypeID
	 */
	public void deleteDocType(int docTypeID)
	throws E5Exception;
	/**
	 * 删除流程时把所有相关排序操作删除
	 * 
	 * @param flowID
	 */
	public void deleteFlow(int flowID)
	throws E5Exception;

	/**
	 * 删除流程节点时把所有相关排序操作删除
	 * 
	 * @param flowNodeID
	 */
	public void deleteFlowNode(int flowNodeID)
	throws E5Exception;

	/**
	 * 取出全部的排序操作
	 * <BR>排序方式：文档类型、流程、流程节点、顺序
	 */
	public ProcOrder[] getAll()
	throws E5Exception;

	/**
	 * 重新设置某条件下的所有操作顺序
	 * <BR>数组顺序决定了操作的排序
	 * <BR>重置时，先按照docTypeID/flowID/flowNodeID，把原来的排序删除
	 * @param docTypeID
	 * @param flowID
	 * @param flowNodeID
	 * @param procs
	 * @throws E5Exception
	 */
	public void reset(int docTypeID, int flowID, int flowNodeID, Proc[] procs)
	throws E5Exception;
	/**
	 * 修改一个操作时，同时修改其排序操作
	 * <BR>该操作不应该直接调用，而应该由修改操作的部分进行控制
	 * @param proc
	 */
	public void update(Proc proc)
	throws E5Exception;

}