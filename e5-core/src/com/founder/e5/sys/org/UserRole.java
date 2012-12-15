package com.founder.e5.sys.org;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * @created 04-7-2005 14:59:06
 * @updated 11-7-2005 12:42:44
 * @version 1.0
 */
public class UserRole implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8358844999219049506L;
	private int hashCode = Integer.MIN_VALUE;
	/**
	 * ��ɫID
	 */
	private int roleID;
	/**
	 * ��ɫ����
	 */
	private String roleName;
	/**
	 * �û�ID
	 */
	private int userID;
	/**
	 * �û�����
	 */
	private String userCode;
	/**
	 * ��ɫ��ʼ����
	 */
	private String startDate;
	/**
	 * ��ɫ��������
	 */
	private String endDate;
	/**
	 * ��ɫ��ʼʱ��
	 */
	private String startTime;
	/**
	 * ��ɫ����ʱ��
	 */
	private String endTime;
	/**
	 * ʱ������
	 */
	private int timeType;
	/**
	 * ʱ��ֵ
	 */
	private int timeValue;

	public UserRole(){

	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("userCode=").append(userCode);
		sb.append(",userID=").append(userID);
		sb.append(",roleName=").append(roleName);
		sb.append(",timeType=").append(timeType);
		sb.append(",timeValue=").append(timeValue);
		sb.append(",startDate=").append(startDate);
		sb.append(",startTime=").append(startTime);
		sb.append(",endDate=").append(endDate);
		sb.append(",endTime=").append(endTime);
	
		return sb.toString();
	}
    /**
     * @return ���� endDate��
     */
    public String getEndDate()
    {
        return endDate;
    }
    /**
     * @return ���� endTime��
     */
    public String getEndTime()
    {
        return endTime;
    }
    /**
     * @return ���� roleID��
     */
    public int getRoleID()
    {
        return roleID;
    }
    /**
     * @return ���� roleName��
     */
    public String getRoleName()
    {
        return roleName;
    }
    /**
     * @return ���� startDate��
     */
    public String getStartDate()
    {
        return startDate;
    }
    /**
     * @return ���� startTime��
     */
    public String getStartTime()
    {
        return startTime;
    }
    /**
     * @return ���� timeType��
     */
    public int getTimeType()
    {
        return timeType;
    }
    /**
     * @return ���� timeValue��
     */
    public int getTimeValue()
    {
        return timeValue;
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
     * @param endDate Ҫ���õ� endDate��
     */
    public void setEndDate(String endDate)
    {
        this.endDate = endDate;
    }
    /**
     * @param endTime Ҫ���õ� endTime��
     */
    public void setEndTime(String endTime)
    {
        this.endTime = endTime;
    }
    /**
     * @param roleID Ҫ���õ� roleID��
     */
    public void setRoleID(int roleID)
    {
        this.roleID = roleID;
    }
    /**
     * @param roleName Ҫ���õ� roleName��
     */
    public void setRoleName(String roleName)
    {
        this.roleName = roleName;
    }
    /**
     * @param startDate Ҫ���õ� startDate��
     */
    public void setStartDate(String startDate)
    {
        this.startDate = startDate;
    }
    /**
     * @param startTime Ҫ���õ� startTime��
     */
    public void setStartTime(String startTime)
    {
        this.startTime = startTime;
    }
    /**
     * @param timeType Ҫ���õ� timeType��
     */
    public void setTimeType(int timeType)
    {
        this.timeType = timeType;
    }
    /**
     * @param timeValue Ҫ���õ� timeValue��
     */
    public void setTimeValue(int timeValue)
    {
        this.timeValue = timeValue;
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
    
    public boolean equals(Object obj)
    {
        if(obj == null) return false;
        if(! (obj instanceof UserRole))
            return false;
        else
        {
            UserRole userRole = (UserRole)obj;
            if(userRole.getUserID() == this.userID && userRole.getRoleID() == this.roleID)
                return true;
        }
        return false;
    }
    
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		if (Integer.MIN_VALUE == this.hashCode) 
		{
			StringBuffer sb = new StringBuffer(100);
			sb.append(userID).append(':').append(roleID);
			this.hashCode = sb.toString().hashCode();
		}
		return this.hashCode;
	}
	
	/**
	 * �жϵ�ǰʱ���Ƿ�����Ч��Χ��
	 * @param curDate
	 * @return
	 */
	public boolean isValid(Date curDate)
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime(curDate);

		if(timeType==1)
		{
			int weekday = cal.get(Calendar.DAY_OF_WEEK)-1;
			
			if(weekday==0)
			{
				weekday=7;
			}
			int setValue=1;
			for(int i=1;i<weekday;i++)
			{
//weekday��0��ʼ				
				setValue=setValue*2;
			}
			int op = timeValue & setValue;
			if(op==0)
			{
//�����趨������				
				return false;
			}
		}
		else if(timeType==2)
		{
			int monthday = cal.get(Calendar.DAY_OF_MONTH);
			int setValue=1;
			for(int i=1;i<monthday;i++)
			{
				setValue=setValue*2;
			}

			int op = timeValue & setValue;
			if(op==0)
			{
//�����趨������		
				return false;
			}
		}
		try
		{
			int shour = Integer.parseInt(startTime.substring(0,startTime.indexOf(":")));
			int sminute = Integer.parseInt(startTime.substring(startTime.indexOf(":")+1));

			Date setStartDate=getSetDate(startDate);
			
			int ehour = Integer.parseInt(endTime.substring(0,endTime.indexOf(":")));
			int eminute = Integer.parseInt(endTime.substring(endTime.indexOf(":")+1));

			Date setEndDate=getSetDate(endDate);

			 
			if(curDate.before(setStartDate))
			{
//�������õĿ�ʼ����				
				return false;
			}
			if( curDate.after(setEndDate))
			{
//�������õĿ�ʼ����				
				return false;
			}
			if(shour==0 && sminute==0 && ehour==0 && eminute==0 )
			{
//ȫ�칤��				
				return true;
			}

			if(cal.get(Calendar.HOUR_OF_DAY)<shour)
			{
//ʱ��Сʱ�������õ���ʼʱ��				
				return false;
			}
			if(cal.get(Calendar.HOUR_OF_DAY)==shour && cal.get(Calendar.MINUTE)<sminute)
			{
//ʱ������������õ���ʼʱ��				
				return false;
			}

			if(cal.get(Calendar.HOUR_OF_DAY)>ehour)
			{
//ʱ��Сʱ�������õĽ���ʱ��				
				return false;
			}
			if(cal.get(Calendar.HOUR_OF_DAY)==ehour && cal.get(Calendar.MINUTE)>eminute)
			{
//ʱ������������õĽ���ʱ��				
				return false;
			}
			
		}
		catch(Exception ex)
		{
			return false;
		}
		return true;
	}

	private Date getSetDate(String strTime)
	{
		int year = Integer.parseInt(strTime.substring(0,strTime.indexOf("-")));
		String strMonthDay = strTime.substring(strTime.indexOf("-")+1,strTime.length());
		int month = Integer.parseInt(strMonthDay.substring(0,strMonthDay.indexOf("-")));
		int day = Integer.parseInt(strMonthDay.substring(strMonthDay.indexOf("-")+1,strMonthDay.length()));
		return getSetDate( year, month, day);
		
	}
	private Date getSetDate(int year,int month,int day)
	{
		Calendar setCal = Calendar.getInstance();
		setCal.set(year,month-1,day);
		Date date=setCal.getTime();
		return date;
	}
}