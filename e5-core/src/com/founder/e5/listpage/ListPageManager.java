package com.founder.e5.listpage;

import org.hibernate.Session;

import com.founder.e5.context.E5Exception;

/**
 * ���PageList�Ĵ�ŵĹ���
 * @author LuZuowei
 *
 */
public interface ListPageManager extends ListPageReader 
{
	/**
	 * �����������ݵ���ĵ�ǰϵͳ��
	 * @param xml
	 * @return
	 * @throws E5Exception
	 */
	public void imports(String xml) throws E5Exception;
	/**
	 * ��ϵͳ�ڵ�����ȫ���б�ʽ����
	 * @return
	 * @throws E5Exception
	 */
	public String exports() throws E5Exception;
	/**
	 * ����һ��List
	 * @param page
	 * @param session
	 * @return
	 */
	public boolean create(ListPage page,Session session) throws E5Exception;
	/**
	 * ���ĵ�ǰ��List
	 * @param page
	 * @param session
	 * @return
	 */
	public boolean update(ListPage page,Session session) throws E5Exception;
	/**
	 * ɾ����ǰ��List
	 * @param page
	 * @param session
	 * @return
	 */
	public boolean delete(ListPage page,Session session) throws E5Exception;
	/**
	 * ɾ����ǰ�ĵ�����ID������List
	 * @param doctypeid
	 * @param session
	 * @return
	 */
	public boolean delete(int doctypeid,Session session) throws E5Exception;
	/**
	 * ɾ����ǰ�ĵ�����ID�ĵ�ǰList 
	 * @param doctypeid
	 * @param listid
	 * @param session
	 * @return
	 */
	public boolean delete(int doctypeid,int listid,Session session) throws E5Exception;
}
