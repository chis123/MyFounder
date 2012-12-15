package com.founder.e5.flow;

import com.founder.e5.context.E5Exception;

/**
 * @created 04-8-2005 14:14:34
 * @author Gong Lijie
 * @version 1.0
 */
public interface FlowManager extends FlowReader {

	/**
	 * ��������
	 * @param docTypeID �ĵ�����ID
	 * @param flow
	 */
	public void create(int docTypeID, Flow flow)
	throws E5Exception;

	/**
	 * ����ĳ���̵��޸�
	 * 
	 * @param flow
	 */
	public void update(Flow flow)
	throws E5Exception;

	/**
	 * ɾ������
	 * ɾ������ʱ��ͬʱҪɾ���������̽ڵ㡢����������
	 * 
	 * @param flowID
	 */
	public void deleteFlow(int flowID)
	throws E5Exception;

	/**
	 * ����һ�����̽ڵ�
	 * 
	 * @param flowNode
	 */
	public void append(int flowID, FlowNode flowNode)
	throws E5Exception;

	/**
	 * ��ĳλ��ǰ����һ�����̽ڵ�
	 * @param flowID ����ID
	 * @param flowNode �ڵ�
	 * @param position ����λ�ã�֮��Ľڵ�ID��
	 */
	public void insert(int flowID, FlowNode flowNode, int position)
	throws E5Exception;

	/**
	 * �޸����̽ڵ�
	 * 
	 * @param flowNode
	 */
	public void update(FlowNode flowNode)
	throws E5Exception;

	/**
	 * ɾ��һ�����̽ڵ�
	 * ͬʱҪɾ�����̽ڵ��µ����в����Լ����򣬲��Ұ�ǰ��ڵ㴮����
	 * 
	 * @param flowNodeID
	 */
	public void deleteFlowNode(int flowNodeID)
	throws E5Exception;

	/**
	 * ɾ���ĵ�����ʱ�����̲���������
	 * �Զ�ɾ�������������̡����̽ڵ㣬����������
	 * 
	 * @param docTypeID
	 */
	public void deleteDocType(int docTypeID)
	throws E5Exception;

}