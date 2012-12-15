package com.founder.e5.commons;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 该类提供日期时间相关的工具方法
 * 
 * @author liyanhui
 * @version 1.0
 * @created 2006-4-10 10:00:06
 */
public class DateUtils {
	private static SimpleDateFormat dateFormat 
				= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
	/**
	 * 取当前时间，格式为yyyy-MM-dd HH:mm:ss:SSS
	 * @return
	 */
    public static String format() {
		return dateFormat.format(new Date());
	}
    
	/**
	 * 取当前时间，按传入格式
	 * @return
	 */
    public static String format(String format) {
    	SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(new Date());
	}
	/**
	 * 把传入的日期转换成字符串，格式为yyyy-MM-dd HH:mm:ss:SSS
	 * @param calendar
	 * @return
	 */
	public static String format(Calendar calendar){
		return dateFormat.format(calendar.getTime());
	}
	/**
	 * 把传入的日期转换成字符串，按传入格式
	 * @param calendar
	 * @return
	 */
	public static String format(Calendar calendar, String format){
    	SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(calendar.getTime());
	}
	/**
	 * 把传入的日期转换成字符串，格式为yyyy-MM-dd HH:mm:ss:SSS
	 * @param date
	 * @return
	 */
	public static String format(Date date){
		return dateFormat.format(date);
	}
	/**
	 * 把传入的日期转换成字符串，按传入格式
	 * @param date
	 * @return
	 */
	public static String format(Date date, String format){
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}
	/**
	 * 取当前日期，时间部分置0
	 * @return
	 */
	public static Date getDate(){
		Calendar ca = Calendar.getInstance();
		return clearTime(ca).getTime();
	}
	
	/**
	 * 把传入的日期其时间部分置0
	 * @param theDate
	 * @return
	 */
	public static Date getDate(Date theDate){
		Calendar ca = Calendar.getInstance();
		ca.setTime(theDate);
		return clearTime(ca).getTime();
	}
	/**
	 * 按传入日期取前n天的时间,时间部分置0
	 * @param theDate
	 * @param days
	 * @return Calendar
	 */
	public static Calendar getCalendarBefore(Date theDate, int days){
		Calendar ca = Calendar.getInstance();
		ca.setTime(theDate);

		return getCalendarBefore(ca, days);
	}
	/**
	 * 取前n天的时间,时间部分置0
	 * @param days
	 * @return Calendar
	 */
	public static Calendar getCalendarBefore(int days){
		return getCalendarBefore(Calendar.getInstance(), days);
	}
	/**
	 * 按传入日期取前n天的时间,时间部分置0
	 * @param theDate
	 * @param days
	 * @return Calendar
	 */
	public static Calendar getCalendarBefore(Calendar ca, int days){
		ca.add(Calendar.DATE, -1 * days);
		return clearTime(ca);
	}
	/**
	 * 清除一个日期的时间部分
	 * @param ca
	 */
	private static Calendar clearTime(Calendar ca)
	{
		//ca.clear(Calendar.HOUR_OF_DAY);
		ca.set(Calendar.HOUR_OF_DAY, 0);
		ca.clear(Calendar.MINUTE);
		ca.clear(Calendar.SECOND);
		ca.clear(Calendar.MILLISECOND);
		return ca;
	}
	
	/**
	 * 返回Timestamp类型的当前时间
	 * @return
	 */
	public static Timestamp getTimestamp()
	{
		return new Timestamp(System.currentTimeMillis());
	}
}
