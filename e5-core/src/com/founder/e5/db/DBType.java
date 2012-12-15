package com.founder.e5.db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import javax.sql.DataSource;

/**
 * 该类定义了“数据库类型”字符串常量，以及自动探测数据库类型的工具方法<br>
 * <p>
 * 注意：在E5系统中使用Hibernate的情况下，定义的数据库类型常量名称，要与Hibernate的dialect类名前缀部分相一致。<br>
 * 如Hiberante中一个org.hibernate.dialect.Oracle9Dialect类，这里的定义就是"oracle"。<br>
 * 常量值一律小写。
 * </p>
 * 
 * @author liyanhui
 * @version 1.0
 * @created 2006-3-30 10:37:37
 */
public class DBType {

	private DBType() {
	}

	public static final String ORACLE = "oracle";
	public static final String SQLSERVER = "sqlserver";
	public static final String MYSQL = "mysql";
	public static final String SYBASE = "sybase";
	public static final String DB2 = "db2";
	public static final String POSTGRESQL = "postgresql";
	
	private static String[] All = { ORACLE, SQLSERVER, MYSQL, SYBASE, DB2, POSTGRESQL};
	/**
	 * 取得当前所有的数据库类型名的集合
	 */
	public static final String[] getAllTypes() {
		return All;
	}
	
	/**
	 * 需要扩展系统支持的数据库类型时，使用此方法
	 * @param allTypes
	 */
	public static void setAllTypes(String[] allTypes)
	{
		All = allTypes;
	}
	
	/**
	 * 根据从DatabaseMetaData中获得的databaseProductName信息猜测数据库类型<br>
	 * <br>
	 * 若猜测失败，则返回null
	 * 
	 * @param conn
	 * @return 数据库类型字符串
	 * @throws SQLException
	 */
	public static String guessDBType( Connection conn ) throws SQLException {
		DatabaseMetaData dbmd = conn.getMetaData();
		if ( dbmd != null ) {
			String name = dbmd.getDatabaseProductName();
			if ( name != null ) {
				name = name.toLowerCase();
				for (int i = 0; i < All.length; i++) {
					if ( name.indexOf(All[i]) != -1 )
						return All[i];
				}
				if ( name.indexOf( "sql server" ) != -1 )
					return SQLSERVER;
			}
		}
		return null;
	}

	/**
	 * 根据从DatabaseMetaData中获得的databaseProductName信息猜测数据库类型<br>
	 * <br>
	 * 若猜测失败，则返回null
	 * 
	 * @param ds
	 * @return 数据库类型字符串
	 * @throws SQLException
	 */
	public static String guessDBType( DataSource ds ) throws SQLException {
		Connection conn = ds.getConnection();
		try {
			return guessDBType( conn );
		} finally {
			try {
				conn.close();
			} catch ( SQLException e ) {
				// ignore this
			}
		}
	}

}
