package com.founder.e5.config;
import java.util.List;
import java.util.ArrayList;

/**
 * 配置文件中的缓存信息类
 * 需要在应用启动时自动读取并在ConfigReader中保留一份实例
 * 系统中只有这一份实例
 * @created on 2005-7-13
 * @author Gong Lijie
 * @version 1.0
 */
public class CacheConfig
{
	private String autoRefresh;
	private List caches = new ArrayList(10);
	
	/**
	 * @return Returns the autoRefresh.
	 */
	public String getAutoRefresh()
	{
		return autoRefresh;
	}
	/**
	 * @param autoRefresh The autoRefresh to set.
	 */
	public void setAutoRefresh(String autoRefresh)
	{
		this.autoRefresh = autoRefresh;
	}

	/**
     * 增加一个缓存加载项
     * @param info CacheInfo
     */
    public void addInfo(InfoCache info)
    {
    	caches.add(info);
    }
    
	/**
	 * @return Returns the map.
	 */
	public InfoCache[] getCaches()
	{
		return (InfoCache[])caches.toArray(new InfoCache[0]);
	}
	public String toString(){
		StringBuffer sbInfo = new StringBuffer(400);
		sbInfo.append("[autoRefresh:").append(autoRefresh);
		InfoCache[] cacheArr = (InfoCache[])caches.toArray(new InfoCache[0]);
		if (caches != null)
			for (int i = 0; i < cacheArr.length; i++)
				sbInfo.append("\n").append(cacheArr[i].toString());
		sbInfo.append("\n]");
		return sbInfo.toString();
	}
}
