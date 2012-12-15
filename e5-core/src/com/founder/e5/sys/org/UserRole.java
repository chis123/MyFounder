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
	 * 角色ID
	 */
	private int roleID;
	/**
	 * 角色名称
	 */
	private String roleName;
	/**
	 * 用户ID
	 */
	private int userID;
	/**
	 * 用户编码
	 */
	private String userCode;
	/**
	 * 角色开始日期
	 */
	private String startDate;
	/**
	 * 角色结束日期
	 */
	private String endDate;
	/**
	 * 角色开始时间
	 */
	private String startTime;
	/**
	 * 角色结束时间
	 */
	private String endTime;
	/**
	 * 时间类型
	 */
	private int timeType;
	/**
	 * 时间值
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
     * @return 返回 endDate。
     */
    public String getEndDate()
    {
        return endDate;
    }
    /**
     * @return 返回 endTime。
     */
    public String getEndTime()
    {
        return endTime;
    }
    /**
     * @return 返回 roleID。
     */
    public int getRoleID()
    {
        return roleID;
    }
    /**
     * @return 返回 roleName。
     */
    public String getRoleName()
    {
        return roleName;
    }
    /**
     * @return 返回 startDate。
     */
    public String getStartDate()
    {
        return startDate;
    }
    /**
     * @return 返回 startTime。
     */
    public String getStartTime()
    {
        return startTime;
    }
    /**
     * @return 返回 timeType。
     */
    public int getTimeType()
    {
        return timeType;
    }
    /**
     * @return 返回 timeValue。
     */
    public int getTimeValue()
    {
        return timeValue;
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
     * @param endDate 要设置的 endDate。
     */
    public void setEndDate(String endDate)
    {
        this.endDate = endDate;
    }
    /**
     * @param endTime 要设置的 endTime。
     */
    public void setEndTime(String endTime)
    {
        this.endTime = endTime;
    }
    /**
     * @param roleID 要设置的 roleID。
     */
    public void setRoleID(int roleID)
    {
        this.roleID = roleID;
    }
    /**
     * @param roleName 要设置的 roleName。
     */
    public void setRoleName(String roleName)
    {
        this.roleName = roleName;
    }
    /**
     * @param startDate 要设置的 startDate。
     */
    public void setStartDate(String startDate)
    {
        this.startDate = startDate;
    }
    /**
     * @param startTime 要设置的 startTime。
     */
    public void setStartTime(String startTime)
    {
        this.startTime = startTime;
    }
    /**
     * @param timeType 要设置的 timeType。
     */
    public void setTimeType(int timeType)
    {
        this.timeType = timeType;
    }
    /**
     * @param timeValue 要设置的 timeValue。
     */
    public void setTimeValue(int timeValue)
    {
        this.timeValue = timeValue;
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
	 * 判断当前时间是否在有效范围内
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
//weekday从0开始				
				setValue=setValue*2;
			}
			int op = timeValue & setValue;
			if(op==0)
			{
//不在设定的周内				
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
//不在设定的月内		
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
//早于设置的开始日期				
				return false;
			}
			if( curDate.after(setEndDate))
			{
//晚于设置的开始日期				
				return false;
			}
			if(shour==0 && sminute==0 && ehour==0 && eminute==0 )
			{
//全天工作				
				return true;
			}

			if(cal.get(Calendar.HOUR_OF_DAY)<shour)
			{
//时间小时早于设置的起始时间				
				return false;
			}
			if(cal.get(Calendar.HOUR_OF_DAY)==shour && cal.get(Calendar.MINUTE)<sminute)
			{
//时间分钟早于设置的起始时间				
				return false;
			}

			if(cal.get(Calendar.HOUR_OF_DAY)>ehour)
			{
//时间小时晚于设置的结束时间				
				return false;
			}
			if(cal.get(Calendar.HOUR_OF_DAY)==ehour && cal.get(Calendar.MINUTE)>eminute)
			{
//时间分钟晚于设置的结束时间				
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