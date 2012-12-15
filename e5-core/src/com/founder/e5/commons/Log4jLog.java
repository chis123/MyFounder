/**
 * $Id: e5project_vss com.founder.e5.commons Log4jLog.java 
 * created on 2006-2-9 9:30:11
 * by liyanhui
 */
package com.founder.e5.commons;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * ����Log4jʵ�ֵ���־����
 * 
 * @author liyanhui
 * @version 1.0
 * @created 2006-2-9 9:30:11
 */
class Log4jLog implements Log {

	private static final String FQCN = Log4jLog.class.getName();

	/**
	 * �����
	 */
	private String category;

	/**
	 * Log4j����־ʵ��
	 */
	private Logger logger;

	/**
	 * ����־����Ӧ�ĸ������
	 */
	private String traceCategory;

	/**
	 * ����־����Ӧ���������
	 */
	private String performanceCategory;

	/**
	 * ����һ��Log4jLogʵ��
	 * 
	 * @param category �����
	 * @param logger Log4j����־ʵ��
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
	// ���ٺ����ܼ����Ϣ��������Ϊһ����𣬵��޼���ĸ��������ʵ���ж�ֻʹ��log4j�е�info����

	/**
	 * @see com.founder.e5.commons.Log#isTraceEnabled()
	 */
	public boolean isTraceEnabled() {
		return logger.isEnabledFor( Level.TRACE );
		// ���︴����log4j�е�TRACE�����ʵ�����Լ��ṩ��
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
	 * ���ɵ�ǰ����ʱλ����Ϣ����TraceManager��ѯ��ǰλ���Ƿ������������������Ӧ�������
	 * 
	 * @param message �û�������Ϣ
	 * @param t ���������λ����Ϣ��Throwable����
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
	 * ���ظ���־�������
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return category;
	}

	// -----------------------------------------------------------------------

	/**
	 * ���ɱ���־����Ӧ��trace���<br>
	 * ���磺�����Ϊ��e5.flow�������Ӧ��trace���Ϊ��trace.flow��
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
	 * ���ɱ���־����Ӧ��performance���<br>
	 * ���磺�����Ϊ��e5.flow�������Ӧ��performance���Ϊ��performance.flow��
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
	 * ���ɵ����ߵ�����ʱ��λ��Ϣ
	 * 
	 * @return ����ʱ��λ��Ϣ
	 */
	private static final RTLocation generateRTLocation( Throwable t ) {
		StackTraceElement[] ste = t.getStackTrace();

		// ��ִ��new Throwable()�ķ����ĽǶ�������
		// ��i=0���ǵ�ǰִ�е㣬��������
		// ��i=1���ǵ����߷����������ñ������ķ���
		// ��i=2���ǵ����ߵĵ�����
		StackTraceElement caller = ste[1];
		String thread = Thread.currentThread().getName();
		String codeLocation = caller.getClassName() + "."
				+ caller.getMethodName();
		
		return ( new RTLocation( thread, codeLocation ) );
	}

}
