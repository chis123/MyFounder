package com.founder.e5.listpage;

import com.founder.e5.context.E5Exception;

/**
 * 文件夹列表方式的读取接口
 * @created 2009-8-25
 * @author Gong Lijie
 * @version 1.0
 */
public interface FVListPageReader {
	/**
	 * 读取一个文件夹/视图的列表方式。
	 * @param fvID
	 * @return ListPage数组
	 * @throws E5Exception
	 */
	ListPage[] get(int fvID) throws E5Exception;
	/**
	 * 读取一个文件夹/视图的列表方式。
	 * @param fvID
	 * @return 列表ID数组
	 * @throws E5Exception
	 */
	int[] getIDs(int fvID) throws E5Exception;
	
	/**
	 * 读所有文件夹的列表方式
	 * @return FVListPage数组
	 * @throws E5Exception
	 */
	FVListPage[] getAll() throws E5Exception;
}
