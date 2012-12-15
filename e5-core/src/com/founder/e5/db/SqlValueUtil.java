package com.founder.e5.db;

/**
 * 提供创建SqlValue类型值的工具方法
 * 
 * @author liyanhui
 * @version 1.0
 * @created 2006-8-29 22:06:53
 */
public class SqlValueUtil {

	/**
	 * 把给定sql表达式包装为SqlValue类型对象（其toSqlString()即返回该表达式）
	 * 
	 * @param sql sql表达式，代表一个值
	 * @return SqlValue类型对象（其toSqlString()即返回参数表达式）
	 */
	public static SqlValue wrap( String sql ) {
		return new SqlValueImpl( sql );
	}

	// -------------------------------------------------------------------------

	private static class SqlValueImpl implements SqlValue {

		private String sql;

		/**
		 * @param sql
		 */
		public SqlValueImpl( String sql ) {
			if ( sql == null )
				throw new NullPointerException();
			this.sql = sql;
		}

		/**
		 * @see com.founder.e5.db.SqlValue#toSqlString()
		 */
		public String toSqlString() {
			return sql;
		}

		public boolean equals( Object obj ) {
			if ( obj instanceof SqlValue ) {
				return sql.equals( ( ( SqlValue ) obj ).toSqlString() );

			}
			return false;
		}

		public int hashCode() {
			return sql.hashCode();
		}

		public String toString() {
			return sql;
		}
	}

}
