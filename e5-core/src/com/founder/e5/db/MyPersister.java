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
 * 该类用来完成从数据库中<i>载入、存储、查询、更新</i>一个POJO实例的工作。<br>
 * <br>
 * 注意：该类目前只作为一种内部实现，不是API的一部分！不建议客户程序使用！
 * 
 * @author liyanhui
 * @version 1.0
 * @created 2006-8-20 12:23:12
 */
public class MyPersister {

	/*
	 * 设计说明： 当使用entity这个词时，泛指实体对象。在Java中表现为POJO，在RDBMS中表现为一条表记录；
	 * 当使用property这个词时，泛指实体对象的属性。在Java中表现为POJO的field，在RDBMS的 表记录中表现为column；
	 * field和column的说明如上。
	 * 
	 * 为使用方便，在Java中一个实体对象可用POJO表示，也可用Map表示；
	 * 当用POJO表示时，POJO的fieldName需要转换（根据用户自定义映射）为columnName；
	 * 当用Map表示时，认为Map的propertyName即为columnName。
	 */

	// -------------------------------------------------------------------------
	// select支持
	/**
	 * 生成查询语句，以主键列为查询条件
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
	 * 生成查询语句，根据给定列生成查询条件
	 * 
	 * @param tc 表配置信息
	 * @param byColumns 条件列
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
	 * 根据表名和id从数据库中查出一条记录，并装配为一个对象返回
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
	 * 根据给定查询条件查询指定表，返回结果列表
	 * 
	 * @param DBSession实例
	 * @param tablename 表名
	 * @param queryParams 查询参数 {列名 - 列值} 注意：不能含null值
	 * @return bean的集合（若此表存在mappingClass，则为该类实例；否则为Map实例）
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
	 * 从结果集中读取一行数据，保存到Map中返回
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
	 * 从结果集中读取bean属性所映射的列值绑定到bean上
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

				// 这里只绑定实例属性
				if ( Modifier.isStatic( modifiers ) )
					continue;

				// 跳过final属性
				if ( Modifier.isFinal( modifiers ) )
					continue;

				String name = field.getName();
				Class type = field.getType();

				// 根据bean属性名获取所映射的表列名
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
	// insert支持

	/**
	 * @param tc
	 * @param valueMap 包含待插入数据的容器
	 * @param paramValues 输出型参数，其中保存了待绑定到?的参数值
	 * @param paramTypes 输出型参数，其中保存了待绑定到?的参数的类型
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
		Map valueMap = null; // 保存 {列名-列值} 对的集合
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
	 * 提取bean属性名和属性值组成值映射集合，其中属性名被转化为映射的表列名
	 * 
	 * @param bean
	 * @param tc 据此查询类属性到表列名的映射信息
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

			if ( columnName != null ) { // 若该属性与表字段存在对应关系
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
	// update支持

	/**
	 * 更新指定表中与给定POJO对应的表记录。<br>
	 * <br>
	 * 注意：该处理过程忽略为null值的属性；<br>
	 * 如果定义了主键字段，则根据主键生成限定条件；否则，根据所有非null列值生成限定条件
	 * 
	 * @param ss DBSession实例
	 * @param tablename 表名
	 * @param entity 实体对象（POJO或Map）
	 * @param dirtyProperties 脏属性（更新时只更新与这些属性对应的列）
	 * @throws SQLException
	 */
	public static void update( DBSession ss, String tablename, Object entity,
			String[] dirtyProperties ) throws SQLException {

		TableConfig tc = DBContext.getTableConfig( tablename, ss );
		String[] idColumns = tc.getMetaData().getIdColumns();

		// 是否定义了主键字段
		boolean idDefined = false;
		List idColumnList = null;
		if ( idColumns.length > 0 ) {
			idDefined = true;
			idColumnList = Arrays.asList( idColumns );
		}

		HashMap updateColumns = new HashMap();
		HashMap conditions = new HashMap();

		List dirtyPropertyList = Arrays.asList( dirtyProperties );

		if ( entity instanceof Map ) { // Map型实体
			Map map = ( Map ) entity;

			for ( Iterator i = map.entrySet().iterator(); i.hasNext(); ) {
				Map.Entry me = ( Map.Entry ) i.next();
				String columnName = ( String ) me.getKey();
				Object columnValue = me.getValue();

				if ( columnValue != null ) {

					// 生成待更新列
					if ( dirtyPropertyList.contains( columnName ) )
						updateColumns.put( columnName, columnValue );

					// 生成限定条件

					// 如果定义了主键字段，则根据主键生成限定条件；否则，根据所有非null列值生成限定条件
					if ( idDefined ) {
						if ( idColumnList.contains( columnName ) )
							conditions.put( columnName, columnValue );
					} else {
						conditions.put( columnName, columnValue );
					}

				}
			}

		}

		else { // POJO型实体
			Field[] fields = entity.getClass().getDeclaredFields();

			for ( int i = 0; i < fields.length; i++ ) {
				Field field = fields[i];
				if ( Modifier.isStatic( field.getModifiers() ) )
					continue;

				String fieldName = field.getName();
				String columnName = tc.getMappingColumn( fieldName );

				if ( columnName != null ) { // 若该属性与表字段存在对应关系

					try {
						field.setAccessible( true );
						Object value = field.get( entity );

						if ( value != null ) {

							// 生成待更新列
							if ( dirtyPropertyList.contains( fieldName ) )
								updateColumns.put( columnName, value );

							// 生成限定条件

							// 如果定义了主键字段，则根据主键生成限定条件；否则，根据所有非null列值生成限定条件
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
	 * 根据给定限定条件更新指定表字段。<br>
	 * <br>
	 * 注意：字段值不能为null
	 * 
	 * @param ss DBSession实例
	 * @param tablename 表名
	 * @param updateColumns 待更新的列名与列值映射信息的集合（注意：不能含null）
	 * @param conditions 限定条件：列名与列值映射信息的集合（注意：不能含null）
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
