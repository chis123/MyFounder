package com.founder.e5.flow;

import java.util.ArrayList;
import java.util.List;

/**
 * 操作组
 * @author Gong Lijie
 * @created on 2008-4-28
 * @version 1.0
 */
public class ProcGroup{
	private int docTypeID;
	private int flowNodeID;
	//private int flag;
	private int groupNo;	//组序号
	private int procCount; 	//本组操作的个数
	private List list = new ArrayList(); 		//同组操作的列表，按顺序。列表元素是ProcOrder对象
	
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
	 * 本组增加一个操作
	 * @param proc
	 */
	public void addProc(ProcOrder proc) {
		list.add(proc);
		procCount++;
	}
	//设置组序号。组的序号自动顺排，因此不对外暴露setter
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
