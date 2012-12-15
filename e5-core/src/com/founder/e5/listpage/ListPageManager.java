package com.founder.e5.listpage;

import org.hibernate.Session;

import com.founder.e5.context.E5Exception;

/**
 * 完成PageList的存放的过程
 * @author LuZuowei
 *
 */
public interface ListPageManager extends ListPageReader 
{
	/**
	 * 将导出的内容导入的当前系统中
	 * @param xml
	 * @return
	 * @throws E5Exception
	 */
	public void imports(String xml) throws E5Exception;
	/**
	 * 将系统内的所有全部列表方式导出
	 * @return
	 * @throws E5Exception
	 */
	public String exports() throws E5Exception;
	/**
	 * 创建一个List
	 * @param page
	 * @param session
	 * @return
	 */
	public boolean create(ListPage page,Session session) throws E5Exception;
	/**
	 * 更改当前的List
	 * @param page
	 * @param session
	 * @return
	 */
	public boolean update(ListPage page,Session session) throws E5Exception;
	/**
	 * 删除当前的List
	 * @param page
	 * @param session
	 * @return
	 */
	public boolean delete(ListPage page,Session session) throws E5Exception;
	/**
	 * 删除当前文档类型ID的所有List
	 * @param doctypeid
	 * @param session
	 * @return
	 */
	public boolean delete(int doctypeid,Session session) throws E5Exception;
	/**
	 * 删除当前文档类型ID的当前List 
	 * @param doctypeid
	 * @param listid
	 * @param session
	 * @return
	 */
	public boolean delete(int doctypeid,int listid,Session session) throws E5Exception;
}
