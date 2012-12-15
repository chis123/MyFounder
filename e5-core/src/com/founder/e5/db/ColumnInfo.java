package com.founder.e5.db;

/**
 * 描述表字段信息<br>
 * 包括：字段名、字段显示名称、类型码、数据类型、可空、唯一、长度、精度、缺省值<br>
 * 该类一般用在文档类型字段管理部分
 * 
 * @version 1.0
 * @created 07-七月-2005 09:59:53
 */
public class ColumnInfo {

	private String name; // 字段名

	private String displayName; // 字段显示名称

	private int type; // 类型码，由java.sql.Types定义

	private String typeName; // 在特定数据库中的类型名

	private String e5TypeName; // 在e5系统中的类型名

	private boolean nullable; // 是否可空

	private boolean unique; // 是否唯一

	private int dataLength; // 数据长度

	private int dataPrecision; // 数据精度（对数字有效）

	private String defaultValue; // 默认值

	/**
	 * 
	 */
	public ColumnInfo() {
		super();
	}

	/**
	 * @param name 列名
	 * @param displayName 列显示名
	 * @param e5TypeName 在e5系统中的类型名
	 * @param nullable 是否可空
	 * @param dataLength 数据长度
	 * @param dataPrecision 数据精度
	 * @param defaultValue 默认值
	 */
	public ColumnInfo( String name, String displayName, String e5TypeName,
			boolean nullable, int dataLength, int dataPrecision,
			String defaultValue ) {
		super();
		this.name = name;
		this.displayName = displayName;
		this.e5TypeName = e5TypeName;
		this.nullable = nullable;
		this.dataLength = dataLength;
		this.dataPrecision = dataPrecision;
		this.defaultValue = defaultValue;
	}

	/**
	 * @param columnCode 列名
	 * @param columnName 列显示名
	 * @param dataType 列类型
	 * @param nullable 是否可空
	 * @param unique 是否唯一
	 * @param dataLength 数据长度
	 * @param dataPrecision 数据精度
	 * @param defaultValue 默认值
	 */
	public ColumnInfo( String columnCode, String columnName, String dataType,
			boolean nullable, boolean unique, int dataLength,
			int dataPrecision, String defaultValue ) {
		super();
		this.name = columnCode;
		this.displayName = columnName;
		this.e5TypeName = dataType;
		this.nullable = nullable;
		this.unique = unique;
		this.dataLength = dataLength;
		this.dataPrecision = dataPrecision;
		this.defaultValue = defaultValue;
	}

	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name The name to set.
	 */
	public void setName( String name ) {
		this.name = name;
	}

	/**
	 * @return 字段显示名
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * @param 字段显示名
	 */
	public void setDisplayName( String displayName ) {
		this.displayName = displayName;
	}

	/**
	 * @return 数据长度（对数字、字符型有意义）
	 */
	public int getDataLength() {
		return dataLength;
	}

	/**
	 * @param 数据长度（对数字、字符型有意义）
	 */
	public void setDataLength( int dataLength ) {
		this.dataLength = dataLength;
	}

	/**
	 * @return 精度（对数字型有意义）
	 */
	public int getDataPrecision() {
		return dataPrecision;
	}

	/**
	 * @param 精度（对数字型有意义）
	 */
	public void setDataPrecision( int dataPrecision ) {
		this.dataPrecision = dataPrecision;
	}

	/**
	 * @return 在e5系统中的类型名
	 */
	public String getE5TypeName() {
		return e5TypeName;
	}

	/**
	 * @param 在e5系统中的类型名
	 */
	public void setE5TypeName( String e5TypeName ) {
		this.e5TypeName = e5TypeName;
	}

	/**
	 * @return 默认值
	 */
	public String getDefaultValue() {
		return defaultValue;
	}

	/**
	 * @param 默认值
	 */
	public void setDefaultValue( String defaultValue ) {
		this.defaultValue = defaultValue;
	}

	/**
	 * @return 是否可空
	 */
	public boolean isNullable() {
		return nullable;
	}

	/**
	 * @param 是否可空
	 */
	public void setNullable( boolean nullable ) {
		this.nullable = nullable;
	}

	/**
	 * @return 是否唯一
	 */
	public boolean isUnique() {
		return unique;
	}

	/**
	 * @param 是否唯一
	 */
	public void setUnique( boolean unique ) {
		this.unique = unique;
	}

	/**
	 * @return 类型码，由java.sql.Types定义
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param type 类型码，由java.sql.Types定义
	 */
	public void setType( int type ) {
		this.type = type;
	}

	/**
	 * @return 在特定数据库中的类型名
	 */
	public String getTypeName() {
		return typeName;
	}

	/**
	 * @param 在特定数据库中的类型名
	 */
	public void setTypeName( String typeName ) {
		this.typeName = typeName;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals( Object obj ) {
		if ( obj instanceof ColumnInfo ) {
			return name.equals( ( ( ColumnInfo ) obj ).name );
		}
		return false;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return name.hashCode();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return name;
	}
}
