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
 * �������ڸ���JDBC�ṩ��ResultSetMetaData̽���Ԫ������Ϣ
 * 
 * @author liyanhui
 * @version 1.0
 * @created 2006-4-28 9:49:27
 */
public class MetaDataUtils {

	/**
	 * ͨ��"select * from $TABLE"��ѯ���ͽ����Ԫ���ݻ�ñ�Ԫ����
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

		// ����̽��������Ϣ

		// JDBC meta data��ʽ
		tmd.setIdColumns( getPKColumns( sess, tablename ) );

		// ����ķ�ʽ���еĻ�
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
	 * ͨ��JDBC meta data��ȡ������Ϣ
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
	 * ȡ�ø�����������ֶ�
	 * 
	 * @param sess ���ݿ����� ע�⣺�����ӵ��û������в�ѯsys.dba_cons_columns��Ȩ�ޣ����򽫱�������ͼ�����ڴ���
	 * @param tablename ����
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
	 * ȡ�ø�����������ֶ�
	 * 
	 * @param sess ���ݿ�����
	 *            ע�⣺�����ӵ��û������в�ѯINFORMATION_SCHEMA.KEY_COLUMN_USAGE��Ȩ�ޣ����򽫱�������ͼ�����ڴ���
	 * @param tablename ����
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
