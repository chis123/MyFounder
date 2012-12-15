/**
 * $Id: e5project com.founder.e5.doc DocID.java 
 * created on 2005-8-2 15:50:31
 * by liyanhui
 */
package com.founder.e5.doc;

/**
 * 文档标识对象，类似一个C中的struct，包含文档库ID和文档ID
 * 
 * @author liyanhui
 * @version 1.0
 * @created 2005-8-2 15:50:31
 */
public final class DocID {

	/**
	 * 文档库ID
	 */
	public int docLibID;
	
	/**
	 * 文档ID
	 */
	public long docID;
	
	/**
	 * Default Constructor.
	 */
	public DocID() {
	};

	/**
	 * 构造一个文档标识对象
	 * @param docid 文档库ID
	 * @param libID
	 */
	public DocID(int docLibID, long docID) {
		this.docID = docID;
		this.docLibID = docLibID;
	}

	public boolean equals( Object obj ) {
		if ( obj instanceof DocID ) {
			DocID another = ( DocID ) obj;
			return ( another.docID == docID && another.docLibID == docLibID );
		}
		return false;
	}

	public int hashCode() {
		return ( int ) docID;
	}

	public String toString() {
		return new StringBuffer().append( "[docLibID=" ).append( docLibID )
				.append( ", docID=" ).append( docID ).append( "]" ).toString();
	}

}
