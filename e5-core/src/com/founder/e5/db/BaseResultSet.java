package com.founder.e5.db;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.founder.e5.db.util.CloseHelper;

/**
 * ��IResultSet�ĳ���ʵ�֣�������������ݿ���صķ�����������ʵ��
 * 
 * @version 1.0
 * @created 08-����-2005 11:06:01
 */
class BaseResultSet extends ResultSetWrapper implements IResultSet {

	/**
	 * ��������
	 * 
	 * @param rs
	 * @return
	 */
	static IResultSet createResultSet( ResultSet rs ) {
		return new BaseResultSet( rs );
	}

	// ------------------------------------------------------------------------

	/**
	 * ���캯��
	 * 
	 * @param ResultSet
	 */
	public BaseResultSet( ResultSet underlying ) {
		super( underlying );
	}

	// ------------------------------------------------------------------------

	/**
	 * ��ȡIClob��������
	 * 
	 * @param i i
	 * @throws IOException
	 */
	public IClob getClob2( int i ) throws SQLException, IOException {
		return LobHelper.getClob(underlying, i);
	}

	/**
	 * ��ȡIClob��������
	 * 
	 * @param columnName columnName
	 * @throws IOException
	 */
	public IClob getClob2( String columnName ) throws SQLException, IOException {
		return LobHelper.getClob(underlying, columnName);
	}

	/**
	 * ��ȡIBlob��������
	 * 
	 * @param i i
	 * @throws IOException
	 */
	public IBlob getBlob2( int i ) throws SQLException, IOException {
		return LobHelper.getBlob(underlying, i);
	}

	/**
	 * ��ȡIBlob��������
	 * 
	 * @param columnName columnName
	 * @throws IOException
	 */
	public IBlob getBlob2( String columnName ) throws SQLException, IOException {
		return LobHelper.getBlob(underlying, columnName);
	}

	/**
	 * ��ȡIBfile��������<br>
	 * ����ֻ�Ǽ򵥵�ȡ�ض�����BfileFactoryManager����ת��ΪIBfile����
	 * 
	 * @see com.founder.e5.db.IResultSet#getBfile(int)
	 */
	public IBfile getBfile( int i ) throws SQLException {
		Object obj = underlying.getObject( i );
		if ( obj == null )
			return null;

		return BfileFactoryManager.convert( obj );
	}

	/**
	 * ��ȡIBfile��������<br>
	 * ����ֻ�Ǽ򵥵�ȡ�ض�����BfileFactoryManager����ת��ΪIBfile����
	 * 
	 * @see com.founder.e5.db.IResultSet#getBfile(java.lang.String)
	 */
	public IBfile getBfile( String columnName ) throws SQLException {
		Object obj = underlying.getObject( columnName );
		if ( obj == null )
			return null;

		return BfileFactoryManager.convert( obj );
	}

	// ------------------------------------------------------------------------

	/**
	 * �رյײ���������֮���������
	 */
	public void close() throws SQLException {
		if ( underlying != null ) {
			Statement stmt = underlying.getStatement();

			try {
				CloseHelper.closeQuietly( underlying );
			} finally {
				CloseHelper.closeQuietly( stmt );
			}
		}
	}

}
