package com.founder.e5.db.meta;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * ��Ԫ���ݶ��󣬱����˱����������С���ʶ�е���Ϣ
 * 
 * @author liyanhui
 * @version 1.0
 * @created 2006-3-1 10:24:53
 */
public class TableMetaData {

	/**
	 * ����
	 */
	private String tablename;

	/**
	 * ��ʶ�ֶΣ������ж����
	 */
	private String[] idColumns = new String[ 0 ];

	/**
	 * ��Ԫ���ݻ��棺����(String) - ����Ϣ����(ColumnMetaData) ����Map��Ϊ�˸�������ȡ����ʱ���㣩
	 */
	private Map columns = new HashMap( 0 );

	/**
	 * @param tablename
	 */
	public TableMetaData( String tablename ) {
		this.tablename = tablename.toUpperCase();
	}

	/**
	 * ����������
	 * 
	 * @return Map�� ����(String) - ����Ϣ����(ColumnMetaData) ��
	 */
	public Map getColumns() {
		return columns;
	}

	/**
	 * @param columns ������
	 */
	public void setColumns( Map columns ) {
		if ( columns != null )
			this.columns = columns;
	}

	/**
	 * �������б�ʶ��
	 * 
	 * @return
	 */
	public String[] getIdColumns() {
		return idColumns;
	}

	/**
	 * @param idColumns ��ʶ��
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

	// -------------------------------------------- ��������

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
	 * ��������ȡ������Ϣ����
	 * 
	 * @param columnName
	 * @return
	 */
	public ColumnMetaData getColumn( String columnName ) {
		return ( ColumnMetaData ) columns.get( columnName );
	}

	/**
	 * ��������������û�еĻ����ؿ����飩
	 * 
	 * @return ��������
	 */
	public String[] getColumnNames() {
		return ( String[] ) columns.keySet().toArray( new String[ 0 ] );
	}

	/**
	 * �����ź�����������飨û�еĻ����ؿ����飩
	 * 
	 * @return ��������
	 */
	public String[] getSortedColumnNames() {
		String[] ss = ( String[] ) columns.keySet().toArray( new String[ 0 ] );
		Arrays.sort( ss );
		return ss;
	}

}
