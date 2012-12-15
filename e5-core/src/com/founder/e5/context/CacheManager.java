package com.founder.e5.context;

import java.util.Hashtable;

import com.founder.e5.commons.Log;
import com.founder.e5.config.*;

/**
 * ����Ĺ�����
 * ���ܰ��������õĻ������ˢ�¡����ֻ�����󡢷��ʻ������
 * @created 11-7-2005 15:46:22
 * @author Gong Lijie
 * @version 1.0
 */
public class CacheManager extends CacheReader {
	private static Log log = Context.getLog("e5.sys");
	/**
	 * ˢ�����л���
	 * ��������ȡ�������б���ÿ���������Cache�ӿ�
	 * ÿ�������඼����ʵ��Cache�ӿ�
	 */
	public synchronized static void refresh() throws Exception {
		if (classNames == null) return;
		
		for (int i = 0; i < classNames.length; i++)
		{
			refresh(classNames[i]);
		}
	}

	/**
	 * ���������ˢ��
	 * ���ô������Cache�ӿ�
	 * ������ˢ���������÷�����������ˢ�·������еĻ����࣬
	 * ֻҪ����ʵ����Cache�ӿ�
	 * @param className Ҫˢ�µĻ����������������ʵ����Cache�ӿ�
	 */
	public synchronized static void refresh(String className) throws Exception {
		log.info("[Refresh]" + className);

		Object cacheObj = Class.forName(className).newInstance();
		Cache cache = (Cache)cacheObj;
		cache.refresh();
		caches.put(className, cacheObj);

		log.info("[Refresh]ok");
	}

	/**
	 * ���������ˢ��
	 * ���ô������Cache�ӿ�
	 * ������ˢ���������÷�����������ˢ�·������еĻ����࣬
	 * ֻҪ����ʵ����Cache�ӿ�
	 * @param clazz Ҫˢ�µĻ����࣬�������ʵ����Cache�ӿ�
	 */
	public synchronized static void refresh(Class clazz) throws Exception {
		refresh(clazz.getName());
	}
	
	/**
	 * ������ģ���еõ����õĻ������б�
	 * Ȼ������һ�εĻ���ˢ��
	 * �÷���ֻ��ϵͳ����ʱ����һ��
	 */
	public synchronized static void init() throws Exception {
		log.info("[Refresh]Init");

		if (cacheNames != null)
		{
			log.info("[Refresh]Cache already loaded.");
			return;
		}
		
		ConfigReader reader = ConfigReader.getInstance();

		servers = reader.getServers();
		CacheConfig cacheConfig = reader.getCacheConfig();
		if (cacheConfig == null)
		{
			log.info("[Refresh]No cache config.");
			return;
		}
		
		autoRefresh = cacheConfig.getAutoRefresh().equals("true");
		if (log.isInfoEnabled())
			log.info("[Refresh]AutoRefresh:" + autoRefresh);

		InfoCache[] cacheArr  = cacheConfig.getCaches();
		if (cacheArr == null) return;
		
		//��ʼ��
		int size = cacheArr.length;
		caches = new Hashtable(size);

		cacheNames = new String[size];
		classNames = new String[size];
		
		for (int i = 0; i < cacheArr.length; i++)
		{
			cacheNames[i] = cacheArr[i].getName();
			classNames[i] = cacheArr[i].getInvokeClass();
		}
		refresh();
	}

	/**
	 * @test
	 * @return
	 */
	public static void show()
	{
		log.info("caches:" + caches);
	}
}