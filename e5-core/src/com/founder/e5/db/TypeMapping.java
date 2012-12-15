/**
 * $Id: e5new com.founder.e5.db Java2DBTypeMapping.java 
 * created on 2006-4-5 15:18:21
 * by liyanhui
 */
package com.founder.e5.db;

import java.sql.Blob;
import java.sql.Clob;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

/**
 * 该类保存Java类型和JDBC类型之间的映射关系
 * 
 * @author liyanhui
 * @version 1.0
 * @created 2006-4-5 15:18:21
 */
public final class TypeMapping {

	/**
	 * 默认的Java类型到JDBC类型的映射关系
	 */
	private static final Map defaultMapping = new HashMap();

	static {
		defaultMapping.put( Integer.TYPE, new Integer( Types.INTEGER ) );
		defaultMapping.put( Integer.class, new Integer( Types.INTEGER ) );
		defaultMapping.put( Long.TYPE, new Integer( Types.BIGINT ) );
		defaultMapping.put( Long.class, new Integer( Types.BIGINT ) );
		defaultMapping.put( Float.TYPE, new Integer( Types.FLOAT ) );
		defaultMapping.put( Float.class, new Integer( Types.FLOAT ) );
		defaultMapping.put( Double.TYPE, new Integer( Types.DOUBLE ) );
		defaultMapping.put( Double.class, new Integer( Types.DOUBLE ) );
		defaultMapping.put( String.class, new Integer( Types.VARCHAR ) );
		defaultMapping.put( Time.class, new Integer( Types.TIME ) );
		defaultMapping.put( Timestamp.class, new Integer( Types.TIMESTAMP ) );
		defaultMapping.put( java.sql.Date.class, new Integer( Types.DATE ) );
		defaultMapping.put( java.util.Date.class, new Integer( Types.DATE ) );
		defaultMapping.put( Clob.class, new Integer( Types.CLOB ) );
		defaultMapping.put( Blob.class, new Integer( Types.BLOB ) );
	}

	/**
	 * 用户定义的Java类型到JDBC类型的映射关系，优先级高于默认值
	 */
	private static Map userDefinedMapping = new HashMap();

	/**
	 * 设置用户自定义的Java类型到JDBC类型的映射关系
	 * 
	 * @param userDefinedMapping
	 */
	public static void setUserDefinedMapping( Map userDefinedMapping ) {
		TypeMapping.userDefinedMapping = userDefinedMapping;
	}

	/**
	 * 清除用户自定义的Java类型到JDBC类型的映射关系
	 */
	public static void clearUserDefinedMapping() {
		userDefinedMapping.clear();
	}

	/**
	 * 获取给定Java对象值类型映射到的JDBC类型
	 * 
	 * @param obj Java对象
	 * @return JDBC类型码，由java.sql.Types定义
	 */
	public static int map( Object obj ) {
		return map( obj.getClass() );
	}

	/**
	 * 获取给定Java类型映射到的JDBC类型
	 * 
	 * @param javaType Java类型
	 * @return JDBC类型码，由java.sql.Types定义
	 */
	public static int map( Class javaType ) {
		Integer type = ( Integer ) userDefinedMapping.get( javaType );
		if ( type == null )
			type = ( Integer ) defaultMapping.get( javaType );

		if ( type != null )
			return type.intValue();
		else
			return Types.OTHER;
	}

}
