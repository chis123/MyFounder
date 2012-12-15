/**
 * $Id: e5project com.founder.e5.db BfileException.java 
 * created on 2005-8-5 14:01:00
 * by liyanhui
 */
package com.founder.e5.db;

/**
 * 表示访问bfile时出的错误
 * 
 * @author liyanhui
 * @version 1.0
 * @created 2005-8-5 14:01:00
 */
public class BfileException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public BfileException() {
		super();
	}

	/**
	 * @param message
	 * @param cause
	 */
	public BfileException( String message, Throwable cause ) {
		super( message, cause );
	}

	/**
	 * @param message
	 */
	public BfileException( String message ) {
		super( message );
	}

	/**
	 * @param cause
	 */
	public BfileException( Throwable cause ) {
		super( cause );
	}

}
