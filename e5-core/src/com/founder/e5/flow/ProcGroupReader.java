package com.founder.e5.flow;

import com.founder.e5.context.E5Exception;

/**
 * �������ȡ��
 * @author Gong Lijie
 * @created on 2008-4-29
 * @version 1.0
 */
public interface ProcGroupReader {

	/**
	 * ȡ������
	 * <br/>ȡĳһ�����̽ڵ��ϵĲ����飬����ĳһ���ĵ����͵ķ����̲�������
	 * <br/>��flowNodeID��0ʱ��ȡ������Ӧ�ĵ������µķ����̲�������
	 * @param docTypeID �ĵ�����ID
	 * @param flowNodeID ���̽ڵ�ID
	 */
	public ProcGroup[] getProcGroups(int docTypeID, int flowNodeID)
	throws E5Exception;
}
