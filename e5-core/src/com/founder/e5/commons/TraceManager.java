/**
 * $Id: e5project_vss com.founder.e5.commons TraceManager.java 
 * created on 2006-1-22 11:39:07
 * by liyanhui
 */
package com.founder.e5.commons;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 跟踪控制信息管理器，负责载入并管理跟踪开关配置信息
 * 
 * @author liyanhui
 * @version 1.0
 * @created 2006-1-22 11:39:07
 */
public class TraceManager {

	/**
	 * key（RTLocation对象） - value（ProbeConfig对象）<br>
	 * 注意：每个key（RTLocation对象）代表了一类探测点，其中RTLocation的线程名和代码位 置属性值被看做正则表达式
	 */
	private static Map enabledProbes = Collections.synchronizedMap( new HashMap() );

	/**
	 * 所有probe的默认配置
	 */
	private static final ProbeConfig default_cfg = new ProbeConfig(
			false,
			false );

	/**
	 * 查询针对单个探测点的配置信息
	 * 
	 * @param rtl 单个探测点
	 * @return
	 */
	public static ProbeConfig queryCfg( RTLocation rtl ) {
		for ( Iterator i = enabledProbes.entrySet().iterator(); i.hasNext(); ) {
			Map.Entry me = ( Map.Entry ) i.next();
			RTLocation key = ( RTLocation ) me.getKey();
			ProbeConfig value = ( ProbeConfig ) me.getValue();

			if ( key.contain( rtl ) )
				return value;
		}

		return default_cfg;
	}

	/**
	 * 检查给定探测点的开关是否打开
	 * 
	 * @param rtl 单个探测点
	 * @return
	 */
	public static boolean isEnabled( RTLocation rtl ) {
		return queryCfg( rtl ).isEnabled();
	}

	/**
	 * 添加一类探测点到已打开探测点列表中
	 * 
	 * @param rtl 代表了一类探测点，其中RTLocation的线程名和代码位置属性值被看做正则表达式
	 */
	public static void addEnabled( RTLocation rtl ) {
		enabledProbes.put( rtl, new ProbeConfig( true ) );
	}

	/**
	 * 添加一类探测点到已打开探测点列表中
	 * 
	 * @param rtl 代表了一类探测点，其中RTLocation的线程名和代码位置属性值被看做正则表达式
	 * @param needStackInfo 是否需要堆栈信息
	 */
	public static void addEnabled( RTLocation rtl, boolean needStackInfo ) {
		enabledProbes.put( rtl, new ProbeConfig( true, needStackInfo ) );
	}

	/**
	 * 添加一类探测点到已打开探测点列表中<br>
	 * 注意：默认不需要堆栈信息
	 * 
	 * @param threadName 线程名模式
	 * @param codeLocation 代码位置模式
	 */
	public static void addEnabled( String threadName, String codeLocation ) {
		addEnabled( new RTLocation( threadName, codeLocation ) );
	}

	/**
	 * 添加一类探测点到已打开探测点列表中，并指定是否需要堆栈信息
	 * 
	 * @param threadName 线程名模式
	 * @param codeLocation 代码位置模式
	 * @param needStackInfo
	 */
	public static void addEnabled( String threadName, String codeLocation,
			boolean needStackInfo ) {
		addEnabled( new RTLocation( threadName, codeLocation ), needStackInfo );
	}

	/**
	 * 从已打开探测点列表中删除给定对象（若存在的话）
	 * 
	 * @param rtl （可能）存在于已打开列表中的对象
	 */
	public static void removeEnabled( RTLocation rtl ) {
		enabledProbes.remove( rtl );
	}

	/**
	 * 从已打开探测点列表中删除给定对象（若存在的话）
	 * 
	 * @param threadName
	 * @param codeLocation
	 */
	public static void removeEnabled( String threadName, String codeLocation ) {
		removeEnabled( new RTLocation( threadName, codeLocation ) );
	}

	/**
	 * 返回当前所有打开的"探测点"集合
	 * 
	 * @return [RTLocation - ProbeConfig]映射的集合
	 */
	public static Map enabledProbes() {
		return Collections.unmodifiableMap( enabledProbes );
	}

	/**
	 * 清除所有打开的开关
	 */
	public static void clearEnabled() {
		enabledProbes.clear();
	}

}
