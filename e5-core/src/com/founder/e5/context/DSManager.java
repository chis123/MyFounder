package com.founder.e5.context;

/**
 * E5数据源管理接口
 * @created 11-7-2005 15:46:25
 * @author Gong Lijie
 * @version 1.0
 */
public interface DSManager extends DSReader {

	/**
	 * 创建一个数据源
	 * 
	 * @param ds
	 */
	public void create(E5DataSource ds) throws E5Exception;

	/**
	 * 修改数据源
	 * 
	 * @param ds
	 */
	public void update(E5DataSource ds) throws E5Exception;

	/**
	 * 数据源删除
	 * 
	 * @param dsID
	 */
	public void delete(int dsID) throws E5Exception;

	/**
	 * 获取所有数据源
	 */
	public E5DataSource[] getAll() throws E5Exception;

}