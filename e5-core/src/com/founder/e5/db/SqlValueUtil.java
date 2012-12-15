package com.founder.e5.db;

/**
 * �ṩ����SqlValue����ֵ�Ĺ��߷���
 * 
 * @author liyanhui
 * @version 1.0
 * @created 2006-8-29 22:06:53
 */
public class SqlValueUtil {

	/**
	 * �Ѹ���sql���ʽ��װΪSqlValue���Ͷ�����toSqlString()�����ظñ��ʽ��
	 * 
	 * @param sql sql���ʽ������һ��ֵ
	 * @return SqlValue���Ͷ�����toSqlString()�����ز������ʽ��
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
