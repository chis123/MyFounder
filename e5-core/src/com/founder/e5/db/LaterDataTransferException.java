package com.founder.e5.db;

/**
 * ͨ��DBSession.executeUpdate������������bfile����oracle��clob/blob����Ҫ��ִ���ֶθ��£��ٽ������ݴ�����������ͣ�ʱ�������ݴ�������г������׳����쳣
 * 
 * @author liyanhui
 * @version 1.0
 * @created 2006-4-21 11:04:21
 */
public class LaterDataTransferException extends RuntimeException {

	private static final long serialVersionUID = 1924615117114641271L;

	/**
	 * 
	 */
	public LaterDataTransferException() {
		super();
	}

	/**
	 * @param message
	 * @param cause
	 */
	public LaterDataTransferException( String message, Throwable cause ) {
		super( message, cause );
	}

	/**
	 * @param message
	 */
	public LaterDataTransferException( String message ) {
		super( message );
	}

	/**
	 * @param cause
	 */
	public LaterDataTransferException( Throwable cause ) {
		super( cause );
	}

}
