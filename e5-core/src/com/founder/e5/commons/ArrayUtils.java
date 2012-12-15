/**
 * $Id: e5new com.founder.e5.commons ArrayUtils.java created on 2006-3-28
 * 14:37:04 by liyanhui
 */
package com.founder.e5.commons;

import gnu.trove.TIntArrayList;

import java.util.ArrayList;

/**
 * 数组操作工具方法
 * 
 * @author liyanhui
 * @version 1.0
 * @created 2006-3-28 14:37:04
 */
public class ArrayUtils {

	public static final int[] EMPTY_INT_ARRAY = {};

	public static final long[] EMPTY_LONG_ARRAY = {};

	public static final Object[] EMPTY_OBJECT_ARRAY = {};

	/**
	 * 取两个数组的交集
	 * 
	 * @param a1
	 * @param a2
	 * @return
	 */
	public static int[] intersect( int[] a1, int[] a2 ) {
		if ( a1.length == 0 || a2.length == 0 )
			return EMPTY_INT_ARRAY;

		TIntArrayList list = new TIntArrayList();
		for ( int i = 0; i < a1.length; i++ ) {
			if ( org.apache.commons.lang.ArrayUtils.contains( a2, a1[i] ) )
				list.add( a1[i] );
		}
		return list.toNativeArray();
	}

	/**
	 * 取两个数组的交集
	 * 
	 * @param a1
	 * @param a2
	 * @return
	 */
	public static String[] intersect( String[] a1, String[] a2 ) {
		ArrayList list = new ArrayList();
		for ( int i = 0; i < a1.length; i++ ) {
			if ( org.apache.commons.lang.ArrayUtils.contains( a2, a1[i] ) )
				list.add( a1[i] );
		}
		return ( String[] ) list.toArray( new String[ list.size() ] );
	}

	/**
	 * 取两个数组的交集
	 * 
	 * @param a1
	 * @param a2
	 * @return
	 */
	public static Object[] intersect( Object[] a1, Object[] a2 ) {
		ArrayList list = new ArrayList();
		for ( int i = 0; i < a1.length; i++ ) {
			if ( org.apache.commons.lang.ArrayUtils.contains( a2, a1[i] ) )
				list.add( a1[i] );
		}
		return list.toArray();
	}

	/**
	 * 集合1减去集合2<br>
	 * <br>
	 * 结果元素满足规则：在集合1中，而且不在集合2中
	 * 
	 * @param a1
	 * @param a2
	 * @return
	 */
	public static int[] minus( int[] a1, int[] a2 ) {
		TIntArrayList list = new TIntArrayList();
		for ( int i = 0; i < a1.length; i++ ) {
			if ( !org.apache.commons.lang.ArrayUtils.contains( a2, a1[i] ) )
				list.add( a1[i] );
		}
		return list.toNativeArray();
	}

	/**
	 * 集合1减去集合2<br>
	 * <br>
	 * 结果元素满足规则：在集合1中，而且不在集合2中
	 * 
	 * @param a1
	 * @param a2
	 * @return
	 */
	public static String[] minus( String[] a1, String[] a2 ) {
		ArrayList list = new ArrayList();
		for ( int i = 0; i < a1.length; i++ ) {
			if ( !org.apache.commons.lang.ArrayUtils.contains( a2, a1[i] ) )
				list.add( a1[i] );
		}
		return ( String[] ) list.toArray( new String[ list.size() ] );
	}

	/**
	 * 集合1减去集合2<br>
	 * <br>
	 * 结果元素满足规则：在集合1中，而且不在集合2中
	 * 
	 * @param a1
	 * @param a2
	 * @return
	 */
	public static Object[] minus( Object[] a1, Object[] a2 ) {
		ArrayList list = new ArrayList();
		for ( int i = 0; i < a1.length; i++ ) {
			if ( !org.apache.commons.lang.ArrayUtils.contains( a2, a1[i] ) )
				list.add( a1[i] );
		}
		return list.toArray();
	}

	/**
	 * 判断一个数组中是否有某个数值。
	 * @param arr
	 * @param value
	 * @return
	 */
	public static boolean contains(int[] arr, int value)
	{
		if (arr == null) return false;
		
		for (int i = 0; i < arr.length; i++) {
			if (value == arr[i]) return true;
		}
		return false;
	}
}
