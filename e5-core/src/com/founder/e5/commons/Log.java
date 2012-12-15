/**
 * $Id: e5project_vss com.founder.e5.commons Log.java 
 * created on 2006-1-22 11:33:26
 * by liyanhui
 */
package com.founder.e5.commons;

/**
 * E5系统日志服务、性能监控服务、动态跟踪服务统一接口
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
	// 注意：下面的方法用于提供跟踪和性能监控功能，与上面的日志功能无关，也跟当前日志所属的类别无关

	/**
	 * 跟踪机制是否已打开？<br>
	 * 调用该方法以避免可能的生成字符串参数的消耗
	 */
	public boolean isTraceEnabled();

	/**
	 * 记录跟踪日志<br>
	 * 注意：跟踪信息包含当前堆栈轨迹信息
	 */
	public void trace();

	/**
	 * 记录跟踪日志
	 * 
	 * @param 跟踪信息
	 */
	public void trace( Object message );

	/**
	 * 性能监控机制是否已打开？<br>
	 * 调用该方法以避免可能的生成字符串参数的消耗
	 */
	public boolean isPerformanceEnabled();

	/**
	 * 记录性能监控日志
	 * 
	 * @param message 性能信息
	 */
	public void performance( Object message );

}
