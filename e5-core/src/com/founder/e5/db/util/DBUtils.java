package com.founder.e5.db.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * �����ṩ��JDBC�����йصĹ��߷���
 * 
 * @author liyanhui
 * @version 1.0
 * @created 2006-8-30 10:16:38
 */
public class DBUtils {

	/**
	 * �ӽ������ȡ��bean����ͬ������ֵ�󶨵�bean���ԡ�
	 * 
	 * @param rs
	 * @param beanClass bean�࣬�����ض��������
	 * @return
	 * @throws SQLException
	 */
	public static Object bindBean( ResultSet rs, Class beanClass )
			throws SQLException {
		return bindBean( rs, beanClass, ( Map ) null );
	}

	/**
	 * �ӽ�����ж�ȡ��ǰ�У�����bean���Ե���������ӳ���ϵ��ȡ��ֵ���󶨵�bean���ԡ�<br>
	 * <br>
	 * ע�⣺����ĳ������ʱ�����Ҳ��������Ե�ӳ���ϵ����Ĭ����������������ͬ��<br>
	 * ���⣬�÷�������<I>static</I>��<I>final</I> ���ԡ�
	 * 
	 * @param rs
	 * @param beanClass
	 * @param mapping ��ʽ��"key1=value1;key2=value2;key3=value3"
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
	 * �ӽ�����ж�ȡ��ǰ�У�����bean���Ե���������ӳ���ϵ��ȡ��ֵ���󶨵�bean���ԡ�<br>
	 * <br>
	 * ע�⣺����ĳ������ʱ�����Ҳ��������Ե�ӳ���ϵ����Ĭ����������������ͬ��<br>
	 * ���⣬�÷�������<I>static</I>��<I>final</I> ���ԡ�
	 * 
	 * @param rs
	 * @param beanClass
	 * @param mapping bean���Ե���������ӳ���ϵ����Ϊnull��
	 * @return װ��õ�bean
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

				// ����ֻ��ʵ������
				if ( Modifier.isStatic( modifier ) )
					continue;

				// ����final����
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
	 * �������������ʹӽ�����л�ȡֵ
	 * 
	 * @param rs �����
	 * @param name ����
	 * @param expectType ������ֵ����
	 * @return expectType���Ͷ���
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
