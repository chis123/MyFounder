package com.founder.e5.db;

/**
 * 通过DBSession.executeUpdate方法插入或更新bfile（或oracle的clob/blob等需要先执行字段更新，再进行数据传输的数据类型）时，若数据传输过程中出错，则抛出该异常
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
