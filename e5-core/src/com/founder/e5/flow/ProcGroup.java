package com.founder.e5.flow;

import java.util.ArrayList;
import java.util.List;

/**
 * ������
 * @author Gong Lijie
 * @created on 2008-4-28
 * @version 1.0
 */
public class ProcGroup{
	private int docTypeID;
	private int flowNodeID;
	//private int flag;
	private int groupNo;	//�����
	private int procCount; 	//��������ĸ���
	private List list = new ArrayList(); 		//ͬ��������б���˳���б�Ԫ����ProcOrder����
	
	public int getGroupNo() {
		return groupNo;
	}
	public List getList() {
		return list;
	}
	public int getProcCount() {
		return procCount;
	}
	/**
	 * ��������һ������
	 * @param proc
	 */
	public void addProc(ProcOrder proc) {
		list.add(proc);
		procCount++;
	}
	//��������š��������Զ�˳�ţ���˲����Ⱪ¶setter
	void setGroupNo(int groupNo) {
		this.groupNo = groupNo;
	}
	
	public int getDocTypeID() {
		return docTypeID;
	}
	void setDocTypeID(int docTypeID) {
		this.docTypeID = docTypeID;
	}
	
	public int getFlowNodeID() {
		return flowNodeID;
	}
	void setFlowNodeID(int flowNodeID) {
		this.flowNodeID = flowNodeID;
	}
}
