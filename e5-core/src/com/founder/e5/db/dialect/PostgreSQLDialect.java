package com.founder.e5.db.dialect;

import java.sql.Types;

import com.founder.e5.db.ColumnInfo;
import com.founder.e5.db.DBType;
import com.founder.e5.db.MappingException;

/**
 * PostgreSQL数据库Dialect
 * 
 * @author wanghc
 * @created 2009-3-10 下午03:39:27
 */
public class PostgreSQLDialect extends CommonDialect 
{
	public PostgreSQLDialect(){
		super();
		registerColumnType( Types.BIT, "bool" );
		registerColumnType( Types.BIGINT, "int8" );
		registerColumnType( Types.SMALLINT, "int2" );
		registerColumnType( Types.TINYINT, "int2" );
		registerColumnType( Types.INTEGER, "int4" );
		registerColumnType( Types.CHAR, "char(1)" );
		registerColumnType( Types.VARCHAR, "varchar($l)" );
		registerColumnType( Types.FLOAT, "float4" );
		registerColumnType( Types.DOUBLE, "float8" );
		registerColumnType( Types.DATE, "date" );
		registerColumnType( Types.TIME, "time" );
		registerColumnType( Types.TIMESTAMP, "timestamp" );
		registerColumnType( Types.VARBINARY, "bytea" );
		registerColumnType( Types.CLOB, "text" );
		registerColumnType( Types.BLOB, "bytea" ); 
		registerColumnType( Types.NUMERIC, "numeric($p, $s)" );
	}
	/* (non-Javadoc)
	 * @see com.founder.e5.db.dialect.Dialect#getDBType()
	 */
	public String getDBType()
	{
		return DBType.POSTGRESQL;
	}
	
	/* (non-Javadoc)
	 * @see com.founder.e5.db.dialect.Dialect#getLimitString(java.lang.String, boolean)
	 */
	public String getLimitString(String sql, boolean hasOffset)
	{
		return new StringBuffer( sql.length()+20 )
			.append(sql)
			.append( hasOffset ? " limit ? offset ?" : " limit ?")
			.toString();
	}
	public String getLimitString( String sql, int offset, int limit ) {
		StringBuffer result = new StringBuffer( sql.length() + 20 );
		
		//若=0，则起始位置其实没限制
		if (offset > 0)	
			result.append(sql).append(" offset ").append(offset).append(" limit ").append(limit);
		else
			result.append(sql).append(" limit ").append(limit);
		return result.toString();
	}
	
	/**
	 * @see com.founder.e5.db.dialect.Dialect#getSelectSequenceNextValString(java.lang.String)
	 */
	public String getSelectSequenceNextValString( String sequenceName )
			throws MappingException {
		StringBuffer sb = new StringBuffer();
		sb.append( "select nextval('" ).append( sequenceName ).append(
				"')" );
		return sb.toString();
	}
	
	/* (non-Javadoc)
	 * @see com.founder.e5.db.dialect.Dialect#supportsLimit()
	 */
	public boolean supportsLimit()
	{
		return true;
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.db.dialect.Dialect#supportsSequences()
	 */
	public boolean supportsSequences()
	{
		return true;
	}
	
	/* (non-Javadoc)
	 * @see com.founder.e5.db.dialect.Dialect#getAddColumnDDL(com.founder.e5.db.ColumnInfo[], java.lang.String)
	 */
	public String getAddColumnDDL(ColumnInfo[] columnInfoArray, String tableName)
	{
		StringBuffer alterSQLBuf = new StringBuffer();
        alterSQLBuf.append("alter table "); //alter table ATEST add VVVVV number;
        alterSQLBuf.append(tableName);

        for (int i = 0; i < columnInfoArray.length; i++) {
            String ddl = this.getColumnDDL(columnInfoArray[i]);
            alterSQLBuf.append(" add ");
            alterSQLBuf.append(ddl).append(",");
            
        }
        alterSQLBuf.deleteCharAt(alterSQLBuf.length() - 1);
        return alterSQLBuf.toString();
	}
}
