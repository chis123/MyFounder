/*
 * Created on 2005-4-1
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.founder.e5.commons;
import java.text.SimpleDateFormat;
import java.util.*;
/**
 * @author dingning
 */
public class TimeHelper {

    public static String getNow()
    {   
        return defaultFormat.format(new Date());
    }
    
    public static String getNow(String pattern)
    {
        SimpleDateFormat format = (SimpleDateFormat)formatMap.get(pattern);
        if(format==null)
        {
            format = new SimpleDateFormat(pattern);
            formatMap.put(pattern, format);
        }
        return format.format(new Date());
    }
    
    private static SimpleDateFormat defaultFormat = 
        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    private static HashMap formatMap = new HashMap();
}
