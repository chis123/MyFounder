package com.founder.e5.sys;
/**
 * 在线用户的归档记录表
 * 与在线用户实时记录表相比，增加了一个用户是否正常退出的标记
 * 用户自己退出为0，如果管理员踢出完成则是1
 * @author LuZuowei
 */
public class LoginUserArchive 
{
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

	private int normalExit;
	public LoginUserArchive() 
	{
		super();
	}
	public int getNormalExit() 
	{
		return normalExit;
	}
	public void setNormalExit(int normalExit) 
	{
		this.normalExit = normalExit;
	}
	public int getAppID() {
		return appID;
	}
	public void setAppID(int appID) {
		this.appID = appID;
	}
	public String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	public String getLastAccessTime() {
		return lastAccessTime;
	}
	public void setLastAccessTime(String lastAccessTime) {
		this.lastAccessTime = lastAccessTime;
	}
	public int getLoginID() {
		return loginID;
	}
	public void setLoginID(int loginID) {
		this.loginID = loginID;
	}
	public String getLoginTime() {
		return loginTime;
	}
	public void setLoginTime(String loginTime) {
		this.loginTime = loginTime;
	}
	public int getPortalID() {
		return portalID;
	}
	public void setPortalID(int portalID) {
		this.portalID = portalID;
	}
	public int getRoleID() {
		return roleID;
	}
	public void setRoleID(int roleID) {
		this.roleID = roleID;
	}
	public String getServerName() {
		return serverName;
	}
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	public String getUserCode() {
		return userCode;
	}
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	public int getUserID() {
		return userID;
	}
	public void setUserID(int userID) {
		this.userID = userID;
	}
}
