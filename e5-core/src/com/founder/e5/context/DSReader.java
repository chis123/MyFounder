package com.founder.e5.context;

/**
 * E5����Դ��ȡ�ӿ�
 * @created 11-7-2005 15:46:25
 * @author Gong Lijie
 * @version 1.0
 */
public interface DSReader {

	/**
	 * ��ȡE5����Դ
	 * @param dsID
	 */
	public E5DataSource get(int dsID) throws E5Exception;

}