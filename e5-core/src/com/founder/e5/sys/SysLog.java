package com.founder.e5.sys;

import java.sql.Timestamp;

/**
 * @version 1.0
 * @updated 08-七月-2005 15:06:53
 */
public class SysLog {
	/**ID*/
	private int logID;
	/**日志类型*/
	private String logType;

	/**操作员*/
	private String operator;

	/**操作*/
	private String operation;

	/**子系统描述*/
	private String appDescription;

	/**详细描述信息*/
	private String description;

	/**完成时间*/
	private String finishTime;

	/**操作用户主机*/
	private String userHost;

	public int getLogID() {
		return logID;
	}

	public void setLogID(int id) {
		this.logID = id;
	}

	/**
	 * @return 返回 appDescription。
	 */
	public String getAppDescription() {
		return appDescription;
	}

	/**
	 * @return 返回 description。
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return 返回 finishTime。
	 */
	public String getFinishTime() {
		return finishTime;
	}

	/**
	 * @return 返回 logType。
	 */
	public String getLogType() {
		return logType;
	}

	/**
	 * @return 返回 operation。
	 */
	public String getOperation() {
		return operation;
	}

	/**
	 * @return 返回 operator。
	 */
	public String getOperator() {
		return operator;
	}

	/**
	 * @return 返回 userHost。
	 */
	public String getUserHost() {
		return userHost;
	}

	/**
	 * @param appDescription 要设置的 appDescription。
	 */
	public void setAppDescription( String appDescription ) {
		this.appDescription = appDescription;
	}

	/**
	 * @param description 要设置的 description。
	 */
	public void setDescription( String description ) {
		this.description = description;
	}

	/**
	 * @param finishTime 要设置的 finishTime。
	 */
	public void setFinishTime( String finishTime ) {
		this.finishTime = finishTime;
	}

	/**
	 * @param logType 要设置的 logType。
	 */
	public void setLogType( String logType ) {
		this.logType = logType;
	}

	/**
	 * @param operation 要设置的 operation。
	 */
	public void setOperation( String operation ) {
		this.operation = operation;
	}

	/**
	 * @param operator 要设置的 operator。
	 */
	public void setOperator( String operator ) {
		this.operator = operator;
	}

	/**
	 * @param userHost 要设置的 userHost。
	 */
	public void setUserHost( String userHost ) {
		this.userHost = userHost;
	}

	/**
	 * 因为FSYS_SYSLOG表声明所有字段不能为空，为了让本实体对象能插入到数据库，该方法检<br>
	 * 查除finishTime外所有别的属性，如发现其值为null则置为'-'；而若finishTime为null，<br>
	 * 则置其值为当前系统时间
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
