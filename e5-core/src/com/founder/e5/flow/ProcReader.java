package com.founder.e5.flow;

import com.founder.e5.context.E5Exception;

/**
 * 操作模块、图标、流程操作的读取器
 * @created 04-8-2005 14:16:20
 * @author Gong Lijie
 * @version 1.0
 */
public interface ProcReader{
	/**
	 * 根据ID获取Proc对象。<br/>
	 * 会依次检查流程操作和非流程操作，返回ID相同的对象。<br/>
	 * 找不到时返回null
	 * @param procID
	 * @return
	 * @throws E5Exception
	 */
	public Proc get(int procID)
	throws E5Exception;

	/**
	 * 取一个流程节点的BACK操作
	 * 
	 * @param flowNodeID 流程节点ID
	 */
	public ProcFlow getBack(int flowNodeID)
	throws E5Exception;

	/**
	 * 取一个流程节点的DO操作
	 * 
	 * @param flowNodeID 流程节点ID
	 */
	public ProcFlow getDo(int flowNodeID)
	throws E5Exception;

	/**
	 * 取一个流程节点的Go操作
	 * 
	 * @param flowNodeID 流程节点ID
	 */
	public ProcFlow getGo(int flowNodeID)
	throws E5Exception;

	/**
	 * 取一个跳转操作
	 * @param procID    跳转操作ID
	 */
	public ProcFlow getJump(int procID)
	throws E5Exception;

	/**
	 * 按跳转操作的名称取一个流程节点的一个跳转操作
	 * 
	 * @param flowNodeID 流程节点ID
	 * @param procName 跳转操作名称
	 */
	public ProcFlow getJump(int flowNodeID, String procName)
	throws E5Exception;

	/**
	 * 根据ProcID获得流程操作对象ProcFlow
	 * @param procID
	 * @return
	 * @throws E5Exception
	 */
	public ProcFlow getProc(int procID)
	throws E5Exception;
	/**
	 * 按流程操作的名称查找流程节点的一个操作
	 * <BR>可能是DO/BACK/GO/JUMP中的任何一个操作
	 * @param flowNodeID 流程节点ID
	 * @param procName 操作名称
	 */
	public ProcFlow getProc(int flowNodeID, String procName)
	throws E5Exception;

	/**
	 * 取一个流程节点的所有跳转操作，
	 * 取出的操作按ID排序
	 * @param flowNodeID 流程节点ID
	 * <br/>当传入的参数为0时，取出所有的跳转操作
	 */
	public ProcFlow[] getJumps(int flowNodeID)
	throws E5Exception;

	/**
	 * 取一个流程节点的所有操作，
	 * 取出的操作按ID排序
	 * @param flowNodeID 流程节点ID
	 * <br/>当传入的参数为0时，取出所有的流程操作
	 */
	public ProcFlow[] getProcs(int flowNodeID)
	throws E5Exception;
	/**
	 * 根据ID取一个非流程操作
	 * 
	 * @param procID
	 */
	public ProcUnflow getUnflow(int procID)
	throws E5Exception;

	/**
	 * 根据操作名称取某文档类型的一个非流程操作
	 * 
	 * @param docTypeID    文档类型ID。该参数必需，因为在不同文档类型下非流程操作可以同名
	 * @param procName    非流程操作的名称
	 */
	public ProcUnflow getUnflow(int docTypeID, String procName)
	throws E5Exception;

	/**
	 * 取一个文档类型的所有非流程操作，
	 * 取出的操作按ID排序
	 * @param docTypeID    文档类型ID
	 * <br/>当传入的参数为0时，取出所有的非流程操作
	 */
	public ProcUnflow[] getUnflows(int docTypeID)
	throws E5Exception;

	/**
	 * 取一个操作模块对象
	 * @param opID 操作ID
	 */
	public Operation getOperation(int opID)
	throws E5Exception;

	/**
	 * 取一个文档类型的所有操作模块，取出的操作模块按ID排序。
	 * 
	 * @param docTypeID 文档类型ID.
	 * <br/>当传入的参数是0时，取出所有的操作模块.
	 * <br/>-1表示取所有的文档类型无关操作.
	 */
	public Operation[] getOperations(int docTypeID)
	throws E5Exception;

	/**
	 * 取一个图标对象
	 * @param iconID 图标ID
	 */
	public Icon getIcon(int iconID)
	throws E5Exception;

	/**
	 * 取所有图标
	 * <br/>取出的图标按ID排序
	 * @return
	 * @throws E5Exception
	 */
	public Icon[] getIcons()
	throws E5Exception;
}