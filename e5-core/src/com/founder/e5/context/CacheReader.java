package com.founder.e5.context;
import java.util.Map;
import java.util.List;

/**
 * �����ȡ��
 * ����ϵͳ�����еĻ��棬����ģ����ʻ���ʱ��ͨ�������ľ�̬��������
 * @created 11-7-2005 15:46:21
 * @author Gong Lijie
 * @version 1.0
 */
public class CacheReader {

	/**
	 * �Ƿ��Զ�ˢ�»���
	 * �������ļ�������
	 */
	protected static boolean autoRefresh = false;
	/**
	 * ����ÿ����������HashTable(value������null)
	 * ��������key������Ķ�����Ϊvalue
	 */
	protected static Map caches;
	/**
	 * ��¼����������������
	 * ���ṩ������ˢ�½��沿��
	 */
	protected static String[] cacheNames;
	
	/**
	 * ��¼������������������
	 * �����ڻ���ˢ�½���
	 */
	protected static String[] classNames;
	/**
	 * Ӧ�÷�������URL
	 * ��Ӧ�÷�����ʱά������б�
	 */
	protected static List servers;

	/**
	 * ȡһ���������
	 * 
	 * @param className ������������
	 */
	public static Object find(String className){
		if (caches == null) return null;
		
		return caches.get(className);
	}

	/**
	 * ȡһ���������
	 * 
	 * @param clazz ���������
	 */
	public static Object find(Class clazz){
		return find(clazz.getName());
	}
	/**
	 * ȡ���л������������Ƶ��б�
	 * �ڻ��������ʹ��
	 */
	public static String[] getCacheNames(){
		return cacheNames;
	}

	/**
	 * ȡ���л���������������Ƶ��б�
	 * �ڻ��������ʹ��
	 */
	public static String[] getCacheClasses(){
		return classNames;
	}
	/**
	 * ��ϵͳ�����ã��ж��Ƿ��Զ�ˢ�»���
	 * ����autoRefresh��ֵ
	 */
	public static boolean isAutoRefresh(){
		return autoRefresh;
	}

}