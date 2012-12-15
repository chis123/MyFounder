/**
 * $Id: e5project_vss com.founder.e5.commons LogFactory.java 
 * created on 2006-2-10 14:45:00
 * by liyanhui
 */
package com.founder.e5.commons;

import org.apache.log4j.Logger;

/**
 * E5 Log����
 * 
 * @author liyanhui
 * @version 1.0
 * @created 2006-2-10 14:45:00
 */
public class LogFactory {

	/**
	 * ��ȡ��־ʵ��
	 * 
	 * @param category ��־�����
	 * @return
	 */
	public static Log getLog( String category ) {
		Logger logger = Logger.getLogger( category );
		return new Log4jLog( category, logger );
	}

}
