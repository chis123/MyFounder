package com.founder.e5.sys;

/**
 * 登录用户的归档记录<br>
 * 与在线用户实时记录表相比，增加了一个用户是否正常退出的标记，用户自己退出为0，如果管理员踢出完成则是1
 * 
 * @author LuZuowei
 */
public class LoginArchive {
	/**
	 * 流水ID号
	 */
	private int loginID;

	/**
	 * 用户ID
	 */
	private int userID;

	/**
	 * 角色ID
	 */
	private int roleID;

	/**
	 * 用户登录名
	 */
	private String userCode;

	/**
	 * 用户登录的客户端机器的名称
	 */
	private String hostName;

	/**
	 * 用户登录服务器的名称
	 */
	private String serverName;

	/**
	 * 用户登录时间
	 */
	private String loginTime;

	/**
	 * 用户登录的子系统ID
	 */
	private int appID;

	/**
	 * 门户ID
	 */
	private int portalID;

	/**
	 * 最后访问时间
	 */
	private String lastAccessTime;

	/**
	 * 用户是否正常退出的标记，0表示用户自己退出，1表示管理员踢出
	 */
	private int normalExit;

	public LoginArchive() {
		super();
	}

	/**
	 * @return 用户是否正常退出的标记，0表示用户自己退出，1表示管理员踢出
	 */
	public int getNormalExit() {
		return normalExit;
	}

	/**
	 * @param normalExit 用户是否正常退出的标记，0表示用户自己退出，1表示管理员踢出
	 */
	public void setNormalExit( int normalExit ) {
		this.normalExit = normalExit;
	}

	/**
	 * @return 用户登录的子系统ID
	 */
	public int getAppID() {
		return appID;
	}

	/**
	 * @param appID 用户登录的子系统ID
	 */
	public void setAppID( int appID ) {
		this.appID = appID;
	}

	/**
	 * @return 用户登录的客户端机器的名称
	 */
	public String getHostName() {
		return hostName;
	}

	/**
	 * @param hostName 用户登录的客户端机器的名称
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
