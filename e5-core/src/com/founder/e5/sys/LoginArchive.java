package com.founder.e5.sys;

/**
 * ��¼�û��Ĺ鵵��¼<br>
 * �������û�ʵʱ��¼����ȣ�������һ���û��Ƿ������˳��ı�ǣ��û��Լ��˳�Ϊ0���������Ա�߳��������1
 * 
 * @author LuZuowei
 */
public class LoginArchive {
	/**
	 * ��ˮID��
	 */
	private int loginID;

	/**
	 * �û�ID
	 */
	private int userID;

	/**
	 * ��ɫID
	 */
	private int roleID;

	/**
	 * �û���¼��
	 */
	private String userCode;

	/**
	 * �û���¼�Ŀͻ��˻���������
	 */
	private String hostName;

	/**
	 * �û���¼������������
	 */
	private String serverName;

	/**
	 * �û���¼ʱ��
	 */
	private String loginTime;

	/**
	 * �û���¼����ϵͳID
	 */
	private int appID;

	/**
	 * �Ż�ID
	 */
	private int portalID;

	/**
	 * ������ʱ��
	 */
	private String lastAccessTime;

	/**
	 * �û��Ƿ������˳��ı�ǣ�0��ʾ�û��Լ��˳���1��ʾ����Ա�߳�
	 */
	private int normalExit;

	public LoginArchive() {
		super();
	}

	/**
	 * @return �û��Ƿ������˳��ı�ǣ�0��ʾ�û��Լ��˳���1��ʾ����Ա�߳�
	 */
	public int getNormalExit() {
		return normalExit;
	}

	/**
	 * @param normalExit �û��Ƿ������˳��ı�ǣ�0��ʾ�û��Լ��˳���1��ʾ����Ա�߳�
	 */
	public void setNormalExit( int normalExit ) {
		this.normalExit = normalExit;
	}

	/**
	 * @return �û���¼����ϵͳID
	 */
	public int getAppID() {
		return appID;
	}

	/**
	 * @param appID �û���¼����ϵͳID
	 */
	public void setAppID( int appID ) {
		this.appID = appID;
	}

	/**
	 * @return �û���¼�Ŀͻ��˻���������
	 */
	public String getHostName() {
		return hostName;
	}

	/**
	 * @param hostName �û���¼�Ŀͻ��˻���������
	 */
	public void setHostName( String hostName ) {
		this.hostName = hostName;
	}

	public String getLastAccessTime() {
		return lastAccessTime;
	}

	public void setLastAccessTime( String lastAccessTime ) {
		this.lastAccessTime = lastAccessTime;
	}

	public int getLoginID() {
		return loginID;
	}

	public void setLoginID( int loginID ) {
		this.loginID = loginID;
	}

	public String getLoginTime() {
		return loginTime;
	}

	public void setLoginTime( String loginTime ) {
		this.loginTime = loginTime;
	}

	public int getPortalID() {
		return portalID;
	}

	public void setPortalID( int portalID ) {
		this.portalID = portalID;
	}

	public int getRoleID() {
		return roleID;
	}

	public void setRoleID( int roleID ) {
		this.roleID = roleID;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName( String serverName ) {
		this.serverName = serverName;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode( String userCode ) {
		this.userCode = userCode;
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID( int userID ) {
		this.userID = userID;
	}

	public boolean equals( Object obj ) {
		if ( obj instanceof LoginArchive ) {
			return loginID == ( ( LoginArchive ) obj ).loginID;
		}
		return false;
	}

	public int hashCode() {
		return loginID;
	}

	public String toString() {
		return new StringBuffer().append( "LoginArchive[" ).append( loginID ).append(
				"]" ).toString();
	}
}
