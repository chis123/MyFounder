package com.founder.e5.context;

import java.util.Hashtable;

import com.founder.e5.commons.Log;
import com.founder.e5.config.*;

/**
 * 缓存的管理类
 * 功能包括对配置的缓存进行刷新、保持缓存对象、访问缓存对象
 * @created 11-7-2005 15:46:22
 * @author Gong Lijie
 * @version 1.0
 */
public class CacheManager extends CacheReader {
	private static Log log = Context.getLog("e5.sys");
	/**
	 * 刷新所有缓存
	 * 从配置中取出缓存列表，对每个类访问其Cache接口
	 * 每个缓存类都必须实现Cache接口
	 */
	public synchronized static void refresh() throws Exception {
		if (classNames == null) return;
		
		for (int i = 0; i < classNames.length; i++)
		{
			refresh(classNames[i]);
		}
	}

	/**
	 * 单个缓存的刷新
	 * 调用传入类的Cache接口
	 * 不检验刷新类名。该方法可以用来刷新非配置中的缓存类，
	 * 只要该类实现了Cache接口
	 * @param className 要刷新的缓存类名，该类必须实现了Cache接口
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
	 * 单个缓存的刷新
	 * 调用传入类的Cache接口
	 * 不检验刷新类名。该方法可以用来刷新非配置中的缓存类，
	 * 只要该类实现了Cache接口
	 * @param clazz 要刷新的缓存类，该类必须实现了Cache接口
	 */
	public synchronized static void refresh(Class clazz) throws Exception {
		refresh(clazz.getName());
	}
	
	/**
	 * 从启动模块中得到配置的缓存类列表
	 * 然后做第一次的缓存刷新
	 * 该方法只在系统启动时调用一次
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
		
		//初始化
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