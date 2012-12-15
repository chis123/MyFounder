/**
 * $Id: e5project_vss com.founder.e5.commons Log4jLog.java 
 * created on 2006-2-9 9:30:11
 * by liyanhui
 */
package com.founder.e5.commons;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * 采用Log4j实现的日志功能
 * 
 * @author liyanhui
 * @version 1.0
 * @created 2006-2-9 9:30:11
 */
class Log4jLog implements Log {

	private static final String FQCN = Log4jLog.class.getName();

	/**
	 * 类别名
	 */
	private String category;

	/**
	 * Log4j的日志实例
	 */
	private Logger logger;

	/**
	 * 该日志类别对应的跟踪类别
	 */
	private String traceCategory;

	/**
	 * 该日志类别对应的性能类别
	 */
	private String performanceCategory;

	/**
	 * 创建一个Log4jLog实例
	 * 
	 * @param category 类别名
	 * @param logger Log4j的日志实例
	 */
	public Log4jLog( String category, Logger logger ) {
		this.category = category;
		this.logger = logger;

		this.traceCategory = generateTraceCategory();
		this.performanceCategory = generatePerformanceCategory();
	}

	/**
	 * @see com.founder.e5.commons.Log#isDebugEnabled()
	 */
	public boolean isDebugEnabled() {
		return logger.isDebugEnabled();
	}

	/**
	 * @see com.founder.e5.commons.Log#isInfoEnabled()
	 */
	public boolean isInfoEnabled() {
		return logger.isInfoEnabled();
	}

	/**
	 * @see com.founder.e5.commons.Log#isWarnEnabled()
	 */
	public boolean isWarnEnabled() {
		return logger.isEnabledFor( Level.WARN );
	}

	/**
	 * @see com.founder.e5.commons.Log#isErrorEnabled()
	 */
	public boolean isErrorEnabled() {
		return logger.isEnabledFor( Level.ERROR );
	}

	/**
	 * @see com.founder.e5.commons.Log#debug(java.lang.Object)
	 */
	public void debug( Object message ) {
		logger.log( FQCN, Level.DEBUG, message, null );
	}

	/**
	 * @see com.founder.e5.commons.Log#debug(java.lang.Object,
	 *      java.lang.Throwable)
	 */
	public void debug( Object message, Throwable t ) {
		logger.log( FQCN, Level.DEBUG, message, t );
	}

	/**
	 * @see com.founder.e5.commons.Log#info(java.lang.Object)
	 */
	public void info( Object message ) {
		logger.log( FQCN, Level.INFO, message, null );
	}

	/**
	 * @see com.founder.e5.commons.Log#info(java.lang.Object,
	 *      java.lang.Throwable)
	 */
	public void info( Object message, Throwable t ) {
		logger.log( FQCN, Level.INFO, message, t );
	}

	/**
	 * @see com.founder.e5.commons.Log#warn(java.lang.Object)
	 */
	public void warn( Object message ) {
		logger.log( FQCN, Level.WARN, message, null );
	}

	/**
	 * @see com.founder.e5.commons.Log#warn(java.lang.Object,
	 *      java.lang.Throwable)
	 */
	public void warn( Object message, Throwable t ) {
		logger.log( FQCN, Level.WARN, message, t );
	}

	/**
	 * @see com.founder.e5.commons.Log#error(java.lang.Object)
	 */
	public void error( Object message ) {
		logger.log( FQCN, Level.ERROR, message, null );
	}

	/**
	 * @see com.founder.e5.commons.Log#error(java.lang.Object,
	 *      java.lang.Throwable)
	 */
	public void error( Object message, Throwable t ) {
		logger.log( FQCN, Level.ERROR, message, t );
	}

	// -----------------------------------------------------------------------
	// 跟踪和性能监控信息，各自作为一种类别，但无级别的概念；它们在实现中都只使用log4j中的info级别

	/**
	 * @see com.founder.e5.commons.Log#isTraceEnabled()
	 */
	public boolean isTraceEnabled() {
		return logger.isEnabledFor( Level.TRACE );
		// 这里复用了log4j中的TRACE概念，但实现是自己提供的
	}

	/**
	 * @see com.founder.e5.commons.Log#trace()
	 */
	public void trace() {
		_trace( "", new Throwable() );
	}

	/**
	 * @see com.founder.e5.commons.Log#trace(java.lang.Object)
	 */
	public void trace( Object message ) {
		_trace( message, new Throwable() );
	}

	/**
	 * 生成当前运行时位置信息，向TraceManager查询当前位置是否允许输出，如是则按相应配置输出
	 * 
	 * @param message 用户附加信息
	 * @param t 包含输出点位置信息的Throwable对象
	 */
	private void _trace( Object message, Throwable t ) {
		RTLocation rtl = generateRTLocation( t );
		ProbeConfig cfg = TraceManager.queryCfg( rtl );
		if ( cfg.isEnabled() ) {
			Logger traceLogger = Logger.getLogger( traceCategory );
			if ( cfg.isNeedStackInfo() )
				traceLogger.info( message, t );
			else
				traceLogger.info( message );
		}
	}

	/**
	 * @see com.founder.e5.commons.Log#isPerformanceEnabled()
	 */
	public boolean isPerformanceEnabled() {
		Logger performanceLogger = Logger.getLogger( performanceCategory );
		return performanceLogger.isInfoEnabled();
	}

	/**
	 * @see com.founder.e5.commons.Log#performance(java.lang.Object)
	 */
	public void performance( Object message ) {
		Logger performanceLogger = Logger.getLogger( performanceCategory );
		performanceLogger.info( message );
	}

	/**
	 * 返回该日志的类别名
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return category;
	}

	// -----------------------------------------------------------------------

	/**
	 * 生成本日志类别对应的trace类别<br>
	 * 例如：本类别为“e5.flow”，则对应的trace类别为“trace.flow”
	 * 
	 * @return
	 */
	private String generateTraceCategory() {
		if ( category.startsWith( "e5." ) )
			return category.replaceFirst( "e5", "trace" );
		else
			return "trace." + category;
	}

	/**
	 * 生成本日志类别对应的performance类别<br>
	 * 例如：本类别为“e5.flow”，则对应的performance类别为“performance.flow”
	 * 
	 * @return
	 */
	private String generatePerformanceCategory() {
		if ( category.startsWith( "e5." ) )
			return category.replaceFirst( "e5", "performance" );
		else
			return "performance." + category;
	}

	// -----------------------------------------------------------------------

	/**
	 * 生成调用者的运行时定位信息
	 * 
	 * @return 运行时定位信息
	 */
	private static final RTLocation generateRTLocation( Throwable t ) {
		StackTraceElement[] ste = t.getStackTrace();

		// 从执行new Throwable()的方法的角度来看：
		// （i=0）是当前执行点，即本方法
		// （i=1）是调用者方法，即调用本方法的方法
		// （i=2）是调用者的调用者
		StackTraceElement caller = ste[1];
		String thread = Thread.currentThread().getName();
		String codeLocation = caller.getClassName() + "."
				+ caller.getMethodName();
		
		return ( new RTLocation( thread, codeLocation ) );
	}

}
