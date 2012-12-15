package com.founder.e5.sys;

import com.founder.e5.context.Cache;
import com.founder.e5.context.Context;
import com.founder.e5.context.E5Exception;

/**
 * 系统配置参数的缓存，包括存储设备的缓存、系统参数的缓存。
 * 此缓存为最新提供，之前一直没有缓存。
 * @created 2008-12-19
 * @author Gong Lijie
 * @version 1.0
 */
public class SysCache implements Cache {
	private SysConfig[] configs = null;
	private StorageDevice[] devices = null;

	/**
	 * 缓存类主要用在Reader实现类中<br>
	 * 在引用时，注意不要使用构造方法创建缓存类的实例<br>
	 * 请使用如下形式：<br>
	 * <pre>
	 * <code>
	 * XXXCache cache = (XXXCache)(CacheReader.find(XXXCache.class));
	 * if (cache != null)
	 * 		return cache.get(...);
	 * </code>
	 * </pre>
	 * 其中XXXCache代表具体的缓存类
	 */
	public SysCache() {
	}
	/**
	 * 缓存刷新
	 * @throws E5Exception
	 */
	public void refresh() throws E5Exception {
		SysConfigManager configManager = (SysConfigManager)Context.getBean(SysConfigManager.class);
		configs = configManager.get();
		
		StorageDeviceManager deviceManager = (StorageDeviceManager)Context.getBean(StorageDeviceManager.class);
		devices = deviceManager.get();
	}
	/**
	 * 缓存重置
	 */
	public void reset(){
	}
	
	SysConfig[] getConfigs() {
		return configs;
	}
	StorageDevice[] getDevices() {
		return devices;
	}
}
