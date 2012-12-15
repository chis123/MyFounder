package com.founder.e5.sys;

import java.util.ArrayList;
import java.util.List;

import com.founder.e5.context.CacheReader;
import com.founder.e5.context.E5Exception;

public class SysConfigReaderImpl implements SysConfigReader
{
	private SysConfigManager manager = SysFactory.getSysConfigManager();
	
	public SysConfig[] get() throws E5Exception
	{
		SysCache cache = getCache();
		if (cache == null)
			return manager.get();
		else {
			SysConfig[] configs = cache.getConfigs();
			if (configs != null) return configs;
			
			return manager.get();
		}
	}

	public SysConfig get(int sysConfigID) throws E5Exception
	{
		SysConfig[] configs = get();
		if (configs == null) return null;
		
		for (int i = 0; i < configs.length; i++) {
			if (configs[i].getSysConfigID() == sysConfigID)
				return configs[i];
		}
		return null;
	}

	public String get(int appID, String project, String item) throws E5Exception
	{
		SysConfig[] configs = get();
		if (configs == null) return null;
		
		for (int i = 0; i < configs.length; i++) {
			if (configs[i].getAppID() == appID 
					&& project.equals(configs[i].getProject()) 
					&& item.equals(configs[i].getItem()))
				return configs[i].getValue();
		}
		return null;
	}

	public SysConfig[] getAppSysConfigs(int appID) throws E5Exception
	{
		SysConfig[] configs = get();
		if (configs == null) return null;
		
		List list = new ArrayList(10);
		for (int i = 0; i < configs.length; i++) {
			if (configs[i].getAppID() == appID)
				list.add(configs[i]);
		}
		return (SysConfig[])list.toArray(new SysConfig[0]);
	}
	
	private SysCache getCache() {
		return (SysCache)CacheReader.find(SysCache.class);
	}
}
