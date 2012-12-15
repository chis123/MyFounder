
package com.founder.e5.db;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;

/**
 * 该类对象保存了执行一条数据库查询（或更新、删除）语句过程中的状态。<br>
 * 该类对象经由DBSession.createQuery( sql )创建
 * 
 * @author liyanhui
 * @version 1.0
 * @created 2006-4-5 14:50:48
 */
public interface DBQuery {

	/**
	 * 设置int值
	 * 
	 * @param position 参数位置，从1开始
	 * @param value 参数值
	 * @return 该对象自身
	 */
	public DBQuery setInt( int position, int value );

	/**
	 * 设置long值
	 * 
	 * @param position 参数位置，从1开始
	 * @param value 参数值
	 * @return 该对象自身
	 */
	public DBQuery setLong( int position, long value );

	/**
	 * 设置float值
	 * 
	 * @param position 参数位置，从1开始
	 * @param value 参数值
	 * @return 该对象自身
	 */
	public DBQuery setFloat( int position, float value );

	/**
	 * 设置double值
	 * 
	 * @param position 参数位置，从1开始
	 * @param value 参数值
	 * @return 该对象自身
	 */
	public DBQuery setDouble( int position, double value );

	/**
	 * 设置字符串值
	 * 
	 * @param position 参数位置，从1开始
	 * @param value 参数值
	 * @return 该对象自身
	 */
	public DBQuery setString( int position, String value );

	/**
	 * 设置日期值
	 * 
	 * @param position 参数位置，从1开始
	 * @param value 参数值
	 * @return 该对象自身
	 */
	public DBQuery setDate( int position, Date value );

	/**
	 * 设置时间值
	 * 
	 * @param position 参数位置，从1开始
	 * @param value 参数值
	 * @return 该对象自身
	 */
	public DBQuery setTime( int position, Time value );

	/**
	 * 设置时间戳值
	 * 
	 * @param position 参数位置，从1开始
	 * @param value 参数值
	 * @return 该对象自身
	 */
	public DBQuery setTimestamp( int position, Timestamp value );

	/**
	 * 设置对象值
	 * 
	 * @param position 参数位置，从1开始
	 * @param value 参数值
	 * @return 该对象自身
	 */
	public DBQuery setObject( int position, Object value );

	/**
	 * 绑定空参数
	 * 
	 * @param position 参数位置，从1开始
	 * @param sqlType 类型码,由java.sql.Types定义
	 * @return 该对象自身
	 */
	public DBQuery setNull( int position, int sqlType );

	/**
	 * 执行查询语句，返回结果集
	 * 
	 * @return
	 * @throws SQLException
	 */
	public IResultSet executeQuery() throws SQLException;

	/**
	 * 执行更新语句，返回改变的行数
	 * 
	 * @return
	 * @throws SQLException
	 */
	public int executeUpdate() throws SQLException;

	/**
	 * 执行查询语句，返回结果集
	 * 
	 * @return List（其中的元素类型为List，即一行列值）
	 * @throws SQLException
	 */
	public List list() throws SQLException;

	/**
	 * 执行查询语句，返回结果集
	 * 
	 * @return Iterator（其中的元素类型为List，即一行列值）
	 * @throws SQLException
	 */
	public Iterator iterator() throws SQLException;

}
