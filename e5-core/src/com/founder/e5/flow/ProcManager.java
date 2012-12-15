package com.founder.e5.flow;

import com.founder.e5.context.E5Exception;

/**
 * ����ɾ��Procʱ,���ProcType��do,go,back���ͣ���Ҫͬ���޸�flownode�Ķ�Ӧ��¼���ֶ�
 * @created 04-8-2005 14:16:16
 * @author Gong Lijie
 * @version 1.0
 */
public interface ProcManager extends ProcReader {

	/**
	 * ��һ�����̽ڵ������BACK����
	 * 
	 * @param flowNodeID
	 * @param proc
	 */
	public void addBack(int flowNodeID, Proc proc)
	throws E5Exception;

	/**
	 * ��һ�����̽ڵ������DO����
	 * 
	 * @param flowNodeID    ���̽ڵ�ID
	 * @param proc
	 */
	public void addDo(int flowNodeID, Proc proc)
	throws E5Exception;

	/**
	 * ��һ�����̽ڵ������GO����
	 * 
	 * @param flowNodeID
	 * @param proc
	 */
	public void addGo(int flowNodeID, Proc proc)
	throws E5Exception;

	/**
	 * ��һ�����̽ڵ���������ת����
	 * 
	 * @param flowNodeID    ���̽ڵ�ID
	 * @param proc
	 */
	public void createJump(int flowNodeID, ProcFlow proc)
	throws E5Exception;

	/**
	 * ��һ�����̽ڵ�������һ�����̲���
	 * @param flowNodeID ���̽ڵ�ID
	 * @param proc ���̲��������Ѿ���procType��ֵ
	 * @throws E5Exception
	 */
	public void createProc(int flowNodeID, ProcFlow proc)
	throws E5Exception;
	/**
	 * ����һ�����̲���
	 * @param proc ���̲��������Ѿ���procType,flowNodeID�ȹؼ��ֶθ�ֵ
	 * @throws E5Exception
	 */
	public void createProc(ProcFlow proc)
	throws E5Exception;
	/**
	 * ��һ���ĵ�����������һ�������̲���
	 * 
	 * @param docTypeID
	 * @param proc
	 */
	public void createUnflow(int docTypeID, ProcUnflow proc)
	throws E5Exception;

	/**
	 * ɾ��һ�����̽ڵ��BACK����
	 * 
	 * @param flowNodeID
	 */
	public void deleteBack(int flowNodeID)
	throws E5Exception;

	/**
	 * ɾ��һ�����̽ڵ��DO����
	 * 
	 * @param flowNodeID
	 */
	public void deleteDo(int flowNodeID)
	throws E5Exception;

	/**
	 * ɾ��һ�����̽ڵ��GO����
	 * 
	 * @param flowNodeID
	 */
	public void deleteGo(int flowNodeID)
	throws E5Exception;

	/**
	 * ɾ��һ����ת����
	 * @param procID    ��תID
	 */
	public void deleteJump(int procID)
	throws E5Exception;

	/**
	 * ɾ��һ�����̲���
	 * @param procID
	 * @throws E5Exception
	 */
	public void deleteProc(int procID)
	throws E5Exception;
	/**
	 * ɾ��һ�������̲���
	 * 
	 * @param procID    �����̲���ID
	 */
	public void deleteUnflow(int procID)
	throws E5Exception;

	/**
	 * ɾ��һ���ĵ����͵����з����̲���
	 * 
	 * @param docTypeID    �ĵ�����ID
	 */
	public void deleteUnflows(int docTypeID)
	throws E5Exception;

	/**
	 * �޸�һ����ת����
	 * @param proc
	 */
	public void updateJump(ProcFlow proc)
	throws E5Exception;

	/**
	 * �޸�һ�����̲���
	 * @param proc
	 * @throws E5Exception
	 */
	public void updateProc(ProcFlow proc)
	throws E5Exception;
	/**
	 * �޸ķ����̲���
	 * 
	 * @param proc
	 */
	public void updateUnflow(ProcUnflow proc)
	throws E5Exception;

	/**
	 * ����һ������ģ��
	 * @param docTypeID �ĵ�����ID��ָ������ģ���������ĵ�����
	 * @param op
	 */
	public void createOperation(int docTypeID, Operation op)
	throws E5Exception;

	/**
	 * �޸�һ������ģ��
	 * @param op
	 */
	public void updateOperation(Operation op)
	throws E5Exception;

	/**
	 * ɾ��һ������ģ��<br/>
	 * ������ģ���Ѿ�������������ʱ��������ɾ���ò���ģ�飬�׳�����Ϊ1001���쳣
	 * @param opID
	 */
	public void deleteOperation(int opID)
	throws E5Exception;
	/**
	 * ɾ��һ���ĵ������µ����в���ģ��
	 * ����ж���ĵ�����ʱ
	 * @param opID
	 */
	public void deleteOperations(int docTypeID)
	throws E5Exception;
	/**
	 * ����һ��ͼ��
	 * @param icon
	 */
	public void createIcon(Icon icon)
	throws E5Exception;

	/**
	 * �޸�һ��ͼ��
	 * ͼ����ļ����������޸�
	 * @param icon
	 */
	public void updateIcon(Icon icon)
	throws E5Exception;

	/**
	 * ɾ��ͼ��<br/>
	 * ��ͼ���Ѿ�������������ʱ��������ɾ����ͼ�꣬�׳�����Ϊ1002���쳣
	 * @param iconID
	 */
	public void deleteIcon(int iconID)
	throws E5Exception;
}