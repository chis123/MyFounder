package com.founder.e5.db.cfg;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.founder.e5.db.meta.TableMetaData;

/**
 * 保存一个表的配置信息的数据结构
 * 
 * @author liyanhui
 * @version 1.0
 * @created 2006-8-20 11:41:49
 */
public class TableConfig {

	/**
	 * 表元数据对象，保存了表名、所有列、标识列的信息
	 */
	private TableMetaData metaData;

	/**
	 * 该表将被映射到的类；如为null，则映射到Map
	 */
	private Class mappingClass;

	/**
	 * 表列与类属性的映射关系：列名（String） - 属性名（String）
	 */
	private Map propMapping = new HashMap();

	/**
	 * 该表所有列配置信息的集合：列名（String） - 列配置信息对象（ColumnConfig）
	 */
	private Map columns = new HashMap();

	/**
	 */
	public TableConfig() {
	}

	/**
	 * @param metaData
	 */
	public TableConfig( TableMetaData metaData ) {
		this.metaData = metaData;
	}

	/**
	 * 检查给定列名（将被自动转为大写）是否存在
	 * 
	 * @param columnaName
	 * @return
	 */
	public boolean isColumnExist( String columnaName ) {
		return columns.keySet().contains( columnaName.toUpperCase() );
	}

	/**
	 * 获取指定列的配置信息
	 * 
	 * @param columnName
	 * @return
	 */
	public ColumnConfig getColumnConfig( String columnName ) {
		return ( ColumnConfig ) columns.get( columnName.toUpperCase() );
	}

	/**
	 * 添加表列名与bean属性名的映射信息
	 * 
	 * @param columnName 表列名
	 * @param fieldName bean属性名
	 */
	public void addPropMapping( String columnName, String fieldName ) {
		propMapping.put( columnName.toUpperCase(), fieldName );
	}

	/**
	 * 根据bean属性名获取所映射的表列名。<br>
	 * <br>
	 * 首先查找映射信息，若找到则返回；否则检查是否存在与属性名同名的列名，若找到则返回；最后若仍找不到，则返回null
	 * 
	 * @param fieldName bean属性名
	 * @return 表列名
	 */
	public String getMappingColumn( String fieldName ) {
		for ( Iterator i = propMapping.entrySet().iterator(); i.hasNext(); ) {
			Map.Entry me = ( Map.Entry ) i.next();
			if ( fieldName.equals( me.getValue() ) )
				return ( String ) me.getKey();
		}

		String columnName = fieldName.toUpperCase();
		if ( columns.keySet().contains( columnName ) )
			return columnName;

		return null;
	}

	/**
	 * 根据表列名获取所映射的bean属性名
	 * 
	 * @param columnName
	 * @return
	 */
	public String getMappingField( String columnName ) {
		return ( String ) propMapping.get( columnName.toUpperCase() );
	}

	public TableMetaData getMetaData() {
		return metaData;
	}

	public void setMetaData( TableMetaData metaData ) {
		this.metaData = metaData;
	}

	/**
	 * @return 该表所有列配置信息的集合：列名（String） - 列配置信息对象（ColumnConfig）
	 */
	public Map getColumns() {
		return columns;
	}

	public void setColumns( Map columns ) {
		if ( columns != null )
			this.columns = columns;
	}

	public Class getMappingClass() {
		return mappingClass;
	}

	public void setMappingClass( Class mappingClass ) {
		this.mappingClass = mappingClass;
	}

	public boolean equals( Object obj ) {
		if ( obj instanceof TableConfig ) {
			return ( ( TableConfig ) obj ).metaData.equals( metaData );
		}
		return false;
	}

	public int hashCode() {
		return metaData.hashCode();
	}

	public String toString() {
		return metaData.toString();
	}
}
