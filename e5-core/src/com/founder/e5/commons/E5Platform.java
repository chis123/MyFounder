package com.founder.e5.commons;

/**
 * 保存平台级常量
 * @created 2005-6-29 14:17:17
 * @author liyanhui
 * @version 1.0
 * @deprecated replaced by com.founder.e5.db.DataType
 */
public class E5Platform {

	public static final String COLUMN_PK = "SYS_DOCUMENTID";
	public static final String COLUMN_DELETEFLAG = "SYS_DELETEFLAG";
	public static final String COLUMN_DOCLIBID = "SYS_DOCLIBID";
	public static final String COLUMN_SYSCREATED = "SYS_CREATED";
	public static final String COLUMN_LASTMODIFIED = "SYS_LASTMODIFIED";
	
	public static final String INTEGER = "INTEGER";
	public static final String LONG = "LONG";
	public static final String CHAR = "CHAR";
	public static final String VARCHAR = "VARCHAR";
	public static final String DOUBLE = "DOUBLE";
	public static final String FLOAT = "FLOAT";
	public static final String DATE = "DATE";
	public static final String TIME = "TIME";
	public static final String TIMESTAMP = "TIMESTAMP";
	public static final String CLOB = "CLOB";
	public static final String BLOB = "BLOB";
	public static final String BFILE = "BFILE";
	public static final String EXTFILE = "EXTFILE";
	/**
	 * 代表文档库ID类型（用于得到ID生成器）
	 */
	public static final int IDTYPE_DOCLIB = 0;
	/**
	 * 代表文档ID类型（用于得到ID生成器）
	 */
	public static final int IDTYPE_DOCUMENT = 1;
	/**
	 * 代表流程记录ID类型（用于得到ID生成器）
	 */
	public static final int IDTYPE_FLOWRECORD = 2;

}