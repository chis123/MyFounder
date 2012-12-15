package com.founder.e5.db;

/**
 * �ӳٳ�ʼ���쳣����ʾ�ڱ�Ӧ�ڳ�ʼ���׶γ��֣���ʵ��ʵ��ʱΪ������ܶ�����Ҫʱ��ִ�г�ʼ�����̣�����������г��ֵ��쳣��<br>
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
