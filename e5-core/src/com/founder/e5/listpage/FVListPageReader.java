package com.founder.e5.listpage;

import com.founder.e5.context.E5Exception;

/**
 * �ļ����б�ʽ�Ķ�ȡ�ӿ�
 * @created 2009-8-25
 * @author Gong Lijie
 * @version 1.0
 */
public interface FVListPageReader {
	/**
	 * ��ȡһ���ļ���/��ͼ���б�ʽ��
	 * @param fvID
	 * @return ListPage����
	 * @throws E5Exception
	 */
	ListPage[] get(int fvID) throws E5Exception;
	/**
	 * ��ȡһ���ļ���/��ͼ���б�ʽ��
	 * @param fvID
	 * @return �б�ID����
	 * @throws E5Exception
	 */
	int[] getIDs(int fvID) throws E5Exception;
	
	/**
	 * �������ļ��е��б�ʽ
	 * @return FVListPage����
	 * @throws E5Exception
	 */
	FVListPage[] getAll() throws E5Exception;
}
