package com.founder.e5.context;

/**
 * E5����Դ����ӿ�
 * @created 11-7-2005 15:46:25
 * @author Gong Lijie
 * @version 1.0
 */
public interface DSManager extends DSReader {

	/**
	 * ����һ������Դ
	 * 
	 * @param ds
	 */
	public void create(E5DataSource ds) throws E5Exception;

	/**
	 * �޸�����Դ
	 * 
	 * @param ds
	 */
	public void update(E5DataSource ds) throws E5Exception;

	/**
	 * ����Դɾ��
	 * 
	 * @param dsID
	 */
	public void delete(int dsID) throws E5Exception;

	/**
	 * ��ȡ��������Դ
	 */
	public E5DataSource[] getAll() throws E5Exception;

}