/**
 * $Id: e5new com.founder.e5.db.dialect SybaseDialect.java 
 * created on 2006-3-29 11:10:17
 * by liyanhui
 */
package com.founder.e5.db.dialect;

import java.sql.Types;

import com.founder.e5.db.DBType;

/**
 * An SQL dialect compatible with Sybase and MS SQL Server.
 * 
 * @author liyanhui
 * @version 1.0
 * @created 2006-3-29 11:10:17
 */
public class SybaseDialect extends CommonDialect {

	public SybaseDialect() {
		super();
		registerColumnType( Types.BIT, "tinyint" ); // Sybase BIT type does not
		// support null values
		registerColumnType( Types.BIGINT, "numeric(19,0)" );
		registerColumnType( Types.SMALLINT, "smallint" );
		registerColumnType( Types.TINYINT, "tinyint" );
		registerColumnType( Types.INTEGER, "int" );
		registerColumnType( Types.CHAR, "char($l)" );
		registerColumnType( Types.VARCHAR, "varchar($l)" );
		registerColumnType( Types.FLOAT, "float" );
		registerColumnType( Types.DOUBLE, "double precision" );
		registerColumnType( Types.DATE, "datetime" );
		registerColumnType( Types.TIME, "datetime" );
		registerColumnType( Types.TIMESTAMP, "datetime" );
		registerColumnType( Types.VARBINARY, "varbinary($l)" );
		registerColumnType( Types.NUMERIC, "numeric($p,$s)" );
		registerColumnType( Types.BLOB, "image" );
		registerColumnType( Types.CLOB, "text" );
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.db.dialect.Dialect#getDBType()
	 */
	public String getDBType() {
		return DBType.SYBASE;
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.db.dialect.Dialect#supportsLimit()
	 */
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
			throw new UnsupportedOperationException( "sybase has no offset" );
		
		return new StringBuffer( querySelect.length() + 8 ).append( querySelect ).insert(
				getAfterSelectInsertPoint( querySelect ),
				" top " + limit ).toString();
	}

}
