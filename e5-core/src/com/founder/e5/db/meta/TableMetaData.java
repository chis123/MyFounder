package com.founder.e5.db.meta;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 表元数据对象，保存了表名、所有列、标识列的信息
 * 
 * @author liyanhui
 * @version 1.0
 * @created 2006-3-1 10:24:53
 */
public class TableMetaData {

	/**
	 * 表名
	 */
	private String tablename;

	/**
	 * 标识字段（可能有多个）
	 */
	private String[] idColumns = new String[ 0 ];

	/**
	 * 列元数据缓存：列名(String) - 列信息对象(ColumnMetaData) （用Map是为了根据名字取对象时方便）
	 */
	private Map columns = new HashMap( 0 );

	/**
	 * @param tablename
	 */
	public TableMetaData( String tablename ) {
		this.tablename = tablename.toUpperCase();
	}

	/**
	 * 返回所有列
	 * 
	 * @return Map（ 列名(String) - 列信息对象(ColumnMetaData) ）
	 */
	public Map getColumns() {
		return columns;
	}

	/**
	 * @param columns 所有列
	 */
	public void setColumns( Map columns ) {
		if ( columns != null )
			this.columns = columns;
	}

	/**
	 * 返回所有标识列
	 * 
	 * @return
	 */
	public String[] getIdColumns() {
		return idColumns;
	}

	/**
	 * @param idColumns 标识列
	 */
	public void setIdColumns( String[] idColumns ) {
		if ( idColumns != null ) {
			this.idColumns = idColumns;
		}
	}

	public String getTablename() {
		return tablename;
	}

	public void setTablename( String tablename ) {
		this.tablename = tablename;
	}

	public boolean equals( Object obj ) {
		if ( obj instanceof TableMetaData ) {
			return ( ( TableMetaData ) obj ).tablename.equals( tablename );
		}
		return false;
	}

	public int hashCode() {
		return tablename.hashCode();
	}

	public String toString() {
		return tablename;
	}

	// -------------------------------------------- 便利方法

	public String getIdColumn() {
		if ( idColumns.length > 0 )
			return idColumns[0];
		return null;
	}

	public void setIdColumn( String idColumn ) {
		if ( idColumn != null )
			this.idColumns = new String[] { idColumn };
	}

	/**
	 * 根据列名取得列信息对象
	 * 
	 * @param columnName
	 * @return
	 */
	public ColumnMetaData getColumn( String columnName ) {
		return ( ColumnMetaData ) columns.get( columnName );
	}

	/**
	 * 返回所有列名（没有的话返回空数组）
	 * 
	 * @return 列名数组
	 */
	public String[] getColumnNames() {
		return ( String[] ) columns.keySet().toArray( new String[ 0 ] );
	}

	/**
	 * 返回排好序的列名数组（没有的话返回空数组）
	 * 
	 * @return 列名数组
	 */
	public String[] getSortedColumnNames() {
		String[] ss = ( String[] ) columns.keySet().toArray( new String[ 0 ] );
		Arrays.sort( ss );
		return ss;
	}

}
