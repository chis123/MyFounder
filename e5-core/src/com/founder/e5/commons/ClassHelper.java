package com.founder.e5.commons;

/**
 * Created on 2004-12-8
 * @author Ding Ning
 */
public class ClassHelper {
    
    /**
     * ����ȫ������������Ӧ��Class����
     * @param className  ����
     * 
     * @return           ָ��������Ӧ��Class����
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
     * ͨ��ָ��������ø����һ��ʵ��
     * @param className    ����
     * @return             ָ��������һ��ʵ��
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
