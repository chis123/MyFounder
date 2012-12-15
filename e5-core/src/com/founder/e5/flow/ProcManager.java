package com.founder.e5.flow;

import com.founder.e5.context.E5Exception;

/**
 * 在增删改Proc时,如果ProcType是do,go,back类型，需要同步修改flownode的对应记录的字段
 * @created 04-8-2005 14:16:16
 * @author Gong Lijie
 * @version 1.0
 */
public interface ProcManager extends ProcReader {

	/**
	 * 在一个流程节点上添加BACK操作
	 * 
	 * @param flowNodeID
	 * @param proc
	 */
	public void addBack(int flowNodeID, Proc proc)
	throws E5Exception;

	/**
	 * 在一个流程节点上添加DO操作
	 * 
	 * @param flowNodeID    流程节点ID
	 * @param proc
	 */
	public void addDo(int flowNodeID, Proc proc)
	throws E5Exception;

	/**
	 * 在一个流程节点上添加GO操作
	 * 
	 * @param flowNodeID
	 * @param proc
	 */
	public void addGo(int flowNodeID, Proc proc)
	throws E5Exception;

	/**
	 * 在一个流程节点上增加跳转操作
	 * 
	 * @param flowNodeID    流程节点ID
	 * @param proc
	 */
	public void createJump(int flowNodeID, ProcFlow proc)
	throws E5Exception;

	/**
	 * 在一个流程节点上增加一个流程操作
	 * @param flowNodeID 流程节点ID
	 * @param proc 流程操作对象，已经对procType赋值
	 * @throws E5Exception
	 */
	public void createProc(int flowNodeID, ProcFlow proc)
	throws E5Exception;
	/**
	 * 增加一个流程操作
	 * @param proc 流程操作对象，已经对procType,flowNodeID等关键字段赋值
	 * @throws E5Exception
	 */
	public void createProc(ProcFlow proc)
	throws E5Exception;
	/**
	 * 在一个文档类型上增加一个非流程操作
	 * 
	 * @param docTypeID
	 * @param proc
	 */
	public void createUnflow(int docTypeID, ProcUnflow proc)
	throws E5Exception;

	/**
	 * 删除一个流程节点的BACK操作
	 * 
	 * @param flowNodeID
	 */
	public void deleteBack(int flowNodeID)
	throws E5Exception;

	/**
	 * 删除一个流程节点的DO操作
	 * 
	 * @param flowNodeID
	 */
	public void deleteDo(int flowNodeID)
	throws E5Exception;

	/**
	 * 删除一个流程节点的GO操作
	 * 
	 * @param flowNodeID
	 */
	public void deleteGo(int flowNodeID)
	throws E5Exception;

	/**
	 * 删除一个跳转操作
	 * @param procID    跳转ID
	 */
	public void deleteJump(int procID)
	throws E5Exception;

	/**
	 * 删除一个流程操作
	 * @param procID
	 * @throws E5Exception
	 */
	public void deleteProc(int procID)
	throws E5Exception;
	/**
	 * 删除一个非流程操作
	 * 
	 * @param procID    非流程操作ID
	 */
	public void deleteUnflow(int procID)
	throws E5Exception;

	/**
	 * 删除一个文档类型的所有非流程操作
	 * 
	 * @param docTypeID    文档类型ID
	 */
	public void deleteUnflows(int docTypeID)
	throws E5Exception;

	/**
	 * 修改一个跳转操作
	 * @param proc
	 */
	public void updateJump(ProcFlow proc)
	throws E5Exception;

	/**
	 * 修改一个流程操作
	 * @param proc
	 * @throws E5Exception
	 */
	public void updateProc(ProcFlow proc)
	throws E5Exception;
	/**
	 * 修改非流程操作
	 * 
	 * @param proc
	 */
	public void updateUnflow(ProcUnflow proc)
	throws E5Exception;

	/**
	 * 创建一个操作模块
	 * @param docTypeID 文档类型ID，指定操作模块所属的文档类型
	 * @param op
	 */
	public void createOperation(int docTypeID, Operation op)
	throws E5Exception;

	/**
	 * 修改一个操作模块
	 * @param op
	 */
	public void updateOperation(Operation op)
	throws E5Exception;

	/**
	 * 删除一个操作模块<br/>
	 * 当操作模块已经用于流程设置时，不允许删除该操作模块，抛出代码为1001的异常
	 * @param opID
	 */
	public void deleteOperation(int opID)
	throws E5Exception;
	/**
	 * 删除一个文档类型下的所有操作模块
	 * 用于卸载文档类型时
	 * @param opID
	 */
	public void deleteOperations(int docTypeID)
	throws E5Exception;
	/**
	 * 创建一个图标
	 * @param icon
	 */
	public void createIcon(Icon icon)
	throws E5Exception;

	/**
	 * 修改一个图标
	 * 图标的文件名不允许修改
	 * @param icon
	 */
	public void updateIcon(Icon icon)
	throws E5Exception;

	/**
	 * 删除图标<br/>
	 * 当图标已经用于流程设置时，不允许删除该图标，抛出代码为1002的异常
	 * @param iconID
	 */
	public void deleteIcon(int iconID)
	throws E5Exception;
}