/**
 * $Id: e5new com.founder.e5.commons ArrayUtils.java created on 2006-3-28
 * 14:37:04 by liyanhui
 */
package com.founder.e5.commons;

import gnu.trove.TIntArrayList;

import java.util.ArrayList;

/**
 * ����������߷���
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
	 * ȡ��������Ľ���
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
	 * ȡ��������Ľ���
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
	 * ȡ��������Ľ���
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
	 * ����1��ȥ����2<br>
	 * <br>
	 * ���Ԫ����������ڼ���1�У����Ҳ��ڼ���2��
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
	 * ����1��ȥ����2<br>
	 * <br>
	 * ���Ԫ����������ڼ���1�У����Ҳ��ڼ���2��
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
	 * ����1��ȥ����2<br>
	 * <br>
	 * ���Ԫ����������ڼ���1�У����Ҳ��ڼ���2��
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
	 * �ж�һ���������Ƿ���ĳ����ֵ��
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
