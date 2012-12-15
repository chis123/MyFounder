package com.founder.e5.doc.exception;

/**
 * ��ʾ�ӳټ��������ʵ�ʼ���ʱ����
 * 
 * @author liyanhui
 * @version 1.0
 * @created 2005-7-20 11:17:27
 */
public class LazyLoadException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public LazyLoadException() {
		super();
	}

	/**
	 * @param message
	 */
	public LazyLoadException( String message ) {
		super( message );
	}

	/**
	 * @param cause
	 */
	public LazyLoadException( Throwable cause ) {
		super( cause );
	}

	/**
	 * @param message
	 * @param cause
	 */
	public LazyLoadException( String message, Throwable cause ) {
		super( message, cause );
	}

}
