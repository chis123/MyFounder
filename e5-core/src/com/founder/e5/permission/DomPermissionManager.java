package com.founder.e5.permission;

import com.founder.e5.context.E5Exception;

/**
 * �ĵ������ֵ�Ȩ�޹�������
 * �����Ȩ�޹���������Ȩ�޹���ȡ�
 * @created 2008-4-21
 * @author Gong Lijie
 * @version 1.0
 */
public interface DomPermissionManager extends DomPermissionReader{
	/**
	 * ���������Ȩ�ޡ�
	 * 
	 * @param roleID
	 * @param filters ������ȱʡ��Ȩ�ޣ����������ʾ����û��Ȩ�޵Ĺ�����ID����
	 * @throws E5Exception
	 */
	void saveFilters(int roleID, int[] filters) throws E5Exception;
	
	/**
	 * �������Ȩ��
	 * @param roleID
	 * @param rules ����ID����
	 * @throws E5Exception
	 */
	void saveRules(int roleID, int[] rules) throws E5Exception;
}
