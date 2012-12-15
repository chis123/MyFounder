package com.founder.e5.sys.org;

/**
 * @version 1.0
 * @updated 11-七月-2005 10:17:31
 */
public class User {

	/**
	 * 用户ID
	 */
	private int userID;
	/**
	 * 用户编码
	 */
	private String userCode;
	/**
	 * 用户所在组织的ID
	 */
	private int orgID;
	/**
	 * 用户口令
	 */
	private String userPassword;
	/**
	 * 用户名
	 */
	private String userName;
	/**
	 * 电邮地址
	 */
	private String emailAddress;
	/**
	 * 通信地址
	 */
	private String address;
	/**
	 * 邮政编码
	 */
	private String postCode;
	/**
	 * bp机号
	 */
	private String bPNumber;
	/**
	 * 住所电话
	 */
	private String telHomeNumber;
	/**
	 * 办公电话
	 */
	private String telOffNumber;
	/**
	 * 手机号码
	 */
	private String handSetNumber;
	/**
	 * 排序ID
	 */
	private int orderID;

	public User(){

	}

	

    /**
     * @return 返回 address。
     */
    public String getAddress()
    {
        return address;
    }
    /**
     * @return 返回 bPNumber。
     */
    public String getBPNumber()
    {
        return bPNumber;
    }
    /**
     * @return 返回 emailAddress。
     */
    public String getEmailAddress()
    {
        return emailAddress;
    }
    /**
     * @return 返回 handSetNumber。
     */
    public String getHandSetNumber()
    {
        return handSetNumber;
    }
    /**
     * @return 返回 orderID。
     */
    public int getOrderID()
    {
        return orderID;
    }
    /**
     * @return 返回 postCode。
     */
    public String getPostCode()
    {
        return postCode;
    }
    /**
     * @return 返回 telHomeNumber。
     */
    public String getTelHomeNumber()
    {
        return telHomeNumber;
    }
    /**
     * @return 返回 telOffNumber。
     */
    public String getTelOffNumber()
    {
        return telOffNumber;
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
     * @return 返回 userName。
     */
    public String getUserName()
    {
        return userName;
    }
    /**
     * @return 返回 userPassword。
     */
    public String getUserPassword()
    {
        return userPassword;
    }
    /**
     * @param address 要设置的 address。
     */
    public void setAddress(String address)
    {
        this.address = address;
    }
    /**
     * @param number 要设置的 bPNumber。
     */
    public void setBPNumber(String number)
    {
        this.bPNumber = number;
    }
    /**
     * @param emailAddress 要设置的 emailAddress。
     */
    public void setEmailAddress(String emailAddress)
    {
        this.emailAddress = emailAddress;
    }
    /**
     * @param handSetNumber 要设置的 handSetNumber。
     */
    public void setHandSetNumber(String handSetNumber)
    {
        this.handSetNumber = handSetNumber;
    }
    /**
     * @param orderID 要设置的 orderID。
     */
    public void setOrderID(int orderID)
    {
        this.orderID = orderID;
    }
    /**
     * @param postCode 要设置的 postCode。
     */
    public void setPostCode(String postCode)
    {
        this.postCode = postCode;
    }
    /**
     * @param telHomeNumber 要设置的 telHomeNumber。
     */
    public void setTelHomeNumber(String telHomeNumber)
    {
        this.telHomeNumber = telHomeNumber;
    }
    /**
     * @param telOffNumber 要设置的 telOffNumber。
     */
    public void setTelOffNumber(String telOffNumber)
    {
        this.telOffNumber = telOffNumber;
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
    /**
     * @param userName 要设置的 userName。
     */
    public void setUserName(String userName)
    {
        this.userName = userName;
    }
    /**
     * @param userPassword 要设置的 userPassword。
     */
    public void setUserPassword(String userPassword)
    {
        this.userPassword = userPassword;
    }
    /**
     * @return 返回 orgID。
     */
    public int getOrgID()
    {
        return orgID;
    }
    /**
     * @param orgID 要设置的 orgID。
     */
    public void setOrgID(int orgID)
    {
        this.orgID = orgID;
    }
}