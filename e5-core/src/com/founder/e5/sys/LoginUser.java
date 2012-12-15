package com.founder.e5.sys;

import java.io.Serializable;
/**
 * Lu Zuowei modified 2006/4/4
 * ����һ��ʱ���ֶΣ���¼������ʱ��
 * @version 1.0
 * @created 11-����-2005 16:37:14
 */
public class LoginUser  implements Serializable
{
	private static final long serialVersionUID = 8911735575090727722L;
	private int hashCode = Integer.MIN_VALUE;
	
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

	public LoginUser()
	{
	}

    /**
     * @return ���� appID��
     */
    public int getAppID()
    {
        return appID;
    }
    /**
     * @return ���� hostName��
     */
    public String getHostName()
    {
        return hostName;
    }
    /**
     * @return ���� loginTime��
     */
    public String getLoginTime()
    {
        return loginTime;
    }
    /**
     * @return ���� portalID��
     */
    public int getPortalID()
    {
        return portalID;
    }
    /**
     * @return ���� roleID��
     */
    public int getRoleID()
    {
        return roleID;
    }
    /**
     * @return ���� userCode��
     */
    public String getUserCode()
    {
        return userCode;
    }
    /**
     * @return ���� userID��
     */
    public int getUserID()
    {
        return userID;
    }
    /**
     * @param appID Ҫ���õ� appID��
     */
    public void setAppID(int appID)
    {
        this.appID = appID;
    }
    /**
     * @param hostName Ҫ���õ� hostName��
     */
    public void setHostName(String hostName)
    {
        this.hostName = hostName;
    }
    /**
     * @param loginTime Ҫ���õ� loginTime��
     */
    public void setLoginTime(String loginTime)
    {
        this.loginTime = loginTime;
    }
    /**
     * @param portalID Ҫ���õ� portalID��
     */
    public void setPortalID(int portalID)
    {
        this.portalID = portalID;
    }
    /**
     * @param roleID Ҫ���õ� roleID��
     */
    public void setRoleID(int roleID)
    {
        this.roleID = roleID;
    }
    /**
     * @param userCode Ҫ���õ� userCode��
     */
    public void setUserCode(String userCode)
    {
        this.userCode = userCode;
    }
    /**
     * @param userID Ҫ���õ� userID��
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