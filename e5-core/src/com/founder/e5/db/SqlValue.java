package com.founder.e5.db;

/**
 * �ýӿڴ���һ����sql���ʽ��ʾ��ֵ��<br>
 * <br>
 * ʵ���˸ýӿڵĶ�����DBSession����ʹ�ã�������executeQuery��executeUpdateʱ��Ϊ����ֵ���룬Ӱ��ʵ��ִ�е�sql��䡣<br>
 * ���ӣ�<br>
 * SqlValue param1 = ... // ��toSqlString()���ء�sysdate��<br>
 * String sql = "insert into table1 (date) values (?)";<br>
 * dbSession.executeUpdate( sql, new Object[] {param1} );<br>
 * <br>
 * ��ʵ��ִ�е�sql���Ϊ��insert into table1 (date) values (sysdate)
 * 
 * @author liyanhui
 * @version 1.0
 * @created 2006-8-29 17:33:13
 */
public interface SqlValue {

	/**
	 * ����sql���ʽ
	 * 
	 * @return
	 */
	public String toSqlString();

}
