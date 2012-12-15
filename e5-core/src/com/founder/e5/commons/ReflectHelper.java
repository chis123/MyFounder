/*
 * Created on 2004-3-29
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package com.founder.e5.commons;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class ReflectHelper {
	
	private static HashMap classCache = new HashMap();
	public static ClassLoader classLoader = null; 
	
	public static Class classForName(String name) throws ClassNotFoundException {
		if(classCache.get(name)!=null)
			return (Class)classCache.get(name);
		Class clazz = null;
		try {			
			clazz = getClassLoader().loadClass(name);
		}
		catch (Exception e) {
			clazz = Class.forName(name);
		}
		classCache.put(name, clazz);
		return clazz;
	}	
	
	public static ClassLoader getClassLoader() {		
		if (classLoader != null) {
			return (classLoader);
		}
		classLoader = Thread.currentThread().getContextClassLoader();		
		return classLoader;
	}
	
	public static Object executeMethod(Object obj, String methodName, 
	    Class[] paramTypes, Object[] params) throws NoSuchMethodException,
	    InvocationTargetException, IllegalAccessException
	{
	    Method method = null;	    
	    Class clazz = obj.getClass();		    
		method = clazz.getMethod(methodName, paramTypes);
	    return method.invoke(obj, params);	    
	}
	
	public static Object executeMethod(Object obj, String methodName, 
	        Object[] params)  throws NoSuchMethodException,
		    InvocationTargetException, IllegalAccessException
	{   
	    return executeMethod(obj, methodName, null, params);
	}
}
