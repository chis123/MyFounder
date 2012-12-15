package com.founder.e5.db;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 对java.sql.ResultSet的包装，提供从JDBC结果集中直接获取IClob、IBlob、IBfile对象的接口。<br>
 * <br>
 * 目前的实现返回的对象不占据数据库连接资源，可以脱机使用
 * 
 * @created 04-七月-2005 13:32:09
 * @version 1.0
 */
public interface IResultSet extends ResultSet {

	/**
	 * 获得IClob类型数据<br>
	 * <br>
	 * 该方法把java.sql.Clob对象包装为IClob
	 * 
	 * @param i
	 * @throws SQLException
	 * @throws IOException
	 */
	public IClob getClob2( int i ) throws SQLException, IOException;

	/**
	 * 获得IClob类型数据<br>
	 * <br>
	 * 该方法把java.sql.Clob对象包装为IClob
	 * 
	 * @param columnName
	 * @throws SQLException
	 * @throws IOException
	 */
	public IClob getClob2( String columnName ) throws SQLException, IOException;

	/**
	 * 获得IBlob类型数据<br>
	 * <br>
	 * 该方法把java.sql.Blob对象包装为IBlob
	 * 
	 * @param i
	 * @throws SQLException
	 * @throws IOException
	 */
	public IBlob getBlob2( int i ) throws SQLException, IOException;

	/**
	 * 获得IBlob类型数据<br>
	 * <br>
	 * 该方法把java.sql.Blob对象包装为IBlob
	 * 
	 * @param columnName
	 * @throws SQLException
	 * @throws IOException
	 */
	public IBlob getBlob2( String columnName ) throws SQLException, IOException;

	/**
	 * 获得IBfile类型数据<br>
	 * <br>
	 * 该方法从ResultSet.getObject()得到原始数据对象，交由BfileFactoryManager转换
	 * 
	 * @param i
	 * @throws SQLException
	 */
	public IBfile getBfile( int i ) throws SQLException;

	/**
	 * 获得IBfile类型数据<br>
	 * <br>
	 * 该方法从ResultSet.getObject()得到原始数据对象，交由BfileFactoryManager转换
	 * 
	 * @param columnName
	 * @throws SQLException
	 */
	public IBfile getBfile( String columnName ) throws SQLException;

}
