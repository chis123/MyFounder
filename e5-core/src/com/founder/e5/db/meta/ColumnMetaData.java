package com.founder.e5.db.meta;

/**
 * �ֶ�Ԫ���ݶ���
 * 
 * @author liyanhui
 * @version 1.0
 * @created 2006-3-1 10:26:35
 */
public class ColumnMetaData {

	/** ���� */
	private String name;

	/** ���е���������(java.sql.Types����ֵ) */
	private int type;

	/** ���е����������� */
	private String typeName;

	/** ���е��ֽ������� */
	private int dataLength;

	/** NUMBER���еľ������� */
	private int dataPrecision;

	/** NUMBER���е�С����λ�� */
	private int dataScale;

	/** ��ֵ�ɷ�Ϊ�� */
	private boolean nullable = true;

	/**
	 * @param name
	 */
	public ColumnMetaData( String name ) {
		if ( name == null )
			throw new NullPointerException();
		this.name = name;
	}

	public int getDataLength() {
		return dataLength;
	}

	public void setDataLength( int dataLength ) {
		this.dataLength = dataLength;
	}

	public int getDataPrecision() {
		return dataPrecision;
	}

	public void setDataPrecision( int dataPrecision ) {
		this.dataPrecision = dataPrecision;
	}

	public int getDataScale() {
		return dataScale;
	}

	public void setDataScale( int dataScale ) {
		this.dataScale = dataScale;
	}

	public String getName() {
		return name;
	}

	public void setName( String name ) {
		this.name = name;
	}

	public boolean isNullable() {
		return nullable;
	}

	public void setNullable( boolean nullable ) {
		this.nullable = nullable;
	}

	public int getType() {
		return type;
	}

	public void setType( int type ) {
		this.type = type;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName( String typeName ) {
		this.typeName = typeName;
	}

	public boolean equals( Object obj ) {
		if ( obj instanceof ColumnMetaData ) {
			return ( ( ColumnMetaData ) obj ).name.equals( name );
		}
		return false;
	}

	public int hashCode() {
		return name.hashCode();
	}

	public String toString() {
		return name + ", " + typeName;
	}

}
