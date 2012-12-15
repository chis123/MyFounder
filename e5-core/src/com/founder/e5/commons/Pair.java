/**
 * $Id: e5new com.founder.e5.commons Pair.java 
 * created on 2006-4-14 16:41:52
 * by liyanhui
 */
package com.founder.e5.commons;

/**
 * 值对对象，用于表示一个{key-value}对
 * 
 * @author liyanhui
 * @version 1.0
 * @created 2006-4-14 16:41:52
 */
public class Pair {

	private String key;

	private Object value;

	/**
	 * 
	 */
	public Pair() {
	}

	/**
	 * @param key
	 * @param value
	 */
	public Pair( String key, Object value ) {
		this.key = key;
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public void setKey( String key ) {
		this.key = key;
	}

	public Object getValue() {
		return value;
	}

	/**
	 * 返回value的toString值
	 */
	public String getStringValue() {
		if ( value != null )
			return value.toString();
		return null;
	}

	public void setValue( Object value ) {
		this.value = value;
	}

	/**
	 * key相等而且value相等时返回true
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals( Object obj ) {
		if ( obj instanceof Pair ) {
			Pair p2 = ( Pair ) obj;
			return _eq( key, p2.key ) && _eq( value, p2.value );
		}
		return false;
	}

	private static boolean _eq( Object o1, Object o2 ) {
		return o1 == o2 || ( o1 != null && o2 != null && o1.equals( o2 ) );
	}

	public int hashCode() {
		return ( key == null ? 0 : key.hashCode() )
				^ ( value == null ? 0 : value.hashCode() );
	}

	public String toString() {
		return new StringBuffer().append( key ).append( "=" ).append( value ).toString();
	}

}
