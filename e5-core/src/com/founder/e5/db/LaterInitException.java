package com.founder.e5.db;

/**
 * 延迟初始化异常。表示在本应在初始化阶段出现，但实际实现时为提高性能而在需要时才执行初始化过程，在这个过程中出现的异常。<br>
 * 
 * @author liyanhui
 * @version 1.0
 * @created 2006-8-20 13:14:14
 */
public class LaterInitException extends RuntimeException {

	private static final long serialVersionUID = -8564180075073402808L;

	public LaterInitException() {
		super();
	}

	/**
	 * @param message
	 * @param cause
	 */
	public LaterInitException( String message, Throwable cause ) {
		super( message, cause );
	}

	/**
	 * @param message
	 */
	public LaterInitException( String message ) {
		super( message );
	}

	/**
	 * @param cause
	 */
	public LaterInitException( Throwable cause ) {
		super( cause );
	}

}
