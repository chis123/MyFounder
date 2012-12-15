package com.founder.e5.db.util;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import com.founder.e5.db.DBSession;
import com.founder.e5.db.DBType;
import com.founder.e5.db.meta.ColumnMetaData;
import com.founder.e5.db.meta.TableMetaData;

/**
 * 该类用于根据JDBC提供的ResultSetMetaData探测表元数据信息
 * 
 * @author liyanhui
 * @version 1.0
 * @created 2006-4-28 9:49:27
 */
public class MetaDataUtils {

	/**
	 * 通过"select * from $TABLE"查询语句和结果集元数据获得表元数据
	 * 
	 * @param TableMetaData
	 * @param sess
	 * @param tablename
	 * @return TableMetaData
	 * @throws SQLException
	 */
	public static TableMetaData fillMetaData( TableMetaData tmd,
			DBSession sess, String tablename ) throws SQLException {
		HashMap columns = null;
		String sql = "select * from " + tablename;

		ResultSet rs = sess.executeQuery( sql );

		try {
			ResultSetMetaData rsmd = rs.getMetaData();
			int cnt = rsmd.getColumnCount();

			for ( int i = 1; i < cnt + 1; i++ ) {
				String name = rsmd.getColumnName( i ).toUpperCase();

				if ( columns == null )
					columns = new HashMap( cnt );

				ColumnMetaData cmd = new ColumnMetaData( name );

				cmd.setType( rsmd.getColumnType( i ) );
				cmd.setTypeName( rsmd.getColumnTypeName( i ) );
				cmd.setDataLength( rsmd.getColumnDisplaySize( i ) );
				cmd.setDataScale( rsmd.getScale( i ) );
				cmd.setDataPrecision( rsmd.getPrecision( i ) );
				if ( rsmd.isNullable( i ) == ResultSetMetaData.columnNoNulls )
					cmd.setNullable( false );

				columns.put( name, cmd );
			}

		} finally {
			CloseHelper.closeQuietly( rs );
		}

		if ( tmd == null )
			tmd = new TableMetaData( tablename );

		tmd.setColumns( columns );

		// 尝试探测主键信息

		// JDBC meta data方式
		tmd.setIdColumns( getPKColumns( sess, tablename ) );

		// 上面的方式不行的话
		if ( tmd.getIdColumns().length == 0 ) {

			if ( DBType.ORACLE.equals( sess.getDialect().getDBType() ) ) {
				try {
					String[] ids = getOraclePKColumn( sess, tablename );
					tmd.setIdColumns( ids );
				} catch ( SQLException e ) {
					// ignore this
				}
			}

			else if ( DBType.SQLSERVER.equals( sess.getDialect().getDBType() ) ) {
				try {
					String[] ids = getSqlServerPKColumn( sess, tablename );
					tmd.setIdColumns( ids );
				} catch ( SQLException e ) {
					// ignore this
				}
			}

		}

		return tmd;
	}

	/**
	 * 通过JDBC meta data获取主键信息
	 * 
	 * @param sess
	 * @param tablename
	 * @return
	 * @throws SQLException
	 */
	static String[] getPKColumns( DBSession sess, String tablename )
			throws SQLException {
		ArrayList list = new ArrayList();
		DatabaseMetaData dbmd = sess.getConnection().getMetaData();
		ResultSet rs = dbmd.getPrimaryKeys( null, null, tablename );
		while ( rs.next() ) {
			list.add( rs.getString( "COLUMN_NAME" ).toUpperCase() );
		}
		rs.close();

		return ( String[] ) list.toArray( new String[ list.size() ] );
	}

	/**
	 * 取得给定表的主键字段
	 * 
	 * @param sess 数据库连接 注意：该连接的用户必须有查询sys.dba_cons_columns的权限；否则将报告表或视图不存在错误
	 * @param tablename 表名
	 * @return String[]
	 * @throws SQLException
	 */
	static String[] getOraclePKColumn( DBSession sess, String tablename )
			throws SQLException {
		StringBuffer sql = new StringBuffer();
		sql.append(
				"select distinct t1.column_name from sys.dba_cons_columns t1, dba_constraints t2 " ).append(
				"where t1.constraint_name=t2.constraint_name and " ).append(
				"t1.table_name='" ).append( tablename.toUpperCase() ).append(
				"'" );
		sql.append( " and t2.constraint_type='P'" );

		ArrayList list = new ArrayList();
		ResultSet rs = sess.executeQuery( sql.toString() );
		while ( rs.next() ) {
			list.add( rs.getString( 1 ) );
		}
		rs.close();

		return ( String[] ) list.toArray( new String[ list.size() ] );
	}

	/**
	 * 取得给定表的主键字段
	 * 
	 * @param sess 数据库连接
	 *            注意：该连接的用户必须有查询INFORMATION_SCHEMA.KEY_COLUMN_USAGE的权限；否则将报告表或视图不存在错误
	 * @param tablename 表名
	 * @return String[]
	 * @throws SQLException
	 */
	static String[] getSqlServerPKColumn( DBSession sess, String tablename )
			throws SQLException {
		StringBuffer sql = new StringBuffer();
		sql.append(
				"select distinct column_name from INFORMATION_SCHEMA.KEY_COLUMN_USAGE " ).append(
				"where table_name = '" ).append( tablename ).append(
				"' and CONSTRAINT_NAME like 'PK_%'" );

		ArrayList list = new ArrayList();
		ResultSet rs = sess.executeQuery( sql.toString() );
		while ( rs.next() ) {
			list.add( rs.getString( 1 ) );
		}
		rs.close();

		return ( String[] ) list.toArray( new String[ list.size() ] );
	}

}
