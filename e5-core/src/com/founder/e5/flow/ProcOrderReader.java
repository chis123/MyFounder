package com.founder.e5.flow;

import com.founder.e5.context.E5Exception;

/**
 * ���������ȡ��
 * @created 04-8-2005 15:05:35
 * @author Gong Lijie
 * @version 1.0
 */
public interface ProcOrderReader {

	/**
	 * ȡ�����Ĳ���<br/>
	 * Ϊ�˼����Ա��������������ʵ���ϵ�ͬ�ڽӿ��е�����һ��������<br/>
	 * ��flowID����û���ô����̶�����0���ɡ�
	 * @param docTypeID �ĵ�����ID
	 * @param flowID	����ID
	 * @param flowNodeID ���̽ڵ�ID
	 */
	public ProcOrder[] getProcOrders(int docTypeID, int flowID, int flowNodeID)
	throws E5Exception;

	/**
	 * ȡ�����Ĳ���
	 * <br/>ȡĳһ�����̽ڵ��ϵ���������������ĳһ���ĵ����͵ķ������������
	 * <br/>��flowNodeID��0ʱ��ȡ������Ӧ�ĵ������µķ����̲�����������
	 * @param docTypeID �ĵ�����ID
	 * @param flowNodeID ���̽ڵ�ID
	 */
	public ProcOrder[] getProcOrders(int docTypeID, int flowNodeID)
	throws E5Exception;
}