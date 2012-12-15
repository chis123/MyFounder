/**
 * $Id: e5new com.founder.e5.commons ProbeConfig.java 
 * created on 2006-3-22 10:35:13
 * by liyanhui
 */
package com.founder.e5.commons;

/**
 * �����������һ��"̽���"�����ã������Ƿ����Ƿ���Ҫ��ջ��Ϣ��
 * 
 * @author liyanhui
 * @version 1.0
 * @created 2006-3-22 10:35:13
 */
public class ProbeConfig {

	private boolean enabled;

	private boolean needStackInfo;

	/**
	 * ����һ�����ĳ��"̽���"��������Ϣ����
	 * 
	 * @param enabled �Ƿ��
	 */
	public ProbeConfig( boolean enabled ) {
		this.enabled = enabled;
	}

	/**
	 * ����һ�����ĳ��"̽���"��������Ϣ����
	 * 
	 * @param enabled �Ƿ��
	 * @param needStackInfo �Ƿ���Ҫ��ջ��Ϣ
	 */
	public ProbeConfig( boolean enabled, boolean needStackInfo ) {
		this.enabled = enabled;
		this.needStackInfo = needStackInfo;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled( boolean enabled ) {
		this.enabled = enabled;
	}

	public boolean isNeedStackInfo() {
		return needStackInfo;
	}

	public void setNeedStackInfo( boolean needStackInfo ) {
		this.needStackInfo = needStackInfo;
	}
	
	public String toString() {
		return new StringBuffer().append( "enabled:" ).append( enabled ).append(
				", needStackInfo:" ).append( needStackInfo ).toString();
	}

}
