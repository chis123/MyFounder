/**
 * $Id: e5project com.founder.e5.db DataType.java 
 * created on 2005-8-8 13:06:35
 * by liyanhui
 */
package com.founder.e5.db;

import java.sql.Types;

/**
 * ���ඨ����E5ϵͳ��ʹ�õ�13�������������Լ�ÿ��E5��������JDBC���������루��java.sql.Types���壩��ӳ���ϵ
 * 
 * @author liyanhui
 * @version 1.0
 * @created 2005-8-8 13:06:35
 */
public final class DataType {

	/**
	 * TODO��DataType����ΪE5DataType
	 */
	private DataType() {
		super();
	}

	public static final String INTEGER = "INTEGER";

	public static final String LONG = "LONG";

	public static final String CHAR = "CHAR";

	public static final String VARCHAR = "VARCHAR";

	public static final String FLOAT = "FLOAT";

	public static final String DOUBLE = "DOUBLE";

	public static final String DATE = "DATE";

	public static final String TIME = "TIME";

	public static final String TIMESTAMP = "TIMESTAMP";

	public static final String CLOB = "CLOB";

	public static final String BLOB = "BLOB";

	public static final String EXTFILE = "EXTFILE";

	/**
	 * ȡ�ø���e5��������Ӧ��JDBC�������ʹ��루��java.sql.Types���壩
	 * 
	 * @param e5TypeName e5����������
	 * @return JDBC�������ʹ���
	 */
	public static final int getTypeCode( String e5TypeName ) {
		if ( INTEGER.equals( e5TypeName ) )
			return Types.INTEGER;

		if ( LONG.equals( e5TypeName ) )
			return Types.BIGINT;

		if ( CHAR.equals( e5TypeName ) )
			return Types.CHAR;

		if ( VARCHAR.equals( e5TypeName ) )
			return Types.VARCHAR;

		if ( FLOAT.equals( e5TypeName ) )
			return Types.FLOAT;

		if ( DOUBLE.equals( e5TypeName ) )
			return Types.DOUBLE;

		if ( DATE.equals( e5TypeName ) )
			return Types.DATE;

		if ( TIME.equals( e5TypeName ) )
			return Types.TIME;

		if ( TIMESTAMP.equals( e5TypeName ) )
			return Types.TIMESTAMP;

		if ( CLOB.equals( e5TypeName ) )
			return Types.CLOB;

		if ( BLOB.equals( e5TypeName ) )
			return Types.BLOB;

		if ( EXTFILE.equals( e5TypeName ) )
			return Types.VARCHAR;

		throw new IllegalArgumentException( "Illegal e5TypeName: " + e5TypeName );
	}

}
