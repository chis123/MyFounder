/*
 * Created on 2005-4-25 9:54:41
 *
 */
package com.founder.e5.commons;

import java.io.IOException;
import java.io.InputStream;

/**
 * 提供了载入属性文件的便利方法
 * @author liyanhui
 * @version 1.0
 * @created 2005-4-25 9:54:41
 */
public class PropertyUtils {

    public static void main(String[] args) {

    }

    /**
     * 从当前类路径下载入属性文件.
     * 注意：该方法使用当前线程的上下文类载入器在类路径下搜索资源，如果在web应用中要注意
     * 用在jsp中和servlet时类载入器不同；
     * @param uri 属性文件相对于类路径的相对路径名 (not-null)
     * @return java.util.Properties
     * @throws IOException 当指定资源找不到或读取失败时
     */
    public static java.util.Properties loadProperties(String uri)
            throws IOException {
        return loadProperties(uri, null);
    }

    /**
     * 从当前类路径下载入属性文件.
     * 注意：该方法使用当前线程的上下文类载入器在类路径下搜索资源，如果在web应用中要注意
     * 用在jsp中和servlet时类载入器不同；
     * @param uri 属性文件相对于类路径的相对路径名 (not-null)
     * @param encoding 属性文件的编码 (null-safe)
     * @return java.util.Properties
     * @throws IOException 当指定资源找不到或读取失败时
     */
    public static java.util.Properties loadProperties(String uri,
            String encoding) throws IOException {
        if (uri == null)
            throw new NullPointerException();
        
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        InputStream is = cl.getResourceAsStream(uri);
        if (is == null)
            throw new IOException("resource not found: " + uri);

        try {
            if (encoding != null) {
            	com.founder.e5.commons.Properties result = new com.founder.e5.commons.Properties();
                result.load(is, encoding);
                return result;
            } else {
            	 java.util.Properties result  = new java.util.Properties();
            	 result.load(is);
            	 return result;
            }
        } finally {
            ResourceMgr.closeQuietly(is);
        }
    }

}