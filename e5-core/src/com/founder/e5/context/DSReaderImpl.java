package com.founder.e5.context;

/**
 * E5数据源读取实现类
 * @created 11-7-2005 15:46:26
 * @author Gong Lijie
 * @version 1.0
 */
class DSReaderImpl implements DSReader {
	private static DSReaderImpl reader;

	/**
	 * 单例模式，构造方法私有
	 */
	private DSReaderImpl() {		
	}

	/**
	 * 单例模式下的取Reader方法
	 * @return DSReaderImpl
	 */
	public static DSReaderImpl getInstance(){
		if (reader == null)
			reader = new DSReaderImpl();
		return reader;
	}
	/**
	 * 读取E5数据源
	 * 首先从内部缓存中获得数据源，若没有缓存，则从数据库再查一次
	 * @param dsID
	 */
	public E5DataSource get(int dsID) throws E5Exception {
		DSCache cache = (DSCache)(CacheReader.find(DSCache.class));
		if (cache != null)
			return cache.get(dsID);

		DSManager manager = DSManagerImpl.getInstance();
		return manager.get(dsID);
	}
}