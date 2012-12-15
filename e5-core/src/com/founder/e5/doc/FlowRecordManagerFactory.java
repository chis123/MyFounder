/**
 * $Id: e5project com.founder.e5.doc FlowRecordManagerFactory.java 
 * created on 2005-7-21 9:56:47
 * by liyanhui
 */
package com.founder.e5.doc;

/**
 * FlowRecordManagerʵ���Ĺ���
 * @author liyanhui
 * @version 1.0
 * @created 2005-7-21 9:56:47
 */
public class FlowRecordManagerFactory {
	
	private static final FlowRecordManager instance = new FlowRecordManagerImpl();

	/**
	 * @return FlowRecordManager����
	 * @created 2005-7-21 9:57:33
	 */
	public static FlowRecordManager getInstance() {
		return instance;
	}

}
