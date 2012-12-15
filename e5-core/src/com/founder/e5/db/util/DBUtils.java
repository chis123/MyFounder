package com.founder.e5.db.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * 该类提供与JDBC操作有关的工具方法
 * 
 * @author liyanhui
 * @version 1.0
 * @created 2006-8-30 10:16:38
 */
public class DBUtils {

	/**
	 * 从结果集中取与bean属性同名的列值绑定到bean属性。
	 * 
	 * @param rs
	 * @param beanClass bean类，即返回对象的类型
	 * @return
	 * @throws SQLException
	 */
	public static Object bindBean( ResultSet rs, Class beanClass )
			throws SQLException {
		return bindBean( rs, beanClass, ( Map ) null );
	}

	/**
	 * 从结果集中读取当前行，根据bean属性到表列名的映射关系获取列值并绑定到bean属性。<br>
	 * <br>
	 * 注意：处理某个属性时，若找不到该属性的映射关系，则默认列名与属性名相同；<br>
	 * 另外，该方法不绑定<I>static</I>、<I>final</I> 属性。
	 * 
	 * @param rs
	 * @param beanClass
	 * @param mapping 格式："key1=value1;key2=value2;key3=value3"
	 * @return
	 * @throws SQLException
	 */
	public static Object bindBean( ResultSet rs, Class beanClass, String mapping )
			throws SQLException {
		HashMap map = new HashMap();
		String[] ss = mapping.split( ";" );
		for ( int i = 0; i < ss.length; i++ ) {
			String string = ss[i];
			String[] ss2 = string.split( "=" );
			map.put( ss2[0], ss2[1] );
		}
		return bindBean( rs, beanClass, map );
	}

	/**
	 * 从结果集中读取当前行，根据bean属性到表列名的映射关系获取列值并绑定到bean属性。<br>
	 * <br>
	 * 注意：处理某个属性时，若找不到该属性的映射关系，则默认列名与属性名相同；<br>
	 * 另外，该方法不绑定<I>static</I>、<I>final</I> 属性。
	 * 
	 * @param rs
	 * @param beanClass
	 * @param mapping bean属性到表列名的映射关系（可为null）
	 * @return 装配好的bean
	 * @throws SQLException
	 */
	public static Object bindBean( ResultSet rs, Class beanClass, Map mapping )
			throws SQLException {
		Object bean = null;

		try {
			bean = beanClass.newInstance();

			Field[] fields = beanClass.getDeclaredFields();
			for ( int i = 0; i < fields.length; i++ ) {
				Field field = fields[i];
				int modifier = field.getModifiers();

				// 这里只绑定实例属性
				if ( Modifier.isStatic( modifier ) )
					continue;

				// 跳过final属性
				if ( Modifier.isFinal( modifier ) )
					continue;

				String name = field.getName();
				Class type = field.getType();

				String columnName = name;
				if ( mapping != null ) {
					String s = ( String ) mapping.get( name );
					if ( s != null )
						columnName = s;
				}

				Object columnValue = getValue( rs, columnName, type );

				field.setAccessible( true );
				field.set( bean, columnValue );
			}

		} catch ( InstantiationException e ) {
			throw new RuntimeException( e );
		} catch ( IllegalAccessException e ) {
			throw new RuntimeException( e );
		}

		return bean;
	}

	/**
	 * 根据期望的类型从结果集中获取值
	 * 
	 * @param rs 结果集
	 * @param name 列名
	 * @param expectType 期望的值类型
	 * @return expectType类型对象
	 * @throws SQLException
	 */
	public static Object getValue( ResultSet rs, String name, Class expectType )
			throws SQLException {

		if ( expectType == Integer.class || expectType == Integer.TYPE )
			return new Integer( rs.getInt( name ) );

		if ( expectType == Long.class || expectType == Long.TYPE )
			return new Long( rs.getLong( name ) );

		if ( expectType == Float.class || expectType == Float.TYPE )
			return new Float( rs.getFloat( name ) );

		if ( expectType == Double.class || expectType == Double.TYPE )
			return new Double( rs.getDouble( name ) );

		if ( expectType == Boolean.class || expectType == Boolean.TYPE )
			return new Boolean( rs.getBoolean( name ) );

		if ( expectType == Character.class || expectType == Character.TYPE ) {
			String str = rs.getString( name );
			if ( str != null && str.length() > 0 )
				return new Character( str.charAt( 0 ) );
			else
				return null;
		}

		if ( expectType == String.class )
			return rs.getString( name );

		if ( expectType == java.sql.Date.class )
			return rs.getDate( name );

		if ( expectType == java.sql.Time.class )
			return rs.getTime( name );

		if ( expectType == java.sql.Timestamp.class )
			return rs.getTimestamp( name );

		return rs.getObject( name );
	}

}
