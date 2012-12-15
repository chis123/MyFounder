/*
 * Created on 2005-6-21 16:57:53
 *
 */
package com.founder.e5.doc;

import java.lang.reflect.Field;
import java.sql.Timestamp;

/**
 * 流程记录描述了对文档的一次操作的信息。<br>
 * 文档实体与流程记录实体是一对多的关系
 * 
 * @author liyanhui
 * @version 1.0
 * @created 2005-6-21 16:57:53
 */
public class FlowRecord {

	private long frID; // 流程记录实体的数据库标识

	private long docID;// 对应的文档ID

	private int docLibID;// 对应的文档所属文档库的ID

	private int operatorID;// 操作人ID（即用户表的主键）

	private String operator;// 操作人

	private String operation;// 操作名

	private Timestamp startTime;// 操作开始时间

	private Timestamp endTime;// 操作结束时间

	private String fromPosition;// 操作前所处流程节点名

	private String toPosition;// 操作后所处流程节点

	private int lastFlowNode;// 操作前所处流程节点ID

	private int currentFlowNode;// 操作后所处流程节点ID

	private String detail;// 操作详细描述信息

	/**
	 * 
	 */
	public FlowRecord() {
		super();
	}

	/**
	 * @return 操作后所处流程节点ID
	 */
	public int getCurrentFlowNode() {
		return currentFlowNode;
	}

	/**
	 * @param 操作后所处流程节点ID
	 */
	public void setCurrentFlowNode( int currentFlowNode ) {
		this.currentFlowNode = currentFlowNode;
	}

	/**
	 * @return Returns the detail.
	 */
	public String getDetail() {
		return detail;
	}

	/**
	 * @param detail The detail to set.
	 */
	public void setDetail( String detail ) {
		this.detail = detail;
	}

	/**
	 * @return Returns the docID.
	 */
	public long getDocID() {
		return docID;
	}

	public int getDocLibID() {
		return docLibID;
	}

	// docLibID不能被客户修改
	public void setDocLibID( int docLibID ) {
		this.docLibID = docLibID;
	}

	// docID不能被客户修改
	void setDocID( long docID ) {
		this.docID = docID;
	}

	/**
	 * @return Returns the endTime.
	 */
	public Timestamp getEndTime() {
		return endTime;
	}

	/**
	 * @param endTime The endTime to set.
	 */
	public void setEndTime( Timestamp endTime ) {
		this.endTime = endTime;
	}

	/**
	 * @return Returns the frID.
	 */
	public long getFrID() {
		return frID;
	}

	// frID不能被客户修改
	/**
	 * @param frID The frID to set.
	 */
	void setFrID( long frID ) {
		this.frID = frID;
	}

	/**
	 * @return 操作前所处流程节点名
	 */
	public String getFromPosition() {
		return fromPosition;
	}

	/**
	 * @param 操作前所处流程节点名
	 */
	public void setFromPosition( String fromPosition ) {
		this.fromPosition = fromPosition;
	}

	/**
	 * @return 操作前所处流程节点ID
	 */
	public int getLastFlowNode() {
		return lastFlowNode;
	}

	/**
	 * @param 操作前所处流程节点ID
	 */
	public void setLastFlowNode( int lastFlowNode ) {
		this.lastFlowNode = lastFlowNode;
	}

	/**
	 * @return Returns the operation.
	 */
	public String getOperation() {
		return operation;
	}

	/**
	 * @param operation The operation to set.
	 */
	public void setOperation( String operation ) {
		this.operation = operation;
	}

	/**
	 * @return Returns the operator.
	 */
	public String getOperator() {
		return operator;
	}

	/**
	 * @param operator The operator to set.
	 */
	public void setOperator( String operator ) {
		this.operator = operator;
	}

	/**
	 * @return Returns the operatorID.
	 */
	public int getOperatorID() {
		return operatorID;
	}

	/**
	 * @param operatorID The operatorID to set.
	 */
	public void setOperatorID( int operatorID ) {
		this.operatorID = operatorID;
	}

	/**
	 * @return Returns the startTime.
	 */
	public Timestamp getStartTime() {
		return startTime;
	}

	/**
	 * @param startTime The startTime to set.
	 */
	public void setStartTime( Timestamp startTime ) {
		this.startTime = startTime;
	}

	/**
	 * @return Returns the toPosition.
	 */
	public String getToPosition() {
		return toPosition;
	}

	/**
	 * @param toPosition The toPosition to set.
	 */
	public void setToPosition( String toPosition ) {
		this.toPosition = toPosition;
	}

	public boolean equals( Object obj ) {
		if ( obj instanceof FlowRecord ) {
			return ( frID == ( ( FlowRecord ) obj ).frID );
		}
		return false;
	}

	public int hashCode() {
		return ( int ) frID;
	}

	public String toString() {
		StringBuffer bf = new StringBuffer();
		Field[] fields = this.getClass().getDeclaredFields();
		for ( int i = 0; i < fields.length; i++ ) {
			Field field = fields[i];
			field.setAccessible( true );
			Object value = null;
			try {
				value = field.get( this );
			} catch ( Exception e ) {
			}
			bf.append( field.getName() ).append( "=" ).append( value ).append(
					"\n" );
		}
		return bf.toString();
	}
}
