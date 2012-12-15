/**
 * $Id: e5new com.founder.e5.db MappingException.java 
 * created on 2006-3-29 10:37:27
 * by liyanhui
 */
package com.founder.e5.db;

/**
 * 映射异常，经常出现于配置期间
 * 
 * @author liyanhui
 * @version 1.0
 * @created 2006-3-29 10:37:27
 */
public class MappingException extends RuntimeException {

	private static final long serialVersionUID = -3213956285338990890L;

	/**
	 * @param message
	 */
	public MappingException( String message ) {
		super( message );
	}

}
