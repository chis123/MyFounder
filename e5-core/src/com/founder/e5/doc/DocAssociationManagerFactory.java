/**
 * $Id: e5project com.founder.e5.doc DocAssociationManagerFactory.java 
 * created on 2005-7-21 9:57:55
 * by liyanhui
 */
package com.founder.e5.doc;

/**
 * DocAssociationManager实例的工厂
 * @author liyanhui
 * @version 1.0
 * @created 2005-7-21 9:57:55
 */
public class DocAssociationManagerFactory {
	
	private static final DocAssociationManager instance = new DocAssociationManagerImpl();

	/**
	 * @return DocAssociationManager单例
	 * @created 2005-7-21 9:58:38
	 */
	public static DocAssociationManager getInstance() {
		return instance;
	}

}
