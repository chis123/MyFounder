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
 * ���ౣ��Java���ͺ�JDBC����֮���ӳ���ϵ
 * 
 * @author liyanhui
 * @version 1.0
 * @created 2006-4-5 15:18:21
 */
public final class TypeMapping {

	/**
	 * Ĭ�ϵ�Java���͵�JDBC���͵�ӳ���ϵ
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
	 * �û������Java���͵�JDBC���͵�ӳ���ϵ�����ȼ�����Ĭ��ֵ
	 */
	private static Map userDefinedMapping = new HashMap();

	/**
	 * �����û��Զ����Java���͵�JDBC���͵�ӳ���ϵ
	 * 
	 * @param userDefinedMapping
	 */
	public static void setUserDefinedMapping( Map userDefinedMapping ) {
		TypeMapping.userDefinedMapping = userDefinedMapping;
	}

	/**
	 * ����û��Զ����Java���͵�JDBC���͵�ӳ���ϵ
	 */
	public static void clearUserDefinedMapping() {
		userDefinedMapping.clear();
	}

	/**
	 * ��ȡ����Java����ֵ����ӳ�䵽��JDBC����
	 * 
	 * @param obj Java����
	 * @return JDBC�����룬��java.sql.Types����
	 */
	public static int map( Object obj ) {
		return map( obj.getClass() );
	}

	/**
	 * ��ȡ����Java����ӳ�䵽��JDBC����
	 * 
	 * @param javaType Java����
	 * @return JDBC�����룬��java.sql.Types����
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
