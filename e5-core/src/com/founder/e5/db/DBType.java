package com.founder.e5.db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import javax.sql.DataSource;

/**
 * ���ඨ���ˡ����ݿ����͡��ַ����������Լ��Զ�̽�����ݿ����͵Ĺ��߷���<br>
 * <p>
 * ע�⣺��E5ϵͳ��ʹ��Hibernate������£���������ݿ����ͳ������ƣ�Ҫ��Hibernate��dialect����ǰ׺������һ�¡�<br>
 * ��Hiberante��һ��org.hibernate.dialect.Oracle9Dialect�࣬����Ķ������"oracle"��<br>
 * ����ֵһ��Сд��
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
	 * ȡ�õ�ǰ���е����ݿ��������ļ���
	 */
	public static final String[] getAllTypes() {
		return All;
	}
	
	/**
	 * ��Ҫ��չϵͳ֧�ֵ����ݿ�����ʱ��ʹ�ô˷���
	 * @param allTypes
	 */
	public static void setAllTypes(String[] allTypes)
	{
		All = allTypes;
	}
	
	/**
	 * ���ݴ�DatabaseMetaData�л�õ�databaseProductName��Ϣ�²����ݿ�����<br>
	 * <br>
	 * ���²�ʧ�ܣ��򷵻�null
	 * 
	 * @param conn
	 * @return ���ݿ������ַ���
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
	 * ���ݴ�DatabaseMetaData�л�õ�databaseProductName��Ϣ�²����ݿ�����<br>
	 * <br>
	 * ���²�ʧ�ܣ��򷵻�null
	 * 
	 * @param ds
	 * @return ���ݿ������ַ���
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
