/**
 * $Id: e5new com.founder.e5.db.dialect SQLServerDialect.java created on
 * 2006-3-29 11:11:29 by liyanhui
 */
package com.founder.e5.db.dialect;

import java.sql.Types;

import com.founder.e5.db.DBType;

/**
 * A dialect for Microsoft SQL Server 2000
 * 
 * @author liyanhui
 * @version 1.0
 * @created 2006-3-29 11:11:29
 */
public class SQLServerDialect extends SybaseDialect {

	public SQLServerDialect() {
		registerColumnType( Types.VARBINARY, "image" );
		registerColumnType( Types.VARBINARY, 8000, "varbinary($l)" );
	}

	/**
	 * @see com.founder.e5.db.dialect.Dialect#getDBType()
	 */
	public String getDBType() {
		return DBType.SQLSERVER;
	}

	public boolean supportsLimit() {
		return true;
	}

	static int getAfterSelectInsertPoint( String sql ) {
		sql = sql.toLowerCase();
		int selectIndex = sql.indexOf( "select" );
		final int selectDistinctIndex = sql.indexOf( "select distinct" );
		return selectIndex + ( selectDistinctIndex == selectIndex ? 15 : 6 );
	}

	public String getLimitString( String querySelect, int offset, int limit ) {
		if ( offset > 0 )
			throw new UnsupportedOperationException( "sql server has no offset" );
		
		return new StringBuffer( querySelect.length() + 8 ).append( querySelect ).insert(
				getAfterSelectInsertPoint( querySelect ),
				" top " + limit ).toString();
	}

}
