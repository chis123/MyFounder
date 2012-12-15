/**
 * $Id: e5project com.founder.e5.db DBException.java 
 * created on 2005-7-16 12:40:45
 * by liyanhui
 */
package com.founder.e5.context;


/**
 * 代表与数据库有关的异常
 * @author liyanhui
 * @version 1.0
 * @created 2005-7-16 12:40:45
 */
public class DBException extends E5Exception {
	private static final long serialVersionUID = 1L;

	/**
	 * @param message
	 */
	public DBException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public DBException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public DBException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param _errorCode
	 * @param message
	 */
	public DBException(int _errorCode, String message) {
		super(_errorCode, message);
	}

	/**
	 * @param _errorCode
	 * @param cause
	 */
	public DBException(int _errorCode, Throwable cause) {
		super(_errorCode, cause);
	}

	/**
	 * @param _errorCode
	 * @param message
	 * @param cause
	 */
	public DBException(int _errorCode, String message, Throwable cause) {
		super(_errorCode, message, cause);
	}

}
