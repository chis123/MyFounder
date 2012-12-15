package com.founder.e5.listpage;

import com.founder.e5.context.E5Exception;

public interface ListPageReader 
{
	/**
	 * 获取该文档类型所有的列表方式
	 * @param doctypeid
	 * @param session
	 * @return
	 * @throws E5Exception
	 */
	public ListPage[] get(int doctypeid) throws E5Exception;
	/**
	 * 获取当前文档类型的特定列表方式
	 * @param doctypeid
	 * @param listid
	 * @param session
	 * @return
	 * @throws E5Exception
	 */
	public ListPage get(int doctypeid,int listid) throws E5Exception;
	
	/**
	 * 获取当前listid对应的特定类表方式
	 * @param listid
	 * @param session
	 * @return
	 * @throws E5Exception
	 */
	public ListPage getPageList(int listid) throws E5Exception;
}
