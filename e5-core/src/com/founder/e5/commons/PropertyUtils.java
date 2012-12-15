/*
 * Created on 2005-4-25 9:54:41
 *
 */
package com.founder.e5.commons;

import java.io.IOException;
import java.io.InputStream;

/**
 * �ṩ�����������ļ��ı�������
 * @author liyanhui
 * @version 1.0
 * @created 2005-4-25 9:54:41
 */
public class PropertyUtils {

    public static void main(String[] args) {

    }

    /**
     * �ӵ�ǰ��·�������������ļ�.
     * ע�⣺�÷���ʹ�õ�ǰ�̵߳�������������������·����������Դ�������webӦ����Ҫע��
     * ����jsp�к�servletʱ����������ͬ��
     * @param uri �����ļ��������·�������·���� (not-null)
     * @return java.util.Properties
     * @throws IOException ��ָ����Դ�Ҳ������ȡʧ��ʱ
     */
    public static java.util.Properties loadProperties(String uri)
            throws IOException {
        return loadProperties(uri, null);
    }

    /**
     * �ӵ�ǰ��·�������������ļ�.
     * ע�⣺�÷���ʹ�õ�ǰ�̵߳�������������������·����������Դ�������webӦ����Ҫע��
     * ����jsp�к�servletʱ����������ͬ��
     * @param uri �����ļ��������·�������·���� (not-null)
     * @param encoding �����ļ��ı��� (null-safe)
     * @return java.util.Properties
     * @throws IOException ��ָ����Դ�Ҳ������ȡʧ��ʱ
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