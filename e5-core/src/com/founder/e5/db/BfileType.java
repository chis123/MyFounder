package com.founder.e5.db;

/**
 * 该类用于保存bfile类型常量
 * 
 * @author liyanhui
 * @version 1.0
 * @created 2006-4-24 12:59:07
 */
public final class BfileType {

	/**
	 * 外部文件类型（通过字符串方式存在数据库，通过存储设备管理存取）
	 */
	public static final int EXTFILE = 0;

	/**
	 * oracle bfile类型（通过数据库读取，通过存储设备写入）
	 */
	public static final int ORACLE = 1;

}
