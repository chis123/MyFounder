package com.founder.e5.sys.org;

/**
 * @version 1.0
 * @updated 11-����-2005 10:17:31
 */
public class User {

	/**
	 * �û�ID
	 */
	private int userID;
	/**
	 * �û�����
	 */
	private String userCode;
	/**
	 * �û�������֯��ID
	 */
	private int orgID;
	/**
	 * �û�����
	 */
	private String userPassword;
	/**
	 * �û���
	 */
	private String userName;
	/**
	 * ���ʵ�ַ
	 */
	private String emailAddress;
	/**
	 * ͨ�ŵ�ַ
	 */
	private String address;
	/**
	 * ��������
	 */
	private String postCode;
	/**
	 * bp����
	 */
	private String bPNumber;
	/**
	 * ס���绰
	 */
	private String telHomeNumber;
	/**
	 * �칫�绰
	 */
	private String telOffNumber;
	/**
	 * �ֻ�����
	 */
	private String handSetNumber;
	/**
	 * ����ID
	 */
	private int orderID;

	public User(){

	}

	

    /**
     * @return ���� address��
     */
    public String getAddress()
    {
        return address;
    }
    /**
     * @return ���� bPNumber��
     */
    public String getBPNumber()
    {
        return bPNumber;
    }
    /**
     * @return ���� emailAddress��
     */
    public String getEmailAddress()
    {
        return emailAddress;
    }
    /**
     * @return ���� handSetNumber��
     */
    public String getHandSetNumber()
    {
        return handSetNumber;
    }
    /**
     * @return ���� orderID��
     */
    public int getOrderID()
    {
        return orderID;
    }
    /**
     * @return ���� postCode��
     */
    public String getPostCode()
    {
        return postCode;
    }
    /**
     * @return ���� telHomeNumber��
     */
    public String getTelHomeNumber()
    {
        return telHomeNumber;
    }
    /**
     * @return ���� telOffNumber��
     */
    public String getTelOffNumber()
    {
        return telOffNumber;
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
     * @return ���� userName��
     */
    public String getUserName()
    {
        return userName;
    }
    /**
     * @return ���� userPassword��
     */
    public String getUserPassword()
    {
        return userPassword;
    }
    /**
     * @param address Ҫ���õ� address��
     */
    public void setAddress(String address)
    {
        this.address = address;
    }
    /**
     * @param number Ҫ���õ� bPNumber��
     */
    public void setBPNumber(String number)
    {
        this.bPNumber = number;
    }
    /**
     * @param emailAddress Ҫ���õ� emailAddress��
     */
    public void setEmailAddress(String emailAddress)
    {
        this.emailAddress = emailAddress;
    }
    /**
     * @param handSetNumber Ҫ���õ� handSetNumber��
     */
    public void setHandSetNumber(String handSetNumber)
    {
        this.handSetNumber = handSetNumber;
    }
    /**
     * @param orderID Ҫ���õ� orderID��
     */
    public void setOrderID(int orderID)
    {
        this.orderID = orderID;
    }
    /**
     * @param postCode Ҫ���õ� postCode��
     */
    public void setPostCode(String postCode)
    {
        this.postCode = postCode;
    }
    /**
     * @param telHomeNumber Ҫ���õ� telHomeNumber��
     */
    public void setTelHomeNumber(String telHomeNumber)
    {
        this.telHomeNumber = telHomeNumber;
    }
    /**
     * @param telOffNumber Ҫ���õ� telOffNumber��
     */
    public void setTelOffNumber(String telOffNumber)
    {
        this.telOffNumber = telOffNumber;
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
    /**
     * @param userName Ҫ���õ� userName��
     */
    public void setUserName(String userName)
    {
        this.userName = userName;
    }
    /**
     * @param userPassword Ҫ���õ� userPassword��
     */
    public void setUserPassword(String userPassword)
    {
        this.userPassword = userPassword;
    }
    /**
     * @return ���� orgID��
     */
    public int getOrgID()
    {
        return orgID;
    }
    /**
     * @param orgID Ҫ���õ� orgID��
     */
    public void setOrgID(int orgID)
    {
        this.orgID = orgID;
    }
}