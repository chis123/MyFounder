/**
 * $Id: e5project_vss com.founder.e5.sys SysLogRecorder.java 
 * created on 2006-2-8 16:59:49
 * by liyanhui
 */
package com.founder.e5.sys;

import com.founder.e5.context.BaseDAO;
import com.founder.e5.context.EUID;

/**
 * 系统记录功能的使用接口
 * 
 * @author liyanhui
 * @version 1.0
 * @created 2006-2-8 16:59:49
 */
public class SysLogRecorder {

	/**
	 * 记录"记录"类型事件<br>
	 * 该方法设置logType="记录"后插入数据库记录
	 * 
	 * @param event 事件数据对象
	 * 该方法设置logType="记录"后插入数据库记录
	 */
	public static void record( SysLog event ) {
		event.setLogType( "记录" );
		log( event );
	}

	/**
	 * 记录"操作"类型事件
	 * 该方法设置logType="操作"后插入数据库记录
	 * 
	 * @param event 事件数据对象
	 */
	public static void operate( SysLog event ) {
		event.setLogType( "操作" );
		log( event );
	}

	/**
	 * 记录"信息"类型事件
	 * 该方法设置logType="信息"后插入数据库记录
	 * 
	 * @param event 事件数据对象
	 */
	public static void info( SysLog event ) {
		event.setLogType( "信息" );
		log( event );
	}

	/**
	 * 记录"错误"类型事件
	 * 该方法设置logType="错误"后插入数据库记录
	 * 
	 * @param event 事件数据对象
	 */
	public static void error( SysLog event ) {
		event.setLogType( "错误" );
		log( event );
	}

	/**
	 * 记录系统记录事件的底层方法，该类其余的方法设置相应的logType属性后调用此方法完成<br>
	 * 插入数据库的动作。<br>
	 * 提供此方法的用意是让用户可以扩展日志类型（logType）
	 * 
	 * @param event
	 */
	public static void log( SysLog event ) {
		try {
			int id = (int)EUID.getID("SysLogID");
			event.setLogID(id);
			
			BaseDAO dao = new BaseDAO();
			dao.save( event );
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
