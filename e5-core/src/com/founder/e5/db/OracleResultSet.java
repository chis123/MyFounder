/*
 * Created on 2005-7-13 10:59:41
 * 
 */
package com.founder.e5.db;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * IResultSetµÄOracleÊµÏÖ
 * 
 * @author liyanhui
 * @version 1.0
 * @created 2005-7-13 10:59:41
 * @deprecated replaced by BaseResultSet
 */
class OracleResultSet extends BaseResultSet {

	/**
	 * @param rs
	 */
	public OracleResultSet( ResultSet rs ) {
		super( rs );
	}

	/**
	 * @see com.founder.e5.db.IResultSet#getBfile(int)
	 */
	public IBfile getBfile( int i ) throws SQLException {
		oracle.sql.BFILE bfile = ( oracle.sql.BFILE ) underlying.getObject( i );
		return BfileFactoryManager.convert( bfile );
	}

	/**
	 * @see com.founder.e5.db.IResultSet#getBfile(java.lang.String)
	 */
	public IBfile getBfile( String columnName ) throws SQLException {
		oracle.sql.BFILE bfile = ( oracle.sql.BFILE ) underlying.getObject( columnName );
		return BfileFactoryManager.convert( bfile );
	}

}
