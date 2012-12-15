package com.founder.e5.context;

/**
 * 数据源缓存
 * 不应该直接创建该类实例，应使用CacheManager.find方法获得一个实例。
 * @created 11-7-2005 15:46:28
 * @author Gong Lijie
 * @version 1.0
 */
public class DSCache implements Cache {
	E5DataSource[] dsArr = null;
	/**
	 * 缓存类主要用在Reader实现类中<br>
	 * 在引用时，注意不要使用构造方法创建缓存类的实例<br>
	 * 请使用如下形式：<br>
	 * <code>
	 * XXXCache cache = (XXXCache)(CacheReader.find(XXXCache.class));
	 * if (cache != null)
	 * 		return cache.get(...);
	 * </code>
	 * 其中XXXCache代表具体的缓存类
	 */
	DSCache(){

	}

	/**
	 * 缓存刷新
	 * @throws E5Exception
	 */
	public void refresh() throws E5Exception{
		DSManager manager = DSManagerImpl.getInstance();
		dsArr = manager.getAll();
	}

	/**
	 * 缓存重置
	 */
	public void reset(){
		return;
	}
	/**
	 * 从内部缓存中获得一个E5数据源对象
	 * @param dsID
	 * @return
	 */
	E5DataSource get(int dsID)
	{
		if (dsArr == null) return null;
		for (int i = 0; i < dsArr.length; i++)
			if (dsArr[i].getDsID() == dsID)
				return dsArr[i];
		return null;
	}
}