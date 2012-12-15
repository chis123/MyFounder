package com.founder.e5.db;

/**
 * �������ֶ���Ϣ<br>
 * �������ֶ������ֶ���ʾ���ơ������롢�������͡��ɿա�Ψһ�����ȡ����ȡ�ȱʡֵ<br>
 * ����һ�������ĵ������ֶι�����
 * 
 * @version 1.0
 * @created 07-����-2005 09:59:53
 */
public class ColumnInfo {

	private String name; // �ֶ���

	private String displayName; // �ֶ���ʾ����

	private int type; // �����룬��java.sql.Types����

	private String typeName; // ���ض����ݿ��е�������

	private String e5TypeName; // ��e5ϵͳ�е�������

	private boolean nullable; // �Ƿ�ɿ�

	private boolean unique; // �Ƿ�Ψһ

	private int dataLength; // ���ݳ���

	private int dataPrecision; // ���ݾ��ȣ���������Ч��

	private String defaultValue; // Ĭ��ֵ

	/**
	 * 
	 */
	public ColumnInfo() {
		super();
	}

	/**
	 * @param name ����
	 * @param displayName ����ʾ��
	 * @param e5TypeName ��e5ϵͳ�е�������
	 * @param nullable �Ƿ�ɿ�
	 * @param dataLength ���ݳ���
	 * @param dataPrecision ���ݾ���
	 * @param defaultValue Ĭ��ֵ
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
	 * @param columnCode ����
	 * @param columnName ����ʾ��
	 * @param dataType ������
	 * @param nullable �Ƿ�ɿ�
	 * @param unique �Ƿ�Ψһ
	 * @param dataLength ���ݳ���
	 * @param dataPrecision ���ݾ���
	 * @param defaultValue Ĭ��ֵ
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
	 * @return �ֶ���ʾ��
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * @param �ֶ���ʾ��
	 */
	public void setDisplayName( String displayName ) {
		this.displayName = displayName;
	}

	/**
	 * @return ���ݳ��ȣ������֡��ַ��������壩
	 */
	public int getDataLength() {
		return dataLength;
	}

	/**
	 * @param ���ݳ��ȣ������֡��ַ��������壩
	 */
	public void setDataLength( int dataLength ) {
		this.dataLength = dataLength;
	}

	/**
	 * @return ���ȣ��������������壩
	 */
	public int getDataPrecision() {
		return dataPrecision;
	}

	/**
	 * @param ���ȣ��������������壩
	 */
	public void setDataPrecision( int dataPrecision ) {
		this.dataPrecision = dataPrecision;
	}

	/**
	 * @return ��e5ϵͳ�е�������
	 */
	public String getE5TypeName() {
		return e5TypeName;
	}

	/**
	 * @param ��e5ϵͳ�е�������
	 */
	public void setE5TypeName( String e5TypeName ) {
		this.e5TypeName = e5TypeName;
	}

	/**
	 * @return Ĭ��ֵ
	 */
	public String getDefaultValue() {
		return defaultValue;
	}

	/**
	 * @param Ĭ��ֵ
	 */
	public void setDefaultValue( String defaultValue ) {
		this.defaultValue = defaultValue;
	}

	/**
	 * @return �Ƿ�ɿ�
	 */
	public boolean isNullable() {
		return nullable;
	}

	/**
	 * @param �Ƿ�ɿ�
	 */
	public void setNullable( boolean nullable ) {
		this.nullable = nullable;
	}

	/**
	 * @return �Ƿ�Ψһ
	 */
	public boolean isUnique() {
		return unique;
	}

	/**
	 * @param �Ƿ�Ψһ
	 */
	public void setUnique( boolean unique ) {
		this.unique = unique;
	}

	/**
	 * @return �����룬��java.sql.Types����
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param type �����룬��java.sql.Types����
	 */
	public void setType( int type ) {
		this.type = type;
	}

	/**
	 * @return ���ض����ݿ��е�������
	 */
	public String getTypeName() {
		return typeName;
	}

	/**
	 * @param ���ض����ݿ��е�������
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
