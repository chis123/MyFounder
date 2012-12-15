/*
 * Created on 2005-6-21 16:57:53
 *
 */
package com.founder.e5.doc;

import java.lang.reflect.Field;
import java.sql.Timestamp;

/**
 * ���̼�¼�����˶��ĵ���һ�β�������Ϣ��<br>
 * �ĵ�ʵ�������̼�¼ʵ����һ�Զ�Ĺ�ϵ
 * 
 * @author liyanhui
 * @version 1.0
 * @created 2005-6-21 16:57:53
 */
public class FlowRecord {

	private long frID; // ���̼�¼ʵ������ݿ��ʶ

	private long docID;// ��Ӧ���ĵ�ID

	private int docLibID;// ��Ӧ���ĵ������ĵ����ID

	private int operatorID;// ������ID�����û����������

	private String operator;// ������

	private String operation;// ������

	private Timestamp startTime;// ������ʼʱ��

	private Timestamp endTime;// ��������ʱ��

	private String fromPosition;// ����ǰ�������̽ڵ���

	private String toPosition;// �������������̽ڵ�

	private int lastFlowNode;// ����ǰ�������̽ڵ�ID

	private int currentFlowNode;// �������������̽ڵ�ID

	private String detail;// ������ϸ������Ϣ

	/**
	 * 
	 */
	public FlowRecord() {
		super();
	}

	/**
	 * @return �������������̽ڵ�ID
	 */
	public int getCurrentFlowNode() {
		return currentFlowNode;
	}

	/**
	 * @param �������������̽ڵ�ID
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

	// docLibID���ܱ��ͻ��޸�
	public void setDocLibID( int docLibID ) {
		this.docLibID = docLibID;
	}

	// docID���ܱ��ͻ��޸�
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

	// frID���ܱ��ͻ��޸�
	/**
	 * @param frID The frID to set.
	 */
	void setFrID( long frID ) {
		this.frID = frID;
	}

	/**
	 * @return ����ǰ�������̽ڵ���
	 */
	public String getFromPosition() {
		return fromPosition;
	}

	/**
	 * @param ����ǰ�������̽ڵ���
	 */
	public void setFromPosition( String fromPosition ) {
		this.fromPosition = fromPosition;
	}

	/**
	 * @return ����ǰ�������̽ڵ�ID
	 */
	public int getLastFlowNode() {
		return lastFlowNode;
	}

	/**
	 * @param ����ǰ�������̽ڵ�ID
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
