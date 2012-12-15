/**
 * $Id: e5project com.founder.e5.doc.exception DifferentGroupException.java 
 * created on 2005-8-3 9:39:26
 * by liyanhui
 */
package com.founder.e5.doc.exception;

import com.founder.e5.context.E5Exception;

/**
 * 创建组关联关系时两个文档属不同组时抛出该异常
 * @author liyanhui
 * @version 1.0
 * @created 2005-8-3 9:39:26
 */
public class DifferentGroupException extends E5Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param message
	 */
	public DifferentGroupException(String message) {
		super( message );
	}

	/**
	 * @param cause
	 */
	public DifferentGroupException(Throwable cause) {
		super( cause );
	}

	/**
	 * @param message
	 * @param cause
	 */
	public DifferentGroupException(String message, Throwable cause) {
		super( message, cause );
	}

	/**
	 * @param _errorCode
	 * @param message
	 */
	public DifferentGroupException(int _errorCode, String message) {
		super( _errorCode, message );
	}

	/**
	 * @param _errorCode
	 * @param cause
	 */
	public DifferentGroupException(int _errorCode, Throwable cause) {
		super( _errorCode, cause );
	}

	/**
	 * @param _errorCode
	 * @param message
	 * @param cause
	 */
	public DifferentGroupException(int _errorCode, String message,
			Throwable cause) {
		super( _errorCode, message, cause );
	}

}
