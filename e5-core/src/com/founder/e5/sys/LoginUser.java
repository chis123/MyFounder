package com.founder.e5.sys;

import java.io.Serializable;
/**
 * Lu Zuowei modified 2006/4/4
 * 增加一个时间字段，记录最后访问时间
 * @version 1.0
 * @created 11-七月-2005 16:37:14
 */
public class LoginUser  implements Serializable
{
	private static final long serialVersionUID = 8911735575090727722L;
	private int hashCode = Integer.MIN_VALUE;
	
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

	public LoginUser()
	{
	}

    /**
     * @return 返回 appID。
     */
    public int getAppID()
    {
        return appID;
    }
    /**
     * @return 返回 hostName。
     */
    public String getHostName()
    {
        return hostName;
    }
    /**
     * @return 返回 loginTime。
     */
    public String getLoginTime()
    {
        return loginTime;
    }
    /**
     * @return 返回 portalID。
     */
    public int getPortalID()
    {
        return portalID;
    }
    /**
     * @return 返回 roleID。
     */
    public int getRoleID()
    {
        return roleID;
    }
    /**
     * @return 返回 userCode。
     */
    public String getUserCode()
    {
        return userCode;
    }
    /**
     * @return 返回 userID。
     */
    public int getUserID()
    {
        return userID;
    }
    /**
     * @param appID 要设置的 appID。
     */
    public void setAppID(int appID)
    {
        this.appID = appID;
    }
    /**
     * @param hostName 要设置的 hostName。
     */
    public void setHostName(String hostName)
    {
        this.hostName = hostName;
    }
    /**
     * @param loginTime 要设置的 loginTime。
     */
    public void setLoginTime(String loginTime)
    {
        this.loginTime = loginTime;
    }
    /**
     * @param portalID 要设置的 portalID。
     */
    public void setPortalID(int portalID)
    {
        this.portalID = portalID;
    }
    /**
     * @param roleID 要设置的 roleID。
     */
    public void setRoleID(int roleID)
    {
        this.roleID = roleID;
    }
    /**
     * @param userCode 要设置的 userCode。
     */
    public void setUserCode(String userCode)
    {
        this.userCode = userCode;
    }
    /**
     * @param userID 要设置的 userID。
     */
    public void setUserID(int userID)
    {
        this.userID = userID;
    }
    
    public boolean equals (Object obj) 
    {
        if(obj == null) return false;
        if(! (obj instanceof LoginUser))
            return false;
        else
        {
            LoginUser loginUser = (LoginUser)obj;
            if(loginUser.getRoleID() == this.roleID && loginUser.getUserID() == this.userID)
                return true;
        }
        return false;
    }
    
    public int hashCode () 
    {
		if (Integer.MIN_VALUE == this.hashCode) 
		{
			StringBuffer sb = new StringBuffer(100);
			sb.append(roleID).append(':').append(userID);
			this.hashCode = sb.toString().hashCode();
		}
		return this.hashCode;
    }
    
    public String toString () 
    {
		return super.toString();
	}

	public String getLastAccessTime() 
	{
		return lastAccessTime;
	}

	public void setLastAccessTime(String lastAccessTime) 
	{
		this.lastAccessTime = lastAccessTime;
	}

	public int getLoginID() {
		return loginID;
	}

	public void setLoginID(int id) {
		this.loginID = id;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
}