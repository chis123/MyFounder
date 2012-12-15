/*
 * Created on 2005-7-7 12:52:19
 *
 */
package com.founder.e5.doc.exception;

import com.founder.e5.context.E5Exception;

/**
 * E5doc模块基本异常，是模块内所有其他异常的父类。
 * 该异常表明异常情况出在E5doc模块操作上
 * 
 * @author liyanhui
 * @version 1.0
 * @created 2005-7-7 12:52:19
 */
public class DocumentException extends E5Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DocumentException(int _errorCode, String message, Throwable cause) {
		super(_errorCode, message, cause);
	}

	public DocumentException(int _errorCode, String message) {
		super(_errorCode, message);
	}

	public DocumentException(int _errorCode, Throwable cause) {
		super(_errorCode, cause);
	}

	public DocumentException(String message, Throwable cause) {
		super(message, cause);
	}

	public DocumentException(String message) {
		super(message);
	}

	public DocumentException(Throwable cause) {
		super(cause);
	}

}
