package com.founder.e5.flow;

import com.founder.e5.context.E5Exception;

/**
 * ������Ϣ��ȡ��
 * @created 04-6-2005 14:14:20
 * @author Gong Lijie
 * @version 1.0
 */
public interface FlowReader {

	/**
	 * ��������ID�õ�һ������
	 * 
	 * @param flowID    ����ID
	 */
	public Flow getFlow(int flowID)
	throws E5Exception;

	/**
	 * �����ĵ�����ID���������õ�����
	 * 
	 * @param docTypeID    �ĵ�����ID
	 * @param flowName    ������
	 */
	public Flow getFlow(int docTypeID, String flowName)
	throws E5Exception;

	/**
	 * ȡĳ�ĵ����͵���������
	 * 
	 * @param docTypeID    �ĵ�����ID
	 * <br/>������Ĳ�����0ʱ��ȡ�����е�����
	 */
	public Flow[] getFlows(int docTypeID)
	throws E5Exception;

	/**
	 * ������ID�����̽ڵ�����ȡһ�����̽ڵ�
	 * 
	 * @param flowID    ����ID
	 * @param flowNodeName    ���̽ڵ���
	 */
	public FlowNode getFlowNode(int flowID, String flowNodeName)
	throws E5Exception;

	/**
	 * ȡһ�����̵��������̽ڵ㣬���ڵ��ǰ��˳������
	 * @param flowID
	 * <br/>������Ĳ�����0ʱ��ȡ�����е����̽ڵ�
	 */
	public FlowNode[] getFlowNodes(int flowID)
	throws E5Exception;
	
	/**
	 * ȡһ�����̽ڵ�
	 * 
	 * @param flowNodeID
	 */
	public FlowNode getFlowNode(int flowNodeID)
	throws E5Exception;

	/**
	 * ȡĳ���̽ڵ��ǰ��Ľڵ�
	 * 
	 * @param flowNodeID
	 */
	public FlowNode getPreFlowNode(int flowNodeID)
	throws E5Exception;

	/**
	 * ȡĳ���̽ڵ�ĺ���ڵ�
	 * 
	 * @param flowNodeID
	 */
	public FlowNode getNextFlowNode(int flowNodeID)
	throws E5Exception;

}