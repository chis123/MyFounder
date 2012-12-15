package com.founder.e5.db;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import com.founder.e5.db.cfg.ColumnConfig;
import com.founder.e5.db.cfg.DBConfig;
import com.founder.e5.db.cfg.TableConfig;
import com.founder.e5.db.meta.ColumnMetaData;
import com.founder.e5.db.meta.TableMetaData;
import com.founder.e5.db.util.MetaDataUtils;

/**
 * 该类保存应用中涉及到的每个数据库的配置信息（包括名字、类型、DataSource），以及每个数据库
 * 中相应的表的元数据（主键字段，所有字段的字段名、类型、长度、可空等）<br>
 * <br>
 * 为了使用方便，该类所有方法按照静态调用方式访问<br>
 * <br>
 * 为了在只使用一个数据库（这是大部分情况）时用着方便，该类提供了“缺省数据库”的设置<br>
 * <br>
 * 该类提供两种初始化配置信息的方式：配置文件 和 编程式的registerXXX方法
 * 
 * @author liyanhui
 * @version 1.0
 * @created 2006-8-20 10:33:06
 */
public class DBContext {

	private static final String DEFAULT_DB_NAME = "DEFAULT_DB";

	/**
	 * 数据库配置信息缓存：数据库名（String） - 数据库配置信息对象（DBConfig）
	 */
	private static final Hashtable dbConfigCache = new Hashtable();

	// 传给私有方法的tablename一律认为是大写
	// 这些私有的get方法都是若发现相应配置对象不存在则自动创建

	private static DBConfig getDBConfig( String dbname, String tablename ) {
		DBConfig dc = ( DBConfig ) dbConfigCache.get( dbname );
		if ( dc == null ) {
			dc = new DBConfig( dbname );
			dbConfigCache.put( dbname, dc );
		}
		return dc;
	}

	private static TableConfig getTableConfig( String dbname, String tablename ) {
		DBConfig dc = getDBConfig( dbname, tablename );
		TableConfig tc = dc.getTableConfig( tablename );
		if ( tc == null ) {
			tc = new TableConfig();
			dc.addTableConfig( tablename, tc );
		}
		return tc;
	}

	private static TableMetaData getTableMetaData( String dbname,
			String tablename ) {
		TableConfig tc = getTableConfig( dbname, tablename );
		TableMetaData tmd = tc.getMetaData();
		if ( tmd == null ) {
			tmd = new TableMetaData( tablename );
			tc.setMetaData( tmd );
		}
		return tmd;
	}

	// ------------------------------------------------------------------------

	/**
	 * @param dbname
	 * @param tablename
	 * @param idColumn
	 */
	public static void registerIdColumn( String dbname, String tablename,
			String idColumn ) {
		tablename = tablename.toUpperCase();
		getTableMetaData( dbname, tablename ).setIdColumn( idColumn );
	}

	/**
	 * 注册标识字段
	 * 
	 * @param tablename
	 * @param idColumn
	 */
	public static void registerIdColumn( String tablename, String idColumn ) {
		registerIdColumn( DEFAULT_DB_NAME, tablename, idColumn );
	}

	/**
	 * 注册映射类
	 * 
	 * @param dbname
	 * @param tablename
	 * @param mappingClass
	 */
	public static void registerMappingClass( String dbname, String tablename,
			Class mappingClass ) {
		tablename = tablename.toUpperCase();
		getTableConfig( dbname, tablename ).setMappingClass( mappingClass );
	}

	/**
	 * 注册映射类
	 * 
	 * @param tablename
	 * @param mappingClass
	 */
	public static void registerMappingClass( String tablename,
			Class mappingClass ) {
		registerMappingClass( DEFAULT_DB_NAME, tablename, mappingClass );
	}

	/**
	 * 注册表与类的映射关系；包括：表名与类的映射和表列名与类属性名的映射
	 * 
	 * @param dbname 数据库名
	 * @param tablename 表名
	 * @param mappingClass 类
	 * @param propMapping 表列名与类属性名的映射 {key-列名 value-属性名}
	 */
	public static void registerMapping( String dbname, String tablename,
			Class mappingClass, Map propMapping ) {
		tablename = tablename.toUpperCase();
		TableConfig tc = getTableConfig( dbname, tablename );

		tc.setMappingClass( mappingClass );

		for ( Iterator i = propMapping.entrySet().iterator(); i.hasNext(); ) {
			Map.Entry me = ( Map.Entry ) i.next();

			String columnName = ( String ) me.getKey();
			String fieldName = ( String ) me.getValue();

			tc.addPropMapping( columnName, fieldName );
		}
	}

	/**
	 * 注册表与类的映射关系；包括：表名与类的映射和表列名与类属性名的映射
	 * 
	 * @param tablename 表名
	 * @param mappingClass 类
	 * @param propMapping 表列名与类属性名的映射 {key-列名 value-属性名}
	 */
	public static void registerMapping( String tablename, Class mappingClass,
			Map propMapping ) {
		registerMapping( DEFAULT_DB_NAME, tablename, mappingClass, propMapping );
	}

	// ------------------------------------------------------------------------

	/**
	 * 获取表的配置信息（若不存在则自动创建）
	 * 
	 * @param dbname 表所属数据库
	 * @param tablename 表名，自动被转为大写
	 * @param DBSession实例（若表元数据不存在则自动创建）
	 * @return
	 */
	public static TableConfig getTableConfig( String dbname, String tablename,
			DBSession sess ) {
		tablename = tablename.toUpperCase();

		TableConfig tc = getTableConfig( dbname, tablename );
		TableMetaData tmd = tc.getMetaData();

		if ( tmd == null ) {
			tmd = new TableMetaData( tablename );
			tc.setMetaData( tmd );
		}

		try {

			// 此时，若表元数据不存在，则执行数据库查询，自动创建元数据

			if ( tmd.getColumns().size() == 0 )
				MetaDataUtils.fillMetaData( tmd, sess, tablename );

			// 把ColumnMetaData包装为ColumnConfig

			Collection columns = tmd.getColumns().values();
			HashMap tmp = new HashMap( columns.size() );

			for ( Iterator i = columns.iterator(); i.hasNext(); ) {
				ColumnMetaData cmd = ( ColumnMetaData ) i.next();
				ColumnConfig cc = new ColumnConfig( cmd );

				tmp.put( cmd.getName(), cc );
			}

			tc.setColumns( tmp );

		} catch ( SQLException e ) {
			throw new LaterInitException( e );
		}

		return tc;
	}

	/**
	 * 获取表的配置信息（使用默认数据库）
	 * 
	 * @param tablename 表名，自动被转为大写
	 * @param DBSession实例，用于当需要的表元数据尚不存在时自动创建
	 * @return
	 */
	public static TableConfig getTableConfig( String tablename, DBSession sess ) {
		return getTableConfig( DEFAULT_DB_NAME, tablename, sess );
	}

}
