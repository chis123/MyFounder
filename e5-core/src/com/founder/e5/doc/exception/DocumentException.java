/*
 * Created on 2005-7-7 12:52:19
 *
 */
package com.founder.e5.doc.exception;

import com.founder.e5.context.E5Exception;

/**
 * E5docģ������쳣����ģ�������������쳣�ĸ��ࡣ
 * ���쳣�����쳣�������E5docģ�������
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
