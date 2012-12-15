/**
 * $Id: e5project_vss com.founder.e5.sys SysLogRecorder.java 
 * created on 2006-2-8 16:59:49
 * by liyanhui
 */
package com.founder.e5.sys;

import com.founder.e5.context.BaseDAO;
import com.founder.e5.context.EUID;

/**
 * ϵͳ��¼���ܵ�ʹ�ýӿ�
 * 
 * @author liyanhui
 * @version 1.0
 * @created 2006-2-8 16:59:49
 */
public class SysLogRecorder {

	/**
	 * ��¼"��¼"�����¼�<br>
	 * �÷�������logType="��¼"��������ݿ��¼
	 * 
	 * @param event �¼����ݶ���
	 * �÷�������logType="��¼"��������ݿ��¼
	 */
	public static void record( SysLog event ) {
		event.setLogType( "��¼" );
		log( event );
	}

	/**
	 * ��¼"����"�����¼�
	 * �÷�������logType="����"��������ݿ��¼
	 * 
	 * @param event �¼����ݶ���
	 */
	public static void operate( SysLog event ) {
		event.setLogType( "����" );
		log( event );
	}

	/**
	 * ��¼"��Ϣ"�����¼�
	 * �÷�������logType="��Ϣ"��������ݿ��¼
	 * 
	 * @param event �¼����ݶ���
	 */
	public static void info( SysLog event ) {
		event.setLogType( "��Ϣ" );
		log( event );
	}

	/**
	 * ��¼"����"�����¼�
	 * �÷�������logType="����"��������ݿ��¼
	 * 
	 * @param event �¼����ݶ���
	 */
	public static void error( SysLog event ) {
		event.setLogType( "����" );
		log( event );
	}

	/**
	 * ��¼ϵͳ��¼�¼��ĵײ㷽������������ķ���������Ӧ��logType���Ժ���ô˷������<br>
	 * �������ݿ�Ķ�����<br>
	 * �ṩ�˷��������������û�������չ��־���ͣ�logType��
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
