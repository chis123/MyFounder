package com.founder.e5.db.cfg;

import com.founder.e5.db.meta.ColumnMetaData;

/**
 * ����һ���е�������Ϣ�����ݽṹ
 * 
 * @author liyanhui
 * @version 1.0
 * @created 2006-8-20 11:10:56
 */
public class ColumnConfig {

	private ColumnMetaData metaData;

	/**
	 * �Ƿ����� ��������Ӱ���ѯʱ���ɵ�sql��䣩
	 */
	private boolean load = true;

	/**
	 * �Ƿ���� ��������Ӱ�����ʱ���ɵ�sql��䣩
	 */
	private boolean insert = true;

	/**
	 * @param metaData ��Ԫ����
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
