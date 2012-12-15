package com.founder.e5.db.cfg;

import com.founder.e5.db.meta.ColumnMetaData;

/**
 * 保存一个列的配置信息的数据结构
 * 
 * @author liyanhui
 * @version 1.0
 * @created 2006-8-20 11:10:56
 */
public class ColumnConfig {

	private ColumnMetaData metaData;

	/**
	 * 是否载入 （该配置影响查询时生成的sql语句）
	 */
	private boolean load = true;

	/**
	 * 是否插入 （该配置影响插入时生成的sql语句）
	 */
	private boolean insert = true;

	/**
	 * @param metaData 列元数据
	 */
	public ColumnConfig( ColumnMetaData metaData ) {
		if ( metaData == null )
			throw new NullPointerException();
		this.metaData = metaData;
	}

	public boolean isInsert() {
		return insert;
	}

	public void setInsert( boolean insert ) {
		this.insert = insert;
	}

	public boolean isLoad() {
		return load;
	}

	public void setLoad( boolean load ) {
		this.load = load;
	}

	public ColumnMetaData getMetaData() {
		return metaData;
	}

	public void setMetaData( ColumnMetaData metaData ) {
		this.metaData = metaData;
	}

	public boolean equals( Object obj ) {
		if ( obj instanceof ColumnConfig ) {
			return ( ( ColumnConfig ) obj ).metaData.equals( metaData );
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
