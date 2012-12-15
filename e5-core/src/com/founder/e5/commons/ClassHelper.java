package com.founder.e5.commons;

/**
 * Created on 2004-12-8
 * @author Ding Ning
 */
public class ClassHelper {
    
    /**
     * 根据全称类名获得其对应的Class对象
     * @param className  类名
     * 
     * @return           指定类名对应的Class对象
     * @throws ClassNotFoundException
     */
    public static Class applicationClass(String className)
    	throws ClassNotFoundException {
        // Look up the class loader to be used
        ClassLoader classLoader = 
            Thread.currentThread().getContextClassLoader();
        if (classLoader == null) {
            classLoader = ClassHelper.class.getClassLoader();
        }
        // Attempt to load the specified class
        return (classLoader.loadClass(className));
    }
    
    /**
     * 通过指定类名获得该类的一个实例
     * @param className    类名
     * @return             指定类名的一个实例
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static Object applicationInstance(String className)
    	throws ClassNotFoundException, IllegalAccessException, 
    	InstantiationException {

        return (applicationClass(className).newInstance());

    }
}
