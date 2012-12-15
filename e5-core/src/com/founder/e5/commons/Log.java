/**
 * $Id: e5project_vss com.founder.e5.commons Log.java 
 * created on 2006-1-22 11:33:26
 * by liyanhui
 */
package com.founder.e5.commons;

/**
 * E5ϵͳ��־�������ܼ�ط��񡢶�̬���ٷ���ͳһ�ӿ�
 * 
 * @author liyanhui
 * @version 1.0
 * @created 2006-1-22 11:33:26
 */
public interface Log {

	/**
	 * <p>
	 * Is debug logging currently enabled?
	 * </p>
	 * 
	 * <p>
	 * Call this method to prevent having to perform expensive operations (for
	 * example, <code>String</code> concatination) when the log level is more
	 * than debug.
	 * </p>
	 */
	public boolean isDebugEnabled();

	/**
	 * <p>
	 * Is info logging currently enabled?
	 * </p>
	 * 
	 * <p>
	 * Call this method to prevent having to perform expensive operations (for
	 * example, <code>String</code> concatination) when the log level is more
	 * than info.
	 * </p>
	 */
	public boolean isInfoEnabled();

	/**
	 * <p>
	 * Is warning logging currently enabled?
	 * </p>
	 * 
	 * <p>
	 * Call this method to prevent having to perform expensive operations (for
	 * example, <code>String</code> concatination) when the log level is more
	 * than warning.
	 * </p>
	 */
	public boolean isWarnEnabled();

	/**
	 * <p>
	 * Is error logging currently enabled?
	 * </p>
	 * 
	 * <p>
	 * Call this method to prevent having to perform expensive operations (for
	 * example, <code>String</code> concatination) when the log level is more
	 * than error.
	 * </p>
	 */
	public boolean isErrorEnabled();

	// -------------------------------------------------------- Logging Methods

	/**
	 * <p>
	 * Log a message with debug log level.
	 * </p>
	 * 
	 * @param message log this message
	 */
	public void debug( Object message );

	/**
	 * <p>
	 * Log an error with debug log level.
	 * </p>
	 * 
	 * @param message log this message
	 * @param t log this cause
	 */
	public void debug( Object message, Throwable t );

	/**
	 * <p>
	 * Log a message with info log level.
	 * </p>
	 * 
	 * @param message log this message
	 */
	public void info( Object message );

	/**
	 * <p>
	 * Log an error with info log level.
	 * </p>
	 * 
	 * @param message log this message
	 * @param t log this cause
	 */
	public void info( Object message, Throwable t );

	/**
	 * <p>
	 * Log a message with warn log level.
	 * </p>
	 * 
	 * @param message log this message
	 */
	public void warn( Object message );

	/**
	 * <p>
	 * Log an error with warn log level.
	 * </p>
	 * 
	 * @param message log this message
	 * @param t log this cause
	 */
	public void warn( Object message, Throwable t );

	/**
	 * <p>
	 * Log a message with error log level.
	 * </p>
	 * 
	 * @param message log this message
	 */
	public void error( Object message );

	/**
	 * <p>
	 * Log an error with error log level.
	 * </p>
	 * 
	 * @param message log this message
	 * @param t log this cause
	 */
	public void error( Object message, Throwable t );

	// ----------------------------------------------------------------------
	// ע�⣺����ķ��������ṩ���ٺ����ܼ�ع��ܣ����������־�����޹أ�Ҳ����ǰ��־����������޹�

	/**
	 * ���ٻ����Ƿ��Ѵ򿪣�<br>
	 * ���ø÷����Ա�����ܵ������ַ�������������
	 */
	public boolean isTraceEnabled();

	/**
	 * ��¼������־<br>
	 * ע�⣺������Ϣ������ǰ��ջ�켣��Ϣ
	 */
	public void trace();

	/**
	 * ��¼������־
	 * 
	 * @param ������Ϣ
	 */
	public void trace( Object message );

	/**
	 * ���ܼ�ػ����Ƿ��Ѵ򿪣�<br>
	 * ���ø÷����Ա�����ܵ������ַ�������������
	 */
	public boolean isPerformanceEnabled();

	/**
	 * ��¼���ܼ����־
	 * 
	 * @param message ������Ϣ
	 */
	public void performance( Object message );

}
