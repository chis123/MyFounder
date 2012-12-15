package com.founder.e5.db;

import gnu.trove.TIntArrayList;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.founder.e5.db.cfg.ColumnConfig;
import com.founder.e5.db.cfg.TableConfig;
import com.founder.e5.db.meta.ColumnMetaData;
import com.founder.e5.db.util.CloseHelper;
import com.founder.e5.db.util.DBUtils;
import com.founder.e5.db.util.StringUtils;

/**
 * ����������ɴ����ݿ���<i>���롢�洢����ѯ������</i>һ��POJOʵ���Ĺ�����<br>
 * <br>
 * ע�⣺����Ŀǰֻ��Ϊһ���ڲ�ʵ�֣�����API��һ���֣�������ͻ�����ʹ�ã�
 * 
 * @author liyanhui
 * @version 1.0
 * @created 2006-8-20 12:23:12
 */
public class MyPersister {

	/*
	 * ���˵���� ��ʹ��entity�����ʱ����ָʵ�������Java�б���ΪPOJO����RDBMS�б���Ϊһ�����¼��
	 * ��ʹ��property�����ʱ����ָʵ���������ԡ���Java�б���ΪPOJO��field����RDBMS�� ���¼�б���Ϊcolumn��
	 * field��column��˵�����ϡ�
	 * 
	 * Ϊʹ�÷��㣬��Java��һ��ʵ��������POJO��ʾ��Ҳ����Map��ʾ��
	 * ����POJO��ʾʱ��POJO��fieldName��Ҫת���������û��Զ���ӳ�䣩ΪcolumnName��
	 * ����Map��ʾʱ����ΪMap��propertyName��ΪcolumnName��
	 */

	// -------------------------------------------------------------------------
	// select֧��
	/**
	 * ���ɲ�ѯ��䣬��������Ϊ��ѯ����
	 */
	private static String generateQuerySql( TableConfig tc ) {
		String idColumn = tc.getMetaData().getIdColumn();
		if ( idColumn == null )
			throw new IllegalStateException( "No idColumn yet for table '"
					+ tc.getMetaData().getTablename() + "'" );

		String sql = generateQuerySql( tc, new String[] { idColumn } );
		return sql;
	}

	/**
	 * ���ɲ�ѯ��䣬���ݸ��������ɲ�ѯ����
	 * 
	 * @param tc ��������Ϣ
	 * @param byColumns ������
	 * @return
	 */
	private static String generateQuerySql( TableConfig tc, String[] byColumns ) {
		StringBuffer sb = new StringBuffer();
		sb.append( "select " );

		Collection columns = tc.getColumns().values();
		for ( Iterator i = columns.iterator(); i.hasNext(); ) {
			ColumnConfig cc = ( ColumnConfig ) i.next();

			if ( cc.isLoad() )
				sb.append( cc.getMetaData().getName() ).append( ',' );
		}

		StringUtils.trimTail( sb, ',' );

		StringBuffer condition = new StringBuffer();
		for ( int i = 0; i < byColumns.length; i++ ) {
			condition.append( byColumns[i] ).append( "=?" );
			if ( i < byColumns.length - 1 )
				condition.append( " and " );
		}

		sb.append( " from " ).append( tc.getMetaData().getTablename() ).append(
				" where " ).append( condition );
		return sb.toString();
	}

	/**
	 * @param rs
	 * @param meta
	 * @return
	 * @throws SQLException
	 */
	private static Object getValue( ResultSet rs, ColumnMetaData meta )
			throws SQLException {

		String name = meta.getName();
		int type = meta.getType();

		switch ( type ) {
			case Types.INTEGER :
				return new Integer( rs.getInt( name ) );

			case Types.BIGINT :
				return new Long( rs.getLong( name ) );

			case Types.FLOAT :
			case Types.REAL :
				return new Float( rs.getFloat( name ) );

			case Types.DOUBLE :
				return new Double( rs.getDouble( name ) );

			case Types.CHAR :
			case Types.VARCHAR :
				return rs.getString( name );

			case Types.NUMERIC :
				if ( meta.getDataPrecision() > 0 )
					return new Double( rs.getDouble( name ) );
				else
					return new Integer( rs.getInt( name ) );

			case Types.TIMESTAMP :
				return rs.getTimestamp( name );
			case Types.DATE :
				return rs.getDate( name );
			case Types.TIME :
				return rs.getTime( name );
		}

		return rs.getObject( name );
	}

	/**
	 * ���ݱ�����id�����ݿ��в��һ����¼����װ��Ϊһ�����󷵻�
	 * 
	 * @param ss
	 * @param tablename
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	public static Object load( DBSession ss, String tablename, Object id )
			throws SQLException {

		TableConfig tc = DBContext.getTableConfig( tablename, ss );
		String sql = generateQuerySql( tc );

		Object bean = null;
		IResultSet rs = ss.executeQuery( sql, new Object[] { id } );

		try {
			if ( rs.next() ) {

				Class clazz = tc.getMappingClass();

				if ( clazz == null ) {
					bean = readRow( rs, tc );
				}

				else {
					bean = bindBean( rs, clazz, tc );
				}
			}
		} finally {
			CloseHelper.closeQuietly( rs );
		}

		return bean;
	}

	/**
	 * ���ݸ�����ѯ������ѯָ�������ؽ���б�
	 * 
	 * @param DBSessionʵ��
	 * @param tablename ����
	 * @param queryParams ��ѯ���� {���� - ��ֵ} ע�⣺���ܺ�nullֵ
	 * @return bean�ļ��ϣ����˱����mappingClass����Ϊ����ʵ��������ΪMapʵ����
	 * @throws SQLException
	 */
	public static List query( DBSession ss, String tablename, Map queryParams )
			throws SQLException {

		String[] columnNames = ( String[] ) queryParams.keySet().toArray(
				new String[ 0 ] );
		Object[] columnValues = queryParams.values().toArray();

		TableConfig tc = DBContext.getTableConfig( tablename, ss );
		String sql = generateQuerySql( tc, columnNames );

		ArrayList result = new ArrayList();
		IResultSet rs = ss.executeQuery( sql, columnValues );

		try {
			while ( rs.next() ) {

				Object bean = null;
				Class clazz = tc.getMappingClass();

				if ( clazz == null ) {
					bean = readRow( rs, tc );
				}

				else {
					bean = bindBean( rs, clazz, tc );
				}

				result.add( bean );
			}
		} finally {
			CloseHelper.closeQuietly( rs );
		}

		return result;
	}

	/**
	 * �ӽ�����ж�ȡһ�����ݣ����浽Map�з���
	 * 
	 * @param rs
	 * @param tc
	 * @return
	 * @throws SQLException
	 */
	private static HashMap readRow( IResultSet rs, TableConfig tc )
			throws SQLException {
		HashMap map = new HashMap();

		for ( Iterator i = tc.getColumns().values().iterator(); i.hasNext(); ) {
			ColumnConfig cc = ( ColumnConfig ) i.next();

			if ( cc.isLoad() ) {
				ColumnMetaData cmd = cc.getMetaData();
				String name = cmd.getName();
				Object value = getValue( rs, cmd );
				map.put( name, value );
			}
		}

		return map;
	}

	/**
	 * �ӽ�����ж�ȡbean������ӳ�����ֵ�󶨵�bean��
	 * 
	 * @param rs
	 * @param beanClass
	 * @param tc
	 * @return
	 * @throws SQLException
	 */
	private static Object bindBean( IResultSet rs, Class beanClass,
			TableConfig tc ) throws SQLException {
		Object bean = null;

		try {
			bean = beanClass.newInstance();
			Field[] fields = beanClass.getDeclaredFields();

			for ( int i = 0; i < fields.length; i++ ) {
				Field field = fields[i];
				int modifiers = field.getModifiers();

				// ����ֻ��ʵ������
				if ( Modifier.isStatic( modifiers ) )
					continue;

				// ����final����
				if ( Modifier.isFinal( modifiers ) )
					continue;

				String name = field.getName();
				Class type = field.getType();

				// ����bean��������ȡ��ӳ��ı�����
				String columnName = tc.getMappingColumn( name );
				Object value = DBUtils.getValue( rs, columnName, type );

				field.setAccessible( true );
				field.set( bean, value );
			}

		} catch ( InstantiationException e ) {
			throw new RuntimeException( e );
		} catch ( IllegalAccessException e ) {
			throw new RuntimeException( e );
		}

		return bean;
	}

	// -------------------------------------------------------------------------
	// insert֧��

	/**
	 * @param tc
	 * @param valueMap �������������ݵ�����
	 * @param paramValues ����Ͳ��������б����˴��󶨵�?�Ĳ���ֵ
	 * @param paramTypes ����Ͳ��������б����˴��󶨵�?�Ĳ���������
	 * @return
	 */
	private static String generateInertSql( TableConfig tc, Map valueMap,
			ArrayList paramValues, TIntArrayList paramTypes ) {
		StringBuffer sb = new StringBuffer();
		sb.append( "insert into " ).append( tc.getMetaData().getTablename() ).append(
				" ( " );

		StringBuffer names = new StringBuffer();
		StringBuffer values = new StringBuffer();

		for ( Iterator i = valueMap.keySet().iterator(); i.hasNext(); ) {
			String propertyName = ( String ) i.next();

			if ( tc.isColumnExist( propertyName ) ) {

				Object value = valueMap.get( propertyName );
				ColumnConfig cc = tc.getColumnConfig( propertyName );

				if ( cc.isInsert() && value != null ) {
					String columnName = cc.getMetaData().getName();
					names.append( columnName ).append( "," );
					values.append( "?" ).append( "," );

					paramValues.add( value );
					paramTypes.add( cc.getMetaData().getType() );
				}
			}
		}

		StringUtils.trimTail( names, ',' );
		StringUtils.trimTail( values, ',' );

		sb.append( names ).append( " ) values ( " ).append( values ).append(
				" )" );
		return sb.toString();
	}

	/**
	 * @param ss
	 * @param tablename
	 * @param object
	 * @throws SQLException
	 */
	public static void store( DBSession ss, String tablename, Object object )
			throws SQLException {
		Map valueMap = null; // ���� {����-��ֵ} �Եļ���
		TableConfig tc = DBContext.getTableConfig( tablename, ss );

		if ( object instanceof Map ) {
			valueMap = ( Map ) object;
		}

		else {
			valueMap = toValueMap( object, tc );
		}

		ArrayList paramValues = new ArrayList();
		TIntArrayList paramTypes = new TIntArrayList();

		String sql = generateInertSql( tc, valueMap, paramValues, paramTypes );

		ss.executeUpdate(
				sql,
				paramValues.toArray(),
				paramTypes.toNativeArray() );

	}

	/**
	 * ��ȡbean������������ֵ���ֵӳ�伯�ϣ�������������ת��Ϊӳ��ı�����
	 * 
	 * @param bean
	 * @param tc �ݴ˲�ѯ�����Ե���������ӳ����Ϣ
	 * @return
	 */
	private static Map toValueMap( Object bean, TableConfig tc ) {
		HashMap map = new HashMap();
		Field[] fields = bean.getClass().getDeclaredFields();

		for ( int i = 0; i < fields.length; i++ ) {
			Field field = fields[i];
			if ( Modifier.isStatic( field.getModifiers() ) )
				continue;

			String name = field.getName();
			String columnName = tc.getMappingColumn( name );

			if ( columnName != null ) { // ������������ֶδ��ڶ�Ӧ��ϵ
				try {
					field.setAccessible( true );
					Object value = field.get( bean );
					map.put( columnName, value );

				} catch ( IllegalAccessException e ) {
					throw new RuntimeException( e );
				}
			}
		}

		return map;
	}

	// -------------------------------------------------------------------------
	// update֧��

	/**
	 * ����ָ�����������POJO��Ӧ�ı��¼��<br>
	 * <br>
	 * ע�⣺�ô�����̺���Ϊnullֵ�����ԣ�<br>
	 * ��������������ֶΣ���������������޶����������򣬸������з�null��ֵ�����޶�����
	 * 
	 * @param ss DBSessionʵ��
	 * @param tablename ����
	 * @param entity ʵ�����POJO��Map��
	 * @param dirtyProperties �����ԣ�����ʱֻ��������Щ���Զ�Ӧ���У�
	 * @throws SQLException
	 */
	public static void update( DBSession ss, String tablename, Object entity,
			String[] dirtyProperties ) throws SQLException {

		TableConfig tc = DBContext.getTableConfig( tablename, ss );
		String[] idColumns = tc.getMetaData().getIdColumns();

		// �Ƿ����������ֶ�
		boolean idDefined = false;
		List idColumnList = null;
		if ( idColumns.length > 0 ) {
			idDefined = true;
			idColumnList = Arrays.asList( idColumns );
		}

		HashMap updateColumns = new HashMap();
		HashMap conditions = new HashMap();

		List dirtyPropertyList = Arrays.asList( dirtyProperties );

		if ( entity instanceof Map ) { // Map��ʵ��
			Map map = ( Map ) entity;

			for ( Iterator i = map.entrySet().iterator(); i.hasNext(); ) {
				Map.Entry me = ( Map.Entry ) i.next();
				String columnName = ( String ) me.getKey();
				Object columnValue = me.getValue();

				if ( columnValue != null ) {

					// ���ɴ�������
					if ( dirtyPropertyList.contains( columnName ) )
						updateColumns.put( columnName, columnValue );

					// �����޶�����

					// ��������������ֶΣ���������������޶����������򣬸������з�null��ֵ�����޶�����
					if ( idDefined ) {
						if ( idColumnList.contains( columnName ) )
							conditions.put( columnName, columnValue );
					} else {
						conditions.put( columnName, columnValue );
					}

				}
			}

		}

		else { // POJO��ʵ��
			Field[] fields = entity.getClass().getDeclaredFields();

			for ( int i = 0; i < fields.length; i++ ) {
				Field field = fields[i];
				if ( Modifier.isStatic( field.getModifiers() ) )
					continue;

				String fieldName = field.getName();
				String columnName = tc.getMappingColumn( fieldName );

				if ( columnName != null ) { // ������������ֶδ��ڶ�Ӧ��ϵ

					try {
						field.setAccessible( true );
						Object value = field.get( entity );

						if ( value != null ) {

							// ���ɴ�������
							if ( dirtyPropertyList.contains( fieldName ) )
								updateColumns.put( columnName, value );

							// �����޶�����

							// ��������������ֶΣ���������������޶����������򣬸������з�null��ֵ�����޶�����
							if ( idDefined ) {
								if ( idColumnList.contains( columnName ) )
									conditions.put( columnName, value );
							} else {
								conditions.put( columnName, value );
							}

						}

					} catch ( IllegalAccessException e ) {
						throw new RuntimeException( e );
					}
				}
			}
		}

		update( ss, tablename, updateColumns, conditions );
	}

	/**
	 * ���ݸ����޶���������ָ�����ֶΡ�<br>
	 * <br>
	 * ע�⣺�ֶ�ֵ����Ϊnull
	 * 
	 * @param ss DBSessionʵ��
	 * @param tablename ����
	 * @param updateColumns �����µ���������ֵӳ����Ϣ�ļ��ϣ�ע�⣺���ܺ�null��
	 * @param conditions �޶���������������ֵӳ����Ϣ�ļ��ϣ�ע�⣺���ܺ�null��
	 * @throws SQLException
	 */
	public static int update( DBSession ss, String tablename,
			Map updateColumns, Map conditions ) throws SQLException {
		StringBuffer sb = new StringBuffer();
		sb.append( "update " ).append( tablename ).append( " set " );

		for ( Iterator i = updateColumns.entrySet().iterator(); i.hasNext(); ) {
			Map.Entry me = ( Map.Entry ) i.next();
			String columnName = ( String ) me.getKey();

			sb.append( columnName ).append( "=?" );

			if ( i.hasNext() )
				sb.append( ", " );
		}

		sb.append( " where " );

		for ( Iterator i = conditions.entrySet().iterator(); i.hasNext(); ) {
			Map.Entry me = ( Map.Entry ) i.next();
			String columnName = ( String ) me.getKey();

			sb.append( columnName ).append( "=?" );

			if ( i.hasNext() )
				sb.append( " and " );
		}

		String sql = sb.toString();
		ArrayList params = new ArrayList();
		params.addAll( updateColumns.values() );
		params.addAll( conditions.values() );

		return ss.executeUpdate( sql, params.toArray() );
	}

}
