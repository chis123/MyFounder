/**
 * $Id: e5new com.founder.e5.db.dialect Oracle9Dialect.java created on 2006-3-29
 * 11:06:27 by liyanhui
 */
package com.founder.e5.db.dialect;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import oracle.sql.BFILE;

import com.founder.e5.db.BfileFactoryManager;
import com.founder.e5.db.ColumnInfo;
import com.founder.e5.db.DBType;
import com.founder.e5.db.DataType;
import com.founder.e5.db.IBfile;
import com.founder.e5.db.IBlob;
import com.founder.e5.db.IClob;
import com.founder.e5.db.MappingException;

/**
 * An SQL dialect for Oracle 9 (uses ANSI-style syntax where possible).
 * 
 * @author liyanhui
 * @version 1.0
 * @created 2006-3-29 11:06:27
 */
public class Oracle9Dialect extends CommonDialect {

	/**
	 * 
	 */
	public Oracle9Dialect() {
		super();
		registerColumnType( Types.BIT, "number(1,0)" );
		registerColumnType( Types.TINYINT, "number(3,0)" );
		registerColumnType( Types.SMALLINT, "number(5,0)" );
		registerColumnType( Types.INTEGER, "number($l,0)" );
		registerColumnType( Types.BIGINT, "number($l,0)" );
		registerColumnType( Types.CHAR, "char($l)" );
		registerColumnType( Types.VARCHAR, 4000, "varchar2($l)" );
		registerColumnType( Types.VARCHAR, "long" );
		//registerColumnType( Types.FLOAT, "float" );
		registerColumnType( Types.FLOAT, "number($l,$p)" );
		registerColumnType( Types.DOUBLE, "double precision" );
		registerColumnType( Types.DATE, "date" );
		registerColumnType( Types.TIME, "date" );
		registerColumnType( Types.TIMESTAMP, "timestamp" );
		registerColumnType( Types.VARBINARY, 2000, "raw($l)" );
		registerColumnType( Types.VARBINARY, "long raw" );
		registerColumnType( Types.NUMERIC, "number($p,$s)" );
		registerColumnType( Types.BLOB, "blob" );
		registerColumnType( Types.CLOB, "clob" );
	}

	/**
	 * @see com.founder.e5.db.dialect.Dialect#supportsBfile()
	 */
	public boolean supportsBfile() {
		return true;
	}

	/**
	 * @see com.founder.e5.db.dialect.Dialect#supportsSequences()
	 */
	public boolean supportsSequences() {
		return true;
	}

	/**
	 * @see com.founder.e5.db.dialect.Dialect#getSelectSequenceNextValString(java.lang.String)
	 */
	public String getSelectSequenceNextValString( String sequenceName )
			throws MappingException {
		StringBuffer sb = new StringBuffer();
		sb.append( "select " ).append( sequenceName ).append(
				".nextval from dual" );
		return sb.toString();
	}

	/**
	 * @see com.founder.e5.db.dialect.Dialect#getPlaceHolder(java.lang.String)
	 * @deprecated
	 */
	public String getPlaceHolder( String e5DataType ) {
		// TODO：以后增加可配置属性：是否用oracle的bfile类型取代e5的extfile类型

		if ( DataType.EXTFILE.equals( e5DataType ) )
			return "bfilename(?, ?)";
		return "?";
	}

	public String getEmptyClobSql() {
		return "empty_clob()";
	}

	public String getEmptyBlobSql() {
		return "empty_blob()";
	}

	/**
	 * @see com.founder.e5.db.dialect.Dialect#getDBType()
	 */
	public String getDBType() {
		return DBType.ORACLE;
	}

	/**
	 * @see com.founder.e5.db.dialect.Dialect#getBfile(java.sql.ResultSet,
	 *      java.lang.String)
	 */
	public IBfile getBfile( ResultSet rs, String name ) throws SQLException,
			IOException {
		Object value = rs.getObject( name );
		BFILE bfile = ( BFILE ) value;
		return BfileFactoryManager.convert( bfile );
	}

	public void writeBlob( long id, String tablename, String idColumnName,
			String lobColumnName, IBlob blob ) throws SQLException, IOException {
		super.writeBlob( id, tablename, idColumnName, lobColumnName, blob );
	}

	public void writeClob( long id, String tablename, String idColumnName,
			String lobColumnName, IClob clob ) throws SQLException, IOException {
		super.writeClob( id, tablename, idColumnName, lobColumnName, clob );
	}

	public boolean supportsLimit() {
		return true;
	}

	public String getLimitString( String sql, boolean hasOffset ) {

		sql = sql.trim();
		boolean isForUpdate = false;
		if ( sql.toLowerCase().endsWith( " for update" ) ) {
			sql = sql.substring( 0, sql.length() - 11 );
			isForUpdate = true;
		}

		StringBuffer pagingSelect = new StringBuffer( sql.length() + 100 );
		if ( hasOffset ) {
			pagingSelect.append( "select * from ( select row_.*, rownum rownum_ from ( " );
		} else {
			pagingSelect.append( "select * from ( " );
		}
		pagingSelect.append( sql );
		if ( hasOffset ) {
			pagingSelect.append( " ) row_ where rownum <= ?) where rownum_ > ?" );
		} else {
			pagingSelect.append( " ) where rownum <= ?" );
		}

		if ( isForUpdate )
			pagingSelect.append( " for update" );

		return pagingSelect.toString();
	}

	public String getLimitString( String sql, int offset, int limit ) {

		sql = sql.trim();
		boolean hasOffset = ( offset > 0 );
		boolean isForUpdate = false;
		if ( sql.toLowerCase().endsWith( " for update" ) ) {
			sql = sql.substring( 0, sql.length() - 11 );
			isForUpdate = true;
		}

		StringBuffer pagingSelect = new StringBuffer( sql.length() + 100 );
		if ( hasOffset ) {
			pagingSelect.append( "select * from ( select row_.*, rownum rownum_ from ( " );
		} else {
			pagingSelect.append( "select * from ( " );
		}
		pagingSelect.append( sql );
		if ( hasOffset ) {
			pagingSelect.append( " ) row_ where rownum <= " ).append( limit ).append(
					") where rownum_ > " ).append( offset );
		} else {
			pagingSelect.append( " ) where rownum <= " ).append( limit );
		}

		if ( isForUpdate )
			pagingSelect.append( " for update" );

		return pagingSelect.toString();
	}
	
	/* (non-Javadoc)
	 * @see com.founder.e5.db.dialect.Dialect#getAddColumnDDL(com.founder.e5.db.ColumnInfo[], java.lang.String)
	 */
	public String getAddColumnDDL(ColumnInfo[] columnInfoArray, String tableName)
	{
		return getAddColumnDDL2(columnInfoArray, tableName);
	}
}
