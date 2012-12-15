package com.founder.e5.cat;

import com.founder.e5.context.CacheReader;
import com.founder.e5.context.Context;

/**
 * @created on 2005-7-29
 * @author Gong Lijie
 * @version 1.0
 */
class CatHelper
{

	/**
	 * 把一个数组拼接成一个字符串
	 * @param arr
	 * @param joiner 连接符号
	 * @return
	 */
	public static String join(int[] arr, char joiner)
	{
		StringBuffer sb = new StringBuffer();
		sb.append(arr[0]);
		for (int i = 1; i < arr.length; i++)
			sb.append(joiner).append(arr[i]);
		return sb.toString();
	}
	
	/**
	 * 内部方法，获取Manager接口
	 * 统一改用Context.getBean的形式
	 * 需要context包和e5-config.xml的支持
	 * @return
	 */
	static CatExtManager getExtManager()
	{
		CatExtManager manager = (CatExtManager)Context.getBean(CatExtManager.class);
		if (manager == null)
			manager = Factory.buildCatExtManager();
		return manager;
	}

	static CatManager getCatManager()
	{
		CatManager manager = (CatManager)Context.getBean(CatManager.class);
		if (manager == null)
			manager = Factory.buildCatManager();
		return manager;
	}

	static CatCache getCache()
	{
		return (CatCache)CacheReader.find(CatCache.class);
	}

}
