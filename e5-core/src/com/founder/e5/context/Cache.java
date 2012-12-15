package com.founder.e5.context;

/**
 * 缓存接口
 * 每个实现缓存的类都继承此接口
 * @created 11-7-2005 15:46:20
 * @author Gong Lijie
 * @version 1.0
 */
public interface Cache {
	/**
	 * 缓存刷新
	 */
	public void refresh() throws E5Exception;

	/**
	 * 缓存重置
	 */
	public void reset();

}