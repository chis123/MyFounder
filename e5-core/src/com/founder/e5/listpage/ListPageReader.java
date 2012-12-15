package com.founder.e5.listpage;

import com.founder.e5.context.E5Exception;

public interface ListPageReader 
{
	/**
	 * ��ȡ���ĵ��������е��б�ʽ
	 * @param doctypeid
	 * @param session
	 * @return
	 * @throws E5Exception
	 */
	public ListPage[] get(int doctypeid) throws E5Exception;
	/**
	 * ��ȡ��ǰ�ĵ����͵��ض��б�ʽ
	 * @param doctypeid
	 * @param listid
	 * @param session
	 * @return
	 * @throws E5Exception
	 */
	public ListPage get(int doctypeid,int listid) throws E5Exception;
	
	/**
	 * ��ȡ��ǰlistid��Ӧ���ض����ʽ
	 * @param listid
	 * @param session
	 * @return
	 * @throws E5Exception
	 */
	public ListPage getPageList(int listid) throws E5Exception;
}
