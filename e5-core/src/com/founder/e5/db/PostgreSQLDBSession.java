package com.founder.e5.db;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.founder.e5.db.dialect.Dialect;
import com.founder.e5.db.util.CloseHelper;

/**
 * PostgreSQL 特殊处理
 * 
 * @author wanghc
 * @created 2009-3-20 下午05:02:26
 */
public class PostgreSQLDBSession extends BaseDBSession
{
	public PostgreSQLDBSession(){
		this.dialect = Dialect.getDialect(DBType.POSTGRESQL);
	}
	
	/* (non-Javadoc)
	 * @see com.founder.e5.db.BaseDBSession#getClob(java.lang.String, java.lang.Object[])
	 */
	public IClob getClob(String sql, Object[] params) throws SQLException, IOException
	{
		ResultSet rs = executeQuery0( sql, params, null );
		try {
			if ( rs.next() )
				return LobHelper.createClob(LobHelper.readClob( rs.getCharacterStream(1)));	
		} finally {
			Statement pmt = rs.getStatement();
			CloseHelper.closeQuietly( rs );
			CloseHelper.closeQuietly( pmt );
		}
		return null;
	}
	/**
	 * @see com.founder.e5.db.DBSession#executeQuery(java.lang.String,
	 *      java.lang.Object[], int[])
	 */
	public IResultSet executeQuery( String sql, Object[] params, int[] types )
			throws SQLException {
		ResultSet rs = executeQuery0( sql, params, types );
		return new PostgreSQLResultSet( rs );
	}

	/**
	 * @see com.founder.e5.db.DBSession#executeQuery(java.lang.String,
	 *      java.lang.Object[], int[], int, int)
	 */
	public IResultSet executeQuery( String sql, Object[] params, int[] types,
			int resultSetType, int resultSetConcurrency ) throws SQLException {
		ResultSet rs = executeQuery0(
				sql,
				params,
				types,
				resultSetType,
				resultSetConcurrency );
		return new PostgreSQLResultSet( rs );
	}
	
	/* (non-Javadoc)
	 * @see com.founder.e5.db.BaseDBSession#getBlob(java.lang.String, java.lang.Object[])
	 */
	public IBlob getBlob(String sql, Object[] params) throws SQLException, IOException
	{
		ResultSet rs = executeQuery0( sql, params, null );
		try {
			if ( rs.next() )
				return LobHelper.createBlob(LobHelper.readBlob(rs.getBinaryStream(1)));
		} finally {
			Statement pmt = rs.getStatement();
			CloseHelper.closeQuietly( rs );
			CloseHelper.closeQuietly( pmt );
		}
		return null;
	}
}
