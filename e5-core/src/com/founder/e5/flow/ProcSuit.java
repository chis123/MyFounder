package com.founder.e5.flow;

/**
 * 操作系列的实体类
 * @author Gong Lijie
 * @created on 2008-4-29
 * @version 1.0
 */
public class ProcSuit extends ProcOrder{
	private static final long serialVersionUID = 1L;
	
	private int flag;
	private int suitID;
	private String suitName;
	private int suitCount;
	
	public int getFlag() {
		return flag;
	}
	public int getSuitCount() {
		return suitCount;
	}
	public int getSuitID() {
		return suitID;
	}
	public String getSuitName() {
		return suitName;
	}
	
}
