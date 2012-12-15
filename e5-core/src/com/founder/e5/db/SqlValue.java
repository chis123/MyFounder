package com.founder.e5.db;

/**
 * 该接口代表一个用sql表达式表示的值。<br>
 * <br>
 * 实现了该接口的对象与DBSession联合使用，用于在executeQuery、executeUpdate时作为参数值传入，影响实际执行的sql语句。<br>
 * 例子：<br>
 * SqlValue param1 = ... // 其toSqlString()返回“sysdate”<br>
 * String sql = "insert into table1 (date) values (?)";<br>
 * dbSession.executeUpdate( sql, new Object[] {param1} );<br>
 * <br>
 * 则实际执行的sql语句为：insert into table1 (date) values (sysdate)
 * 
 * @author liyanhui
 * @version 1.0
 * @created 2006-8-29 17:33:13
 */
public interface SqlValue {

	/**
	 * 返回sql表达式
	 * 
	 * @return
	 */
	public String toSqlString();

}
