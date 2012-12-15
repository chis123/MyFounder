package com.founder.e5.flow;

import com.founder.e5.context.E5Exception;

/**
 * �����������
 * @author Gong Lijie
 * @created on 2008-4-28
 * @version 1.0
 */
public interface ProcGroupManager extends ProcGroupReader {
	/**
	 * ��������ĳ�����µ����в�����
	 * <BR>����˳������˲������˳��
	 * <BR>����ʱ���Ȱ���docTypeID/flowNodeID����ԭ���Ĳ�����ɾ��
	 * @param docTypeID
	 * @param flowNodeID
	 * @param procs
	 * @throws E5Exception
	 */
	public void reset(int docTypeID, int flowNodeID, ProcGroup[] groups)
	throws E5Exception;
	
	/**
	 * ȡ���еĲ�����
	 * @return
	 * @throws E5Exception
	 */
	public ProcGroup[] getAllGroups() throws E5Exception;
}