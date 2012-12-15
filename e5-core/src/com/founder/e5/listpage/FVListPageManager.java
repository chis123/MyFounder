package com.founder.e5.listpage;

import com.founder.e5.context.E5Exception;

/**
 * 按文件夹设置列表方式的管理接口
 * @created 2009-8-25
 * @author Gong Lijie
 * @version 1.0
 */
public interface FVListPageManager extends FVListPageReader {
	/**
	 * 删除一个文件夹上设置的列表
	 * @param fvID
	 * @throws E5Exception
	 */
	void delete(int fvID) throws E5Exception;

	/**
	 * 保存一个文件夹的列表设置
	 * @param fvID int 文件夹ID
	 * @param lists int[] 列表ID的数组
	 * @throws E5Exception
	 */
	void save(int fvID, int[] lists) throws E5Exception;
}
