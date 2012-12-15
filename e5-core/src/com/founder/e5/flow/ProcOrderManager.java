package com.founder.e5.flow;

import com.founder.e5.context.E5Exception;

/**
 * @created 04-8-2005 15:05:30
 * @author Gong Lijie
 * @version 1.0
 */
public interface ProcOrderManager extends ProcOrderReader {

	/**
	 * ������һ�����̽ڵ�ʱ���Զ������з����̲����ӵ������������
	 * <BR>�ò�����Ӧ��ֱ�ӵ��ã���Ӧ�����������̽ڵ�Ĳ��ֽ��п���
	 * @param node
	 * @throws E5Exception û���ҵ���Ӧ������ʱ�׳�����1003���쳣
	 */
	public void addFlowNode(FlowNode node)
	throws E5Exception;

	/**
	 * ����һ�����̲���ʱ���������������
	 * <BR>�ò�����Ӧ��ֱ�ӵ��ã���Ӧ�����������̲����Ĳ��ֽ��п���

	 * <BR>�������̲�����ֻҪ�ڶ�Ӧ�����̽ڵ������������������
	 * <BR>���ڷ����̲�������Ҫ���ĵ����������ӷ��������򣬲������ĵ����͵��������̵�ÿ�����̽ڵ�����������
	 * 
	 * <BR>ʹ��һ��Proc���͵Ĳ����������������Ϣ��
	 * <BR>�������͡��ĵ����ͣ�ֻ�Է����̲�����Ч����������Ϣ��ֻ�����̲�����Ч����
	 * <BR>����ID��ֻ�Է����̲�������ת������Ч��������������Ϣ
	 * @param proc
	 */
	public void append(Proc proc)
	throws E5Exception;

	/**
	 * ɾ��һ�����̲���ʱ��ͬʱɾ�����������
	 * <BR>�ò�����Ӧ��ֱ�ӵ��ã���Ӧ����ɾ�����̲����Ĳ��ֽ��п���
	 * @param proc
	 */
	public void delete(Proc proc)
	throws E5Exception;

	/**
	 * ɾ���ĵ�����ʱ����������������ɾ��
	 * 
	 * @param docTypeID
	 */
	public void deleteDocType(int docTypeID)
	throws E5Exception;
	/**
	 * ɾ������ʱ����������������ɾ��
	 * 
	 * @param flowID
	 */
	public void deleteFlow(int flowID)
	throws E5Exception;

	/**
	 * ɾ�����̽ڵ�ʱ����������������ɾ��
	 * 
	 * @param flowNodeID
	 */
	public void deleteFlowNode(int flowNodeID)
	throws E5Exception;

	/**
	 * ȡ��ȫ�����������
	 * <BR>����ʽ���ĵ����͡����̡����̽ڵ㡢˳��
	 */
	public ProcOrder[] getAll()
	throws E5Exception;

	/**
	 * ��������ĳ�����µ����в���˳��
	 * <BR>����˳������˲���������
	 * <BR>����ʱ���Ȱ���docTypeID/flowID/flowNodeID����ԭ��������ɾ��
	 * @param docTypeID
	 * @param flowID
	 * @param flowNodeID
	 * @param procs
	 * @throws E5Exception
	 */
	public void reset(int docTypeID, int flowID, int flowNodeID, Proc[] procs)
	throws E5Exception;
	/**
	 * �޸�һ������ʱ��ͬʱ�޸����������
	 * <BR>�ò�����Ӧ��ֱ�ӵ��ã���Ӧ�����޸Ĳ����Ĳ��ֽ��п���
	 * @param proc
	 */
	public void update(Proc proc)
	throws E5Exception;

}