
package com.founder.e5.db;

import gnu.trove.TIntArrayList;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.founder.e5.db.util.CloseHelper;

/**
 * DBQuery基本实现
 * 
 * @author liyanhui
 * @version 1.0
 * @created 2006-4-5 15:04:24
 */
public class BaseDBQuery implements DBQuery {

	/**
	 * 工厂方法
	 * 
	 * @param DBSession示例
	 * @param sql语句
	 * @return
	 */
	public static DBQuery getInstance( DBSession dbsess, String sql ) {
		return new BaseDBQuery( dbsess, sql );
	}

	/**
	 * 此变量保存了创建自己的DBSession对象
	 */
	protected DBSession owner;

	/**
	 * this对象对应的sql语句
	 */
	protected String sql;

	/**
	 * 待绑定参数值
	 */
	protected ArrayList paramValues = new ArrayList();

	/**
	 * 绑定参数类型，顺序与paramValues对应
	 */
	protected TIntArrayList paramTypes = new TIntArrayList();

	/**
	 * @param owner
	 */
	public BaseDBQuery( DBSession owner ) {
		this.owner = owner;
	}

	/**
	 * @param owner
	 * @param sql
	 */
	public BaseDBQuery( DBSession owner, String sql ) {
		this.owner = owner;
		this.sql = sql;
	}

	/**
	 * @see com.founder.e5.db.DBQuery#setInt(int, int)
	 */
	public DBQuery setInt( int position, int value ) {
		paramValues.add( position - 1, new Integer( value ) );
		paramTypes.insert( position - 1, Types.INTEGER );
		return this;
	}

	/**
	 * @see com.founder.e5.db.DBQuery#setLong(int, long)
	 */
	public DBQuery setLong( int position, long value ) {
		paramValues.add( position - 1, new Long( value ) );
		paramTypes.insert( position - 1, Types.BIGINT );
		return this;
	}

	/**
	 * @see com.founder.e5.db.DBQuery#setFloat(int, float)
	 */
	public DBQuery setFloat( int position, float value ) {
		paramValues.add( position - 1, new Float( value ) );
		paramTypes.insert( position - 1, Types.FLOAT );
		return this;
	}

	/**
	 * @see com.founder.e5.db.DBQuery#setDouble(int, double)
	 */
	public DBQuery setDouble( int position, double value ) {
		paramValues.add( position - 1, new Double( value ) );
		paramTypes.insert( position - 1, Types.DOUBLE );
		return this;
	}

	/**
	 * @see com.founder.e5.db.DBQuery#setString(int, java.lang.String)
	 */
	public DBQuery setString( int position, String value ) {
		paramValues.add( position - 1, value );
		paramTypes.insert( position - 1, Types.VARCHAR );
		return this;
	}

	/**
	 * @see com.founder.e5.db.DBQuery#setDate(int, java.sql.Date)
	 */
	public DBQuery setDate( int position, Date value ) {
		paramValues.add( position - 1, value );
		paramTypes.insert( position - 1, Types.DATE );
		return this;
	}

	/**
	 * @see com.founder.e5.db.DBQuery#setTime(int, java.sql.Time)
	 */
	public DBQuery setTime( int position, Time value ) {
		paramValues.add( position - 1, value );
		paramTypes.insert( position - 1, Types.TIME );
		return this;
	}

	/**
	 * @see com.founder.e5.db.DBQuery#setTimestamp(int, java.sql.Timestamp)
	 */
	public DBQuery setTimestamp( int position, Timestamp value ) {
		paramValues.add( position - 1, value );
		paramTypes.insert( position - 1, Types.TIMESTAMP );
		return this;
	}

	/**
	 * @see com.founder.e5.db.DBQuery#setObject(int, Object)
	 */
	public DBQuery setObject( int position, Object value ) {
		paramValues.add( position - 1, value );

		int type = TypeMapping.map( value );
		paramTypes.insert( position - 1, type );
		return this;
	}

	/**
	 * @see com.founder.e5.db.DBQuery#setNull(int, int)
	 */
	public DBQuery setNull( int position, int sqlType ) {
		paramValues.add( position - 1, null );
		paramTypes.insert( position - 1, sqlType );
		return this;
	}

	/**
	 * @throws SQLException
	 * @see com.founder.e5.db.DBQuery#executeQuery()
	 */
	public IResultSet executeQuery() throws SQLException {
		return owner.executeQuery(
				sql,
				paramValues.toArray(),
				paramTypes.toNativeArray() );
	}

	/**
	 * @throws SQLException
	 * @see com.founder.e5.db.DBQuery#executeUpdate()
	 */
	public int executeUpdate() throws SQLException {
		return owner.executeUpdate(
				sql,
				paramValues.toArray(),
				paramTypes.toNativeArray() );
	}

	/**
	 * @throws SQLException
	 * @see com.founder.e5.db.DBQuery#list()
	 */
	public List list() throws SQLException {
		ArrayList result = new ArrayList();

		ResultSet rs = executeQuery();
		ResultSetMetaData rsmd = rs.getMetaData();
		int cnt = rsmd.getColumnCount();

		try {
			while ( rs.next() ) {
				ArrayList row = new ArrayList( cnt );
				for ( int i = 0; i < cnt; i++ ) {
					Object value = rs.getObject( i + 1 );
					row.add( value );
				}
				result.add(row);
			}
		} finally {
			CloseHelper.closeQuietly( rs );
		}

		return result;
	}

	/**
	 * @throws SQLException
	 * @see com.founder.e5.db.DBQuery#iterator()
	 */
	public Iterator iterator() throws SQLException {
		return list().iterator();
	}

}
