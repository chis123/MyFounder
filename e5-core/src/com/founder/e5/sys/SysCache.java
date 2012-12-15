package com.founder.e5.sys;

import com.founder.e5.context.Cache;
import com.founder.e5.context.Context;
import com.founder.e5.context.E5Exception;

/**
 * ϵͳ���ò����Ļ��棬�����洢�豸�Ļ��桢ϵͳ�����Ļ��档
 * �˻���Ϊ�����ṩ��֮ǰһֱû�л��档
 * @created 2008-12-19
 * @author Gong Lijie
 * @version 1.0
 */
public class SysCache implements Cache {
	private SysConfig[] configs = null;
	private StorageDevice[] devices = null;

	/**
	 * ��������Ҫ����Readerʵ������<br>
	 * ������ʱ��ע�ⲻҪʹ�ù��췽�������������ʵ��<br>
	 * ��ʹ��������ʽ��<br>
	 * <pre>
	 * <code>
	 * XXXCache cache = (XXXCache)(CacheReader.find(XXXCache.class));
	 * if (cache != null)
	 * 		return cache.get(...);
	 * </code>
	 * </pre>
	 * ����XXXCache�������Ļ�����
	 */
	public SysCache() {
	}
	/**
	 * ����ˢ��
	 * @throws E5Exception
	 */
	public void refresh() throws E5Exception {
		SysConfigManager configManager = (SysConfigManager)Context.getBean(SysConfigManager.class);
		configs = configManager.get();
		
		StorageDeviceManager deviceManager = (StorageDeviceManager)Context.getBean(StorageDeviceManager.class);
		devices = deviceManager.get();
	}
	/**
	 * ��������
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
