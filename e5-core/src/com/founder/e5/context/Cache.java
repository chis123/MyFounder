package com.founder.e5.context;

/**
 * ����ӿ�
 * ÿ��ʵ�ֻ�����඼�̳д˽ӿ�
 * @created 11-7-2005 15:46:20
 * @author Gong Lijie
 * @version 1.0
 */
public interface Cache {
	/**
	 * ����ˢ��
	 */
	public void refresh() throws E5Exception;

	/**
	 * ��������
	 */
	public void reset();

}