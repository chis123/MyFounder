/*
 * Created on 2005-6-22 16:17:59
 *
 */
package com.founder.e5.doc;

/**
 * DocumentManagerʵ���Ĺ���
 * @author liyanhui
 * @version 1.0
 * @created 2005-6-22 16:17:59
 */
public class DocumentManagerFactory {
	
	private static final DocumentManager instance = new DocumentManagerImpl();

    /**
     * @return DocumentManager����
     */
    public static DocumentManager getInstance() {
    	return instance;
    }
}
