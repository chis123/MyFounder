package com.founder.e5.commons;
import java.util.*;
import java.text.*;
import java.io.*;

/**
 * Created on 2004-3-30
 * @author Ding Ning
 */
public class StringHelper {
	
	/**
	 * 24小时格式
	 */
	public static SimpleDateFormat format= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");	
	
	public static boolean isEmpty(String str)
	{
		if(str==null || str.trim().equals(""))
			return true;
		else
			return false;
	}
    public static String formatUnderLine(String str)
	{
		if(StringHelper.isEmpty(str))
			return str;
		if(str.indexOf("_")<0)
			return str;
		StringTokenizer st = new StringTokenizer(str, "_", true);
		StringBuffer sb = new StringBuffer(str.length());
		while(st.hasMoreTokens())
		{
			String token = st.nextToken();
			if(token.equals("_"))
				sb.append(token);
			else
				sb.append(toUpperFirstChar(token));
		}
		return sb.toString();
	}
	
	public static String toUpperFirstChar(String str)
	{
		if(StringHelper.isEmpty(str))
			return str;
		return str.substring(0,1).toUpperCase() + 
			str.substring(1);
	}	
	public static String toLowerFirstChar(String str)
	{
		if(StringHelper.isEmpty(str))
			return str;
		return str.substring(0,1).toLowerCase() + 
			str.substring(1);
	}
	public static String space(int count)
	{
		StringBuffer sb = new StringBuffer();
		int i=0;
		while(i<count)
		{	
			sb.append(" ");
			i++;
		}		
		return sb.toString();
	}
	public static String[] split(String list, String seperators) {
		return split(list, seperators, false);
	}

	public static String[] split(String list, String seperators, boolean include) 
	{
		if(isEmpty(list))
			return new String[0];
		StringTokenizer tokens = new StringTokenizer(list, seperators, include);
		String[] result = new String[ tokens.countTokens() ];
		int i=0;
		while ( tokens.hasMoreTokens() ) {
			result[i++] = tokens.nextToken();
		}
		return result;
	}
	public static String trim(String str)
	{
		if(isEmpty(str))		
			return str;			
		else 
			return str.trim();
	}
	
	public static String format(java.util.Date date)
	{
		if(date==null)
			return null;
		else
			return format.format(date); 
	}	
	
	public static String format(Integer obj)
	{
		if(obj==null)
			return null;
		else
			return obj.toString(); 
	}	
	
	public static String format(Long obj)
	{
		if(obj==null)
			return null;
		else
			return obj.toString(); 
	}
	public static String format(Double obj)
	{
		if(obj==null)
			return null;
		else
			return obj.toString(); 
	}
	public static final String stackTrace(Throwable e)
	{
		String result = null;
		try
		{
			StringWriter writer = new StringWriter();
			e.printStackTrace( new PrintWriter(writer, true) );
			result = writer.toString();
		}
		catch (Exception exception)
		{
			// Do nothing.
		}
		return result;
	}
	
	/**
	 * 判断数组arr中是否包含str
	 * @param str  参数不能为null
	 * @param arr  数组中元素不能为null
	 * @param ingoreCase  字符串比较时是否忽略大小写,true忽略大小写比较，false不忽略
	 * @return
	 */	
	public static boolean include(String str, String[] arr, boolean ingoreCase)
	{	    
	    if(str!=null && str.length()!=0 && str!=null)
	    {
	        for(int i=0; i<arr.length; i++)
	        {
	            if(ingoreCase && arr[i].equalsIgnoreCase(str))
	            {
	                return true;	                
	            }
	            else if(!ingoreCase && arr[i].equals(str))
	            {
	                return true;	                
	            }	                
	        }
	    }
	    return false;
	}
	
	/**
	 * 判断arr中是否包含str，忽略大小写比较
	 * @param str
	 * @param arr
	 * @return
	 */
	public static boolean include(String str, String[] arr)
	{
	    return include(str, arr, false);
	}
	
	public static String toString(String[] arr)
	{
	    if(arr==null)
	        return null;
	    if(arr.length==0)
	        return "";
	    StringBuffer sb = new StringBuffer();
	    sb.append("{");
	    for(int i=0; i<arr.length; i++)
	    {
	        sb.append(arr[i]);
	        if(i!=arr.length-1)
	            sb.append(",");
	    }
	    sb.append("}");
	    return sb.toString();
	}
	public static String toString(long[] arr)
	{
	    if(arr==null)
	        return null;
	    if(arr.length==0)
	        return "";
	    StringBuffer sb = new StringBuffer();
	    for(int i=0; i<arr.length; i++)
	    {
	        sb.append(arr[i]);
	        if(i!=arr.length-1)
	            sb.append(",");
	    }
	    return sb.toString();
	}
	/**
	 * map的key是String,value是String[]
	 * @param map
	 * @return
	 */
	public static String toString(Map map)
	{
	    if(map==null)
	        return null;
	    Iterator keys = map.keySet().iterator();
	    StringBuffer sb = new StringBuffer();
	    while(keys.hasNext())
	    {
	        String key = (String)keys.next();
	        sb.append(key).append(":{");
	        sb.append(StringHelper.toString(map.get(key)));	        
	        sb.append("}");
	        if(keys.hasNext())
	            sb.append(";");
	    }
	    return sb.toString();
	}
	public static String toString(Object obj)
	{
	    return obj.toString();
	}
	public static String toString(Collection coll)
	{
	    Iterator keys = coll.iterator();
	    StringBuffer sb = new StringBuffer("{");
	    while(keys.hasNext())
	    {
	        String key = (String)keys.next();
	        sb.append(key);
	        if(keys.hasNext())
	            sb.append(",");
	    }
	    sb.append("}");
	    return sb.toString();
	}
	/**
	 * 将字符串转换为可以在xml中输出的格式
	 * @param s
	 * @return
	 */
	public static String escapeXml(String s) {
		if (s == null) return null;
		StringBuffer sb = new StringBuffer();
		for(int i=0; i<s.length(); i++) {
			char c = s.charAt(i);
			if (c == '<') {
				sb.append("&lt;");
			} else if (c == '>') {
				sb.append("&gt;");
			} else if (c == '\'') {
				sb.append("&apos;");
			} else if (c == '&') {
				sb.append("&amp;");
			} else if (c == '"') {
				sb.append("&quot;");
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}
	/**
	 * 
	 * @param value      ??×?·???
	 * @param key        ??±?????????×?·???
	 * @param replaceValue  ????key????×?·???
	 * @return
	 */
	public static String replace(String value, String key, String replaceValue) {
		if (value != null && key != null && replaceValue != null) {
			int pos = value.indexOf(key);
			
			if (pos >= 0) {
				int length = value.length();
				int start = pos;
				int end = pos + key.length();
				
				if (length == key.length()) {
					value = replaceValue;
				} else if (end == length) {
					value = value.substring(0, start) + replaceValue; 
				} else {
					value = value.substring(0, start) + replaceValue + 
						replace(value.substring(end), key, replaceValue);
				}
			}
		}
		return value;
	}
}
