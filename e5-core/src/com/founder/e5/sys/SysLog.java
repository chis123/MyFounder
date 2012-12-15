package com.founder.e5.sys;

import java.sql.Timestamp;

/**
 * @version 1.0
 * @updated 08-����-2005 15:06:53
 */
public class SysLog {
	/**ID*/
	private int logID;
	/**��־����*/
	private String logType;

	/**����Ա*/
	private String operator;

	/**����*/
	private String operation;

	/**��ϵͳ����*/
	private String appDescription;

	/**��ϸ������Ϣ*/
	private String description;

	/**���ʱ��*/
	private String finishTime;

	/**�����û�����*/
	private String userHost;

	public int getLogID() {
		return logID;
	}

	public void setLogID(int id) {
		this.logID = id;
	}

	/**
	 * @return ���� appDescription��
	 */
	public String getAppDescription() {
		return appDescription;
	}

	/**
	 * @return ���� description��
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return ���� finishTime��
	 */
	public String getFinishTime() {
		return finishTime;
	}

	/**
	 * @return ���� logType��
	 */
	public String getLogType() {
		return logType;
	}

	/**
	 * @return ���� operation��
	 */
	public String getOperation() {
		return operation;
	}

	/**
	 * @return ���� operator��
	 */
	public String getOperator() {
		return operator;
	}

	/**
	 * @return ���� userHost��
	 */
	public String getUserHost() {
		return userHost;
	}

	/**
	 * @param appDescription Ҫ���õ� appDescription��
	 */
	public void setAppDescription( String appDescription ) {
		this.appDescription = appDescription;
	}

	/**
	 * @param description Ҫ���õ� description��
	 */
	public void setDescription( String description ) {
		this.description = description;
	}

	/**
	 * @param finishTime Ҫ���õ� finishTime��
	 */
	public void setFinishTime( String finishTime ) {
		this.finishTime = finishTime;
	}

	/**
	 * @param logType Ҫ���õ� logType��
	 */
	public void setLogType( String logType ) {
		this.logType = logType;
	}

	/**
	 * @param operation Ҫ���õ� operation��
	 */
	public void setOperation( String operation ) {
		this.operation = operation;
	}

	/**
	 * @param operator Ҫ���õ� operator��
	 */
	public void setOperator( String operator ) {
		this.operator = operator;
	}

	/**
	 * @param userHost Ҫ���õ� userHost��
	 */
	public void setUserHost( String userHost ) {
		this.userHost = userHost;
	}

	/**
	 * ��ΪFSYS_SYSLOG�����������ֶβ���Ϊ�գ�Ϊ���ñ�ʵ������ܲ��뵽���ݿ⣬�÷�����<br>
	 * ���finishTime�����б�����ԣ��緢����ֵΪnull����Ϊ'-'������finishTimeΪnull��<br>
	 * ������ֵΪ��ǰϵͳʱ��
	 */
	public void makeDBInsertable() {
		if ( this.logType == null )
			logType = "-";
		if ( this.operator == null )
			this.operator = "-";
		if ( this.operation == null )
			this.operation = "-";
		if ( this.appDescription == null )
			this.appDescription = "-";
		if ( this.description == null )
			this.description = "-";
		if ( this.userHost == null )
			this.userHost = "-";
		if ( this.finishTime == null )
			this.finishTime = new Timestamp( System.currentTimeMillis() )
					.toString();
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		if ( finishTime != null )
			sb.append( "finishTime=" ).append( finishTime ).append( "\n" );
		if ( logType != null )
			sb.append( "logType=" ).append( logType ).append( "\n" );
		if ( operator != null )
			sb.append( "operator=" ).append( operator ).append( "\n" );
		if ( operation != null )
			sb.append( "operation=" ).append( operation ).append( "\n" );
		if ( userHost != null )
			sb.append( "userHost=" ).append( userHost ).append( "\n" );
		if ( appDescription != null )
			sb.append( "appDescription=" ).append( appDescription ).append(
					"\n" );
		if ( description != null )
			sb.append( "description=" ).append( description ).append( "\n" );
		return sb.toString();
	}

}
