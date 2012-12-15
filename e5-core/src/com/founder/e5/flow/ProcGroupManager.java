package com.founder.e5.flow;

import com.founder.e5.context.E5Exception;

/**
 * 操作组管理器
 * @author Gong Lijie
 * @created on 2008-4-28
 * @version 1.0
 */
public interface ProcGroupManager extends ProcGroupReader {
	/**
	 * 重新设置某条件下的所有操作组
	 * <BR>数组顺序决定了操作组的顺序
	 * <BR>重置时，先按照docTypeID/flowNodeID，把原来的操作组删除
	 * @param docTypeID
	 * @param flowNodeID
	 * @param procs
	 * @throws E5Exception
	 */
	public void reset(int docTypeID, int flowNodeID, ProcGroup[] groups)
	throws E5Exception;
	
	/**
	 * 取所有的操作组
	 * @return
	 * @throws E5Exception
	 */
	public ProcGroup[] getAllGroups() throws E5Exception;
}