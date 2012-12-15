/**
 * $Id: e5new com.founder.e5.db.dialect Dialect.java created on 2006-3-29
 * 10:31:59 by liyanhui
 */
package com.founder.e5.db.dialect;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import com.founder.e5.db.ColumnInfo;
import com.founder.e5.db.DBType;
import com.founder.e5.db.DataType;
import com.founder.e5.db.IBfile;
import com.founder.e5.db.IBlob;
import com.founder.e5.db.IClob;
import com.founder.e5.db.MappingException;

/**
 * �������ض����ݿ�sql������Ϣ<br>
 * �ο���org.hibernate.dialect.Dialect
 * 
 * @author liyanhui
 * @version 1.0
 * @created 2006-3-29 10:31:59
 */
public abstract class Dialect {

	private static final Dialect ORACLE = new OracleDialect();
	private static final Dialect SQLSERVER = new SQLServerDialect();
	private static final Dialect MYSQL = new MySQLDialect();
	private static final Dialect SYBASE = new SybaseDialect();
	private static final Dialect POSTGRESQL = new PostgreSQLDialect();
	
	/**
	 * ��������
	 * 
	 * @param dbType ���ݿ�����
	 * @return Dialectʵ��
	 */
	public static Dialect getDialect( String dbType ) {
		if ( DBType.ORACLE.equals( dbType ) ) {
			return ORACLE;
		}
		if ( DBType.SQLSERVER.equals( dbType ) ) {
			return SQLSERVER;
		}
		if ( DBType.MYSQL.equals( dbType ) ) {
			return MYSQL;
		}
		if ( DBType.SYBASE.equals( dbType ) ) {
			return SYBASE;
		}
		if ( DBType.POSTGRESQL.equals( dbType ) ){
			return POSTGRESQL;
		}
			
		throw new RuntimeException( "unsupported dbType: " + dbType );
	}

	// --------------------------------------------------------------------------

	// �÷��Ե���������ӳ����Ϣ
	private final TypeNames typeNames = new TypeNames();

	/**
	 * Get the name of the database type associated with the given
	 * <tt>java.sql.Types</tt> typecode.
	 * 
	 * @param code <tt>java.sql.Types</tt> typecode
	 * @return the database type name
	 * @throws MappingException
	 */
	public String getTypeName( int code ) throws MappingException {
		String result = typeNames.get( code );
		if ( result == null ) {
			throw new MappingException(
					"No default type mapping for (java.sql.Types) " + code );
		}
		return result;
	}

	/**
	 * Get the name of the database type associated with the given
	 * <tt>java.sql.Types</tt> typecode.
	 * 
	 * @param code <tt>java.sql.Types</tt> typecode
	 * @param length the length or precision of the column
	 * @param precision the scale of the column
	 * @param scale
	 * 
	 * @return the database type name
	 * @throws MappingException
	 */
	public String getTypeName( int code, int length, int precision, int scale )
			throws MappingException {
		String result = typeNames.get( code, length, precision, scale );
		if ( result == null ) {
			throw new MappingException(
					"No type mapping for java.sql.Types code: " + code
							+ ", length: " + length );
		}
		return result;
	}

	/**
	 * Subclasses register a typename for the given type code and maximum column
	 * length. <tt>$l</tt> in the type name with be replaced by the column
	 * length (if appropriate).
	 * 
	 * @param code <tt>java.sql.Types</tt> typecode
	 * @param capacity maximum length of database type
	 * @param name the database type name
	 */
	protected void registerColumnType( int code, int capacity, String name ) {
		typeNames.put( code, capacity, name );
	}

	/**
	 * Subclasses register a typename for the given type code. <tt>$l</tt> in
	 * the type name with be replaced by the column length (if appropriate).
	 * 
	 * @param code <tt>java.sql.Types</tt> typecode
	 * @param name the database type name
	 */
	protected void registerColumnType( int code, String name ) {
		typeNames.put( code, name );
	}

	/**
	 * Does this dialect support the <tt>UNIQUE</tt> column syntax?
	 * 
	 * @return boolean
	 */
	public boolean supportsUnique() {
		return true;
	}

	/**
	 * �Ƿ�֧��bfile����
	 * 
	 * @return boolean
	 */
	public boolean supportBfile() {
		return false;
	}

	/**
	 * Does this dialect support sequences?
	 * 
	 * @return boolean
	 */
	public boolean supportsSequences() {
		return false;
	}

	// -------------------------------------------------------------------------
	// ��Χ��ѯ

	/**
	 * Does this <tt>Dialect</tt> have some kind of <tt>LIMIT</tt> syntax?
	 */
	public boolean supportsLimit() {
		return false;
	}

	/**
	 * Does this dialect support an offset?
	 */
	public boolean supportsLimitOffset() {
		return supportsLimit();
	}

	/**
	 * Add a <tt>LIMIT</tt> clause to the given SQL <tt>SELECT</tt>
	 * 
	 * @return the modified SQL
	 */
	public String getLimitString( String querySelect, boolean hasOffset ) {
		throw new UnsupportedOperationException( "paged queries not supported" );
	}

	/**
	 * ��÷�Χ��ѯ���
	 * 
	 * @param querySelect ������ѡȡ��Χ��ԭʼsql���
	 * @param offset ��ʼλ�ã���1��ʼ
	 * @param limit ��¼����
	 * @return
	 */
	public String getLimitString( String querySelect, int offset, int limit ) {
		return getLimitString( querySelect, offset > 0 );
	}

	/**
	 * ��÷�Χ��ѯ���
	 * 
	 * @param querySelect ������ѡȡ��Χ��ԭʼsql���
	 * @param offset ��ʼλ�ã���1��ʼ
	 * @param limit ��¼����
	 * @return
	 */
	public String getLimitString( String querySelect, String offset, String limit ) {
		throw new UnsupportedOperationException( "paged queries not supported" );
	}

	// -------------------------------------------------------------------------

	/**
	 * �������ݿ����ͣ���com.founder.e5.db.DBType���壩
	 */
	public abstract String getDBType();

	// -------------------------------------------------------------------------

	/**
	 * ��װ�������ֶ�ֵ���Ա�������sql�����
	 * 
	 * @param type �ֶ�����
	 * @param value �ֶ�ֵ
	 * @return sqlֱ����
	 */
	public String getWrappedValue( int type, String value ) {
		if ( type == Types.VARCHAR )
			return '\'' + value + '\'';

		switch ( type ) {
			case Types.VARCHAR :
			case Types.CHAR :
				return '\'' + value + '\'';
			case Types.INTEGER :
			case Types.BIGINT :
			case Types.FLOAT :
			case Types.DOUBLE :
				return value;

			// TODO:�����ǲ���Ӧ�����쳣
			default :
				return value;
		}
	}

	/**
	 * �����ֶ���Ϣ�������ڶ����ֶε�DDL���Ƭ��
	 * 
	 * @param columnInfo �ֶ���Ϣ
	 * @return DDL���Ƭ�ϣ��磺SYS_DELETEFLAG number not null default 0��
	 */
	public String getColumnDDL( ColumnInfo columnInfo ) {
		StringBuffer sb = new StringBuffer();
		sb.append( columnInfo.getName() ).append( " " );

		int type = DataType.getTypeCode( columnInfo.getE5TypeName() );
		int length = columnInfo.getDataLength();
		//����������ʵ��������δ���ó��ȣ����Ϊ10
		if (length == 0 && (type == Types.INTEGER || type == Types.BIGINT || type == Types.FLOAT))
			length = 10;
		int precision = columnInfo.getDataPrecision();
		String typeName = getTypeName( type, length, precision, 0 );

		sb.append( typeName );

		String defaultValue = columnInfo.getDefaultValue();
		if ( defaultValue != null && !"".equals( defaultValue.trim() ) ) {
			sb.append( " default " ).append(
					getWrappedValue( type, defaultValue ) );
		}

		if ( columnInfo.isNullable() )
			sb.append( " null" );
		else
			sb.append( " not null" );

		if ( supportsUnique() && columnInfo.isUnique() )
			sb.append( " unique" );

		return sb.toString();
	}

	/**
	 * �����ֶ���Ϣ�ͱ�����������ֶε�DLL���
	 * 
	 * @param columnInfoArray - �ֶ���Ϣ����
	 * @param tableName - ����
	 * @return ������DDL���,��alter table DOM_1_DOCLIB add content varchar(200)
	 */
	public String getAddColumnDDL(ColumnInfo[] columnInfoArray,String tableName)
	{
		StringBuffer alterSQLBuf = new StringBuffer();
        alterSQLBuf.append("alter table "); //alter table ATEST add VVVVV number;
        alterSQLBuf.append(tableName).append(" add ");

        for (int i = 0; i < columnInfoArray.length; i++) {
            String ddl = this.getColumnDDL(columnInfoArray[i]);
            alterSQLBuf.append(ddl).append(",");
            
        }
        alterSQLBuf.deleteCharAt(alterSQLBuf.length() - 1);
        return alterSQLBuf.toString();
	}
	/**
	 * �����ֶ���Ϣ�ͱ�����������ֶε�DLL��䣬add������������Ű�Χ��
	 * ����ר��Ϊ����׼����getAddColumnDDL�ڶ�����ʽ�������š�
	 * �������ǱȽϳ�������ʽ��������ڸ���������ֱ�ӵ��ã�
	 * @param columnInfoArray - �ֶ���Ϣ����
	 * @param tableName - ����
	 * @return ������DDL���,��alter table DOM_1_DOCLIB add content varchar(200)
	 */
	protected String getAddColumnDDL2(ColumnInfo[] columnInfoArray, String tableName)
	{
		StringBuffer alterSQLBuf = new StringBuffer();
        alterSQLBuf.append("alter table "); //alter table ATEST add VVVVV number;
        alterSQLBuf.append(tableName).append(" add (");

        for (int i = 0; i < columnInfoArray.length; i++) {
            String ddl = this.getColumnDDL(columnInfoArray[i]);
            alterSQLBuf.append(ddl).append(",");
            
        }
        alterSQLBuf.deleteCharAt(alterSQLBuf.length() - 1);
        alterSQLBuf.append(")");
        return alterSQLBuf.toString();
	}
	
	/**
	 * ����e5�������ͣ�ȡ��������sql����е�ռλ����<br>
	 * �Դ�������ͣ���ռΪ��Ϊ'?'��������Oracle��extfile���ͣ�ʵ�ʶ�Ӧ��bfile���ͣ�����ռλ��Ϊ'bfilename(?,?)'
	 * 
	 * @param e5DataType E5����������
	 * @return ռλ���ַ���������insert��update�����
	 * @deprecated ������ܺ���Ӧ�÷ŵ�Type��
	 */
	public String getPlaceHolder( String e5DataType ) {
		return "?";
	}

	/**
	 * �õ��������ɿ�clobֵ��sqlƬ��
	 * 
	 */
	public String getEmptyClobSql() {
		return "''";
	}

	/**
	 * �õ��������ɿ�blobֵ��sqlƬ��
	 * 
	 */
	public String getEmptyBlobSql() {
		return "null";
	}

	// -------------------------------------------------------------------------

	/**
	 * ���ɲ�ѯ�������е���һ��ֵ��sql���
	 * 
	 * @param sequenceName ������
	 * @return sql���
	 * @throws MappingException
	 */
	public String getSelectSequenceNextValString( String sequenceName )
			throws MappingException {
		throw new MappingException( "Dialect " + this
				+ " doesn't support sequence" );
	}

	/**
	 * д�����ݵ����ݿ��Clob�����ֶΡ� <br>
	 * <br>
	 * ע�⣺�÷����������beginTransaction��commitTransactionʹ��
	 * 
	 * @param id ��¼��ʶֵ
	 * @param tablename ���ݱ���
	 * @param idColumnName ��ʶ�ֶ���
	 * @param lobColumnName Clob�ֶ���
	 * @param clob ��д������
	 * @exception IOException
	 * @exception SQLException
	 */
	public abstract void writeClob( long id, String tablename,
			String idColumnName, String lobColumnName, IClob clob )
			throws SQLException, IOException;

	/**
	 * д�����ݵ����ݿ��Blob�����ֶ�<br>
	 * ע�⣺�÷����������beginTransaction��commitTransactionʹ��
	 * 
	 * @param id ��¼��ʶֵ
	 * @param tablename ���ݱ���
	 * @param idColumnName ��ʶ�ֶ���
	 * @param lobColumnName Blob�ֶ���
	 * @param blob ��д������
	 * @exception IOException
	 * @exception SQLException
	 */
	public abstract void writeBlob( long id, String tablename,
			String idColumnName, String lobColumnName, IBlob blob )
			throws SQLException, IOException;

	// -------------------------------------------------------------------------

	/**
	 * �ӽ������ȡ��Clob�����ֶ�ֵ
	 * 
	 * @param rs
	 * @param name
	 * @return
	 * @throws SQLException
	 * @throws IOException
	 */
	public abstract IClob getClob( ResultSet rs, String name )
			throws SQLException, IOException;

	/**
	 * �ӽ������ȡ��Blob�����ֶ�ֵ
	 * 
	 * @param rs
	 * @param name
	 * @return
	 * @throws SQLException
	 * @throws IOException
	 */
	public abstract IBlob getBlob( ResultSet rs, String name )
			throws SQLException, IOException;

	/**
	 * �ӽ������ȡ��Bfile�����ֶ�ֵ
	 * 
	 * @param rs
	 * @param name
	 * @return
	 * @throws SQLException
	 * @throws IOException
	 */
	public abstract IBfile getBfile( ResultSet rs, String name )
			throws SQLException, IOException;
}
