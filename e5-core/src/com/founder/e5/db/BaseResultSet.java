package com.founder.e5.db;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.founder.e5.db.util.CloseHelper;

/**
 * 对IResultSet的初步实现，保留与具体数据库相关的方法给具体类实现
 * 
 * @version 1.0
 * @created 08-七月-2005 11:06:01
 */
class BaseResultSet extends ResultSetWrapper implements IResultSet {

	/**
	 * 工厂方法
	 * 
	 * @param rs
	 * @return
	 */
	static IResultSet createResultSet( ResultSet rs ) {
		return new BaseResultSet( rs );
	}

	// ------------------------------------------------------------------------

	/**
	 * 构造函数
	 * 
	 * @param ResultSet
	 */
	public BaseResultSet( ResultSet underlying ) {
		super( underlying );
	}

	// ------------------------------------------------------------------------

	/**
	 * 获取IClob类型数据
	 * 
	 * @param i i
	 * @throws IOException
	 */
	public IClob getClob2( int i ) throws SQLException, IOException {
		return LobHelper.getClob(underlying, i);
	}

	/**
	 * 获取IClob类型数据
	 * 
	 * @param columnName columnName
	 * @throws IOException
	 */
	public IClob getClob2( String columnName ) throws SQLException, IOException {
		return LobHelper.getClob(underlying, columnName);
	}

	/**
	 * 获取IBlob类型数据
	 * 
	 * @param i i
	 * @throws IOException
	 */
	public IBlob getBlob2( int i ) throws SQLException, IOException {
		return LobHelper.getBlob(underlying, i);
	}

	/**
	 * 获取IBlob类型数据
	 * 
	 * @param columnName columnName
	 * @throws IOException
	 */
	public IBlob getBlob2( String columnName ) throws SQLException, IOException {
		return LobHelper.getBlob(underlying, columnName);
	}

	/**
	 * 获取IBfile类型数据<br>
	 * 这里只是简单的取回对象，由BfileFactoryManager负责转化为IBfile对象
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
	 * 获取IBfile类型数据<br>
	 * 这里只是简单的取回对象，由BfileFactoryManager负责转化为IBfile对象
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
	 * 关闭底层结果集和与之关联的语句
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
