package com.founder.e5.flow;

import com.founder.e5.context.E5Exception;

/**
 * ����ģ�顢ͼ�ꡢ���̲����Ķ�ȡ��
 * @created 04-8-2005 14:16:20
 * @author Gong Lijie
 * @version 1.0
 */
public interface ProcReader{
	/**
	 * ����ID��ȡProc����<br/>
	 * �����μ�����̲����ͷ����̲���������ID��ͬ�Ķ���<br/>
	 * �Ҳ���ʱ����null
	 * @param procID
	 * @return
	 * @throws E5Exception
	 */
	public Proc get(int procID)
	throws E5Exception;

	/**
	 * ȡһ�����̽ڵ��BACK����
	 * 
	 * @param flowNodeID ���̽ڵ�ID
	 */
	public ProcFlow getBack(int flowNodeID)
	throws E5Exception;

	/**
	 * ȡһ�����̽ڵ��DO����
	 * 
	 * @param flowNodeID ���̽ڵ�ID
	 */
	public ProcFlow getDo(int flowNodeID)
	throws E5Exception;

	/**
	 * ȡһ�����̽ڵ��Go����
	 * 
	 * @param flowNodeID ���̽ڵ�ID
	 */
	public ProcFlow getGo(int flowNodeID)
	throws E5Exception;

	/**
	 * ȡһ����ת����
	 * @param procID    ��ת����ID
	 */
	public ProcFlow getJump(int procID)
	throws E5Exception;

	/**
	 * ����ת����������ȡһ�����̽ڵ��һ����ת����
	 * 
	 * @param flowNodeID ���̽ڵ�ID
	 * @param procName ��ת��������
	 */
	public ProcFlow getJump(int flowNodeID, String procName)
	throws E5Exception;

	/**
	 * ����ProcID������̲�������ProcFlow
	 * @param procID
	 * @return
	 * @throws E5Exception
	 */
	public ProcFlow getProc(int procID)
	throws E5Exception;
	/**
	 * �����̲��������Ʋ������̽ڵ��һ������
	 * <BR>������DO/BACK/GO/JUMP�е��κ�һ������
	 * @param flowNodeID ���̽ڵ�ID
	 * @param procName ��������
	 */
	public ProcFlow getProc(int flowNodeID, String procName)
	throws E5Exception;

	/**
	 * ȡһ�����̽ڵ��������ת������
	 * ȡ���Ĳ�����ID����
	 * @param flowNodeID ���̽ڵ�ID
	 * <br/>������Ĳ���Ϊ0ʱ��ȡ�����е���ת����
	 */
	public ProcFlow[] getJumps(int flowNodeID)
	throws E5Exception;

	/**
	 * ȡһ�����̽ڵ�����в�����
	 * ȡ���Ĳ�����ID����
	 * @param flowNodeID ���̽ڵ�ID
	 * <br/>������Ĳ���Ϊ0ʱ��ȡ�����е����̲���
	 */
	public ProcFlow[] getProcs(int flowNodeID)
	throws E5Exception;
	/**
	 * ����IDȡһ�������̲���
	 * 
	 * @param procID
	 */
	public ProcUnflow getUnflow(int procID)
	throws E5Exception;

	/**
	 * ���ݲ�������ȡĳ�ĵ����͵�һ�������̲���
	 * 
	 * @param docTypeID    �ĵ�����ID���ò������裬��Ϊ�ڲ�ͬ�ĵ������·����̲�������ͬ��
	 * @param procName    �����̲���������
	 */
	public ProcUnflow getUnflow(int docTypeID, String procName)
	throws E5Exception;

	/**
	 * ȡһ���ĵ����͵����з����̲�����
	 * ȡ���Ĳ�����ID����
	 * @param docTypeID    �ĵ�����ID
	 * <br/>������Ĳ���Ϊ0ʱ��ȡ�����еķ����̲���
	 */
	public ProcUnflow[] getUnflows(int docTypeID)
	throws E5Exception;

	/**
	 * ȡһ������ģ�����
	 * @param opID ����ID
	 */
	public Operation getOperation(int opID)
	throws E5Exception;

	/**
	 * ȡһ���ĵ����͵����в���ģ�飬ȡ���Ĳ���ģ�鰴ID����
	 * 
	 * @param docTypeID �ĵ�����ID.
	 * <br/>������Ĳ�����0ʱ��ȡ�����еĲ���ģ��.
	 * <br/>-1��ʾȡ���е��ĵ������޹ز���.
	 */
	public Operation[] getOperations(int docTypeID)
	throws E5Exception;

	/**
	 * ȡһ��ͼ�����
	 * @param iconID ͼ��ID
	 */
	public Icon getIcon(int iconID)
	throws E5Exception;

	/**
	 * ȡ����ͼ��
	 * <br/>ȡ����ͼ�갴ID����
	 * @return
	 * @throws E5Exception
	 */
	public Icon[] getIcons()
	throws E5Exception;
}