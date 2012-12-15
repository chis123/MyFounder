package com.founder.e5.db.meta;

/**
 * 字段元数据对象
 * 
 * @author liyanhui
 * @version 1.0
 * @created 2006-3-1 10:26:35
 */
public class ColumnMetaData {

	/** 列名 */
	private String name;

	/** 该列的数据类型(java.sql.Types类型值) */
	private int type;

	/** 该列的数据类型名 */
	private String typeName;

	/** 该列的字节数限制 */
	private int dataLength;

	/** NUMBER型列的精度限制 */
	private int dataPrecision;

	/** NUMBER型列的小数点位数 */
	private int dataScale;

	/** 列值可否为空 */
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
