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
 * 保存了特定数据库sql方言信息<br>
 * 参考自org.hibernate.dialect.Dialect
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
	 * 工厂方法
	 * 
	 * @param dbType 数据库类型
	 * @return Dialect实例
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

	// 该方言的数据类型映射信息
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
	 * 是否支持bfile类型
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
	// 范围查询

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
	 * 获得范围查询语句
	 * 
	 * @param querySelect 不包含选取范围的原始sql语句
	 * @param offset 开始位置，从1开始
	 * @param limit 记录条数
	 * @return
	 */
	public String getLimitString( String querySelect, int offset, int limit ) {
		return getLimitString( querySelect, offset > 0 );
	}

	/**
	 * 获得范围查询语句
	 * 
	 * @param querySelect 不包含选取范围的原始sql语句
	 * @param offset 开始位置，从1开始
	 * @param limit 记录条数
	 * @return
	 */
	public String getLimitString( String querySelect, String offset, String limit ) {
		throw new UnsupportedOperationException( "paged queries not supported" );
	}

	// -------------------------------------------------------------------------

	/**
	 * 返回数据库类型（由com.founder.e5.db.DBType定义）
	 */
	public abstract String getDBType();

	// -------------------------------------------------------------------------

	/**
	 * 包装给定的字段值，以便能用在sql语句中
	 * 
	 * @param type 字段类型
	 * @param value 字段值
	 * @return sql直接量
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

			// TODO:这里是不是应该抛异常
			default :
				return value;
		}
	}

	/**
	 * 根据字段信息生成用于定义字段的DDL语句片断
	 * 
	 * @param columnInfo 字段信息
	 * @return DDL语句片断（如：SYS_DELETEFLAG number not null default 0）
	 */
	public String getColumnDDL( ColumnInfo columnInfo ) {
		StringBuffer sb = new StringBuffer();
		sb.append( columnInfo.getName() ).append( " " );

		int type = DataType.getTypeCode( columnInfo.getE5TypeName() );
		int length = columnInfo.getDataLength();
		//若是整数或实数，并且未设置长度，则改为10
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
	 * 根据字段信息和表名生成添加字段的DLL语句
	 * 
	 * @param columnInfoArray - 字段信息数组
	 * @param tableName - 表名
	 * @return 完整的DDL语句,如alter table DOM_1_DOCLIB add content varchar(200)
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
	 * 根据字段信息和表名生成添加字段的DLL语句，add后的列名有括号包围。
	 * 这是专门为子类准备的getAddColumnDDL第二种形式，带括号。
	 * （可能是比较常见的形式，因此做在父类里，子类可直接调用）
	 * @param columnInfoArray - 字段信息数组
	 * @param tableName - 表名
	 * @return 完整的DDL语句,如alter table DOM_1_DOCLIB add content varchar(200)
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
	 * 根据e5数据类型，取得其用于sql语句中的占位符。<br>
	 * 对大多数类型，该占为符为'?'，但对于Oracle的extfile类型（实际对应到bfile类型），其占位符为'bfilename(?,?)'
	 * 
	 * @param e5DataType E5数据类型名
	 * @return 占位符字符串，用在insert或update语句中
	 * @deprecated 这个功能好像应该放到Type中
	 */
	public String getPlaceHolder( String e5DataType ) {
		return "?";
	}

	/**
	 * 得到用于生成空clob值的sql片断
	 * 
	 */
	public String getEmptyClobSql() {
		return "''";
	}

	/**
	 * 得到用于生成空blob值的sql片断
	 * 
	 */
	public String getEmptyBlobSql() {
		return "null";
	}

	// -------------------------------------------------------------------------

	/**
	 * 生成查询给定序列的下一个值的sql语句
	 * 
	 * @param sequenceName 序列名
	 * @return sql语句
	 * @throws MappingException
	 */
	public String getSelectSequenceNextValString( String sequenceName )
			throws MappingException {
		throw new MappingException( "Dialect " + this
				+ " doesn't support sequence" );
	}

	/**
	 * 写入内容到数据库的Clob类型字段。 <br>
	 * <br>
	 * 注意：该方法必须配合beginTransaction、commitTransaction使用
	 * 
	 * @param id 记录标识值
	 * @param tablename 数据表名
	 * @param idColumnName 标识字段名
	 * @param lobColumnName Clob字段名
	 * @param clob 待写入内容
	 * @exception IOException
	 * @exception SQLException
	 */
	public abstract void writeClob( long id, String tablename,
			String idColumnName, String lobColumnName, IClob clob )
			throws SQLException, IOException;

	/**
	 * 写入内容到数据库的Blob类型字段<br>
	 * 注意：该方法必须配合beginTransaction、commitTransaction使用
	 * 
	 * @param id 记录标识值
	 * @param tablename 数据表名
	 * @param idColumnName 标识字段名
	 * @param lobColumnName Blob字段名
	 * @param blob 待写入内容
	 * @exception IOException
	 * @exception SQLException
	 */
	public abstract void writeBlob( long id, String tablename,
			String idColumnName, String lobColumnName, IBlob blob )
			throws SQLException, IOException;

	// -------------------------------------------------------------------------

	/**
	 * 从结果集中取得Clob类型字段值
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
	 * 从结果集中取得Blob类型字段值
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
	 * 从结果集中取得Bfile类型字段值
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
