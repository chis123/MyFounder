package com.founder.e5.listpage;

import com.founder.e5.context.E5Exception;

/**
 * ���ļ��������б�ʽ�Ĺ���ӿ�
 * @created 2009-8-25
 * @author Gong Lijie
 * @version 1.0
 */
public interface FVListPageManager extends FVListPageReader {
	/**
	 * ɾ��һ���ļ��������õ��б�
	 * @param fvID
	 * @throws E5Exception
	 */
	void delete(int fvID) throws E5Exception;

	/**
	 * ����һ���ļ��е��б�����
	 * @param fvID int �ļ���ID
	 * @param lists int[] �б�ID������
	 * @throws E5Exception
	 */
	void save(int fvID, int[] lists) throws E5Exception;
}
