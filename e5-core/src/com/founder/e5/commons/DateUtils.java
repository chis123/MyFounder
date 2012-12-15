package com.founder.e5.commons;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * �����ṩ����ʱ����صĹ��߷���
 * 
 * @author liyanhui
 * @version 1.0
 * @created 2006-4-10 10:00:06
 */
public class DateUtils {
	private static SimpleDateFormat dateFormat 
				= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
	/**
	 * ȡ��ǰʱ�䣬��ʽΪyyyy-MM-dd HH:mm:ss:SSS
	 * @return
	 */
    public static String format() {
		return dateFormat.format(new Date());
	}
    
	/**
	 * ȡ��ǰʱ�䣬�������ʽ
	 * @return
	 */
    public static String format(String format) {
    	SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(new Date());
	}
	/**
	 * �Ѵ��������ת�����ַ�������ʽΪyyyy-MM-dd HH:mm:ss:SSS
	 * @param calendar
	 * @return
	 */
	public static String format(Calendar calendar){
		return dateFormat.format(calendar.getTime());
	}
	/**
	 * �Ѵ��������ת�����ַ������������ʽ
	 * @param calendar
	 * @return
	 */
	public static String format(Calendar calendar, String format){
    	SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(calendar.getTime());
	}
	/**
	 * �Ѵ��������ת�����ַ�������ʽΪyyyy-MM-dd HH:mm:ss:SSS
	 * @param date
	 * @return
	 */
	public static String format(Date date){
		return dateFormat.format(date);
	}
	/**
	 * �Ѵ��������ת�����ַ������������ʽ
	 * @param date
	 * @return
	 */
	public static String format(Date date, String format){
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}
	/**
	 * ȡ��ǰ���ڣ�ʱ�䲿����0
	 * @return
	 */
	public static Date getDate(){
		Calendar ca = Calendar.getInstance();
		return clearTime(ca).getTime();
	}
	
	/**
	 * �Ѵ����������ʱ�䲿����0
	 * @param theDate
	 * @return
	 */
	public static Date getDate(Date theDate){
		Calendar ca = Calendar.getInstance();
		ca.setTime(theDate);
		return clearTime(ca).getTime();
	}
	/**
	 * ����������ȡǰn���ʱ��,ʱ�䲿����0
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
	 * ȡǰn���ʱ��,ʱ�䲿����0
	 * @param days
	 * @return Calendar
	 */
	public static Calendar getCalendarBefore(int days){
		return getCalendarBefore(Calendar.getInstance(), days);
	}
	/**
	 * ����������ȡǰn���ʱ��,ʱ�䲿����0
	 * @param theDate
	 * @param days
	 * @return Calendar
	 */
	public static Calendar getCalendarBefore(Calendar ca, int days){
		ca.add(Calendar.DATE, -1 * days);
		return clearTime(ca);
	}
	/**
	 * ���һ�����ڵ�ʱ�䲿��
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
	 * ����Timestamp���͵ĵ�ǰʱ��
	 * @return
	 */
	public static Timestamp getTimestamp()
	{
		return new Timestamp(System.currentTimeMillis());
	}
}
