package com.founder.e5.context;

/**
 * E5����Դ��ȡʵ����
 * @created 11-7-2005 15:46:26
 * @author Gong Lijie
 * @version 1.0
 */
class DSReaderImpl implements DSReader {
	private static DSReaderImpl reader;

	/**
	 * ����ģʽ�����췽��˽��
	 */
	private DSReaderImpl() {		
	}

	/**
	 * ����ģʽ�µ�ȡReader����
	 * @return DSReaderImpl
	 */
	public static DSReaderImpl getInstance(){
		if (reader == null)
			reader = new DSReaderImpl();
		return reader;
	}
	/**
	 * ��ȡE5����Դ
	 * ���ȴ��ڲ������л������Դ����û�л��棬������ݿ��ٲ�һ��
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