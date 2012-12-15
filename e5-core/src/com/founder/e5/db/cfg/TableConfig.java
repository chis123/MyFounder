package com.founder.e5.db.cfg;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.founder.e5.db.meta.TableMetaData;

/**
 * ����һ�����������Ϣ�����ݽṹ
 * 
 * @author liyanhui
 * @version 1.0
 * @created 2006-8-20 11:41:49
 */
public class TableConfig {

	/**
	 * ��Ԫ���ݶ��󣬱����˱����������С���ʶ�е���Ϣ
	 */
	private TableMetaData metaData;

	/**
	 * �ñ���ӳ�䵽���ࣻ��Ϊnull����ӳ�䵽Map
	 */
	private Class mappingClass;

	/**
	 * �����������Ե�ӳ���ϵ��������String�� - ��������String��
	 */
	private Map propMapping = new HashMap();

	/**
	 * �ñ�������������Ϣ�ļ��ϣ�������String�� - ��������Ϣ����ColumnConfig��
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
	 * �����������������Զ�תΪ��д���Ƿ����
	 * 
	 * @param columnaName
	 * @return
	 */
	public boolean isColumnExist( String columnaName ) {
		return columns.keySet().contains( columnaName.toUpperCase() );
	}

	/**
	 * ��ȡָ���е�������Ϣ
	 * 
	 * @param columnName
	 * @return
	 */
	public ColumnConfig getColumnConfig( String columnName ) {
		return ( ColumnConfig ) columns.get( columnName.toUpperCase() );
	}

	/**
	 * ��ӱ�������bean��������ӳ����Ϣ
	 * 
	 * @param columnName ������
	 * @param fieldName bean������
	 */
	public void addPropMapping( String columnName, String fieldName ) {
		propMapping.put( columnName.toUpperCase(), fieldName );
	}

	/**
	 * ����bean��������ȡ��ӳ��ı�������<br>
	 * <br>
	 * ���Ȳ���ӳ����Ϣ�����ҵ��򷵻أ��������Ƿ������������ͬ�������������ҵ��򷵻أ���������Ҳ������򷵻�null
	 * 
	 * @param fieldName bean������
	 * @return ������
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
	 * ���ݱ�������ȡ��ӳ���bean������
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
	 * @return �ñ�������������Ϣ�ļ��ϣ�������String�� - ��������Ϣ����ColumnConfig��
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
