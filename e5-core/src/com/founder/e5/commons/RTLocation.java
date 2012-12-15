package com.founder.e5.commons;

/**
 * �������ͨ��ָ����ǰ�߳����͵�ǰ����λ�ö���������ʱ��λ��Ϣ��RTLocation����Ψһ��ʶ
 * �˳�������ʱ��һ��ִ�е㣻�������ʱִ�е��ڶ�̬�����б���Ϊ"̽���"��probe�����û�ͨ
 * ��������ʱ����̽���Ĵ򿪺͹رգ������Ƴ�����Ԥ����Ϣ�����������ν�Ķ�̬���١�
 * 
 * @author li yanhui
 * @version 1.0
 * @created 10-����-2006 14:36:43
 */
public class RTLocation {

	/**
	 * ��ǰ�߳���
	 */
	private final String threadName;

	/**
	 * ����λ����Ϣ����"com.founder.e5.dom.DocLibManagerImpl.create"
	 */
	private final String codeLocation;

	private final String id;

	/**
	 * ����һ��"̽���"����������Ϣ������̽��������ʱλ����Ϣ
	 * 
	 * @param threadName �߳���
	 * @param codeLocation ����λ��
	 */
	public RTLocation( String threadName, String codeLocation ) {
		if ( threadName == null || codeLocation == null )
			throw new NullPointerException();

		this.threadName = threadName;
		this.codeLocation = codeLocation;
		this.id = "[" + this.threadName + "]" + this.codeLocation;
	}

	public String getThreadName() {
		return threadName;
	}

	public String getCodeLocation() {
		return codeLocation;
	}

	/**
	 * ��������������ģʽ�Ƿ���������ľ���RTLocation<br>
	 * ע�⣺���ø÷�����ζ�Ű�������߳���������λ��ֵ����������ʽ��������һ��̽���
	 * �ļ��ϣ��������RTLocation������������̽���
	 * 
	 * @param rtl ������һ������λ��
	 * @return
	 */
	public boolean contain( RTLocation rtl ) {
		if ( equals( rtl ) )
			return true;

		return ( rtl.threadName.matches( threadName ) && rtl.codeLocation.matches( codeLocation ) );
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals( Object obj ) {
		if ( obj instanceof RTLocation ) {
			RTLocation rtl = ( RTLocation ) obj;
			return id.equals( rtl.id );
		}
		return false;
	}

	public int hashCode() {
		return id.hashCode();
	}

	public String toString() {
		return id;
	}

}
