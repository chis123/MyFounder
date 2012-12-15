package com.founder.e5.db.cfg;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

/**
 * 保存一个数据库的配置信息的数据结构<br>
 * <br>
 * 注意：引入该类是为了支持多数据库。不过。。。似乎不应该由该类管理DataSource
 * 
 * @author liyanhui
 * @version 1.0
 * @created 2006-8-20 11:10:42
 */
public class DBConfig {

	private String name;

	private String dbType;

	private DataSource dataSource;

	/**
	 * 所有表配置信息的集合：表名（String） - 表配置信息对象（TableConfig）
	 */
	private Map tables = new HashMap();

	/**
	 * @param name 数据库名
	 */
	public DBConfig( String name ) {
		this.name = name;
	}

	/**
	 * @param tablename
	 * @return
	 */
	public TableConfig getTableConfig( String tablename ) {
		return ( TableConfig ) tables.get( tablename.toUpperCase() );
	}

	/**
	 * @param tablename
	 * @param tc
	 */
	public void addTableConfig( String tablename, TableConfig tc ) {
		tables.put( tablename.toUpperCase(), tc );
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource( DataSource dataSource ) {
		this.dataSource = dataSource;
	}

	public String getDbType() {
		return dbType;
	}

	public void setDbType( String dbType ) {
		this.dbType = dbType;
	}

	public String getName() {
		return name;
	}

	public void setName( String name ) {
		this.name = name;
	}

	public Map getTables() {
		return tables;
	}

	public void setTables( Map tables ) {
		if ( tables != null )
			this.tables = tables;
	}

	public boolean equals( Object obj ) {
		if ( obj instanceof DBConfig ) {
			return name.equals( ( ( DBConfig ) obj ).name );
		}
		return false;
	}

	public int hashCode() {
		return name.hashCode();
	}

	public String toString() {
		return name;
	}

}
