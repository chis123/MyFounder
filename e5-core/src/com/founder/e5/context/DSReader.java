package com.founder.e5.context;

/**
 * E5数据源读取接口
 * @created 11-7-2005 15:46:25
 * @author Gong Lijie
 * @version 1.0
 */
public interface DSReader {

	/**
	 * 读取E5数据源
	 * @param dsID
	 */
	public E5DataSource get(int dsID) throws E5Exception;

}