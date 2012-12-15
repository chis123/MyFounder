package com.founder.e5.context;

/**
 * ����Դ����
 * ��Ӧ��ֱ�Ӵ�������ʵ����Ӧʹ��CacheManager.find�������һ��ʵ����
 * @created 11-7-2005 15:46:28
 * @author Gong Lijie
 * @version 1.0
 */
public class DSCache implements Cache {
	E5DataSource[] dsArr = null;
	/**
	 * ��������Ҫ����Readerʵ������<br>
	 * ������ʱ��ע�ⲻҪʹ�ù��췽�������������ʵ��<br>
	 * ��ʹ��������ʽ��<br>
	 * <code>
	 * XXXCache cache = (XXXCache)(CacheReader.find(XXXCache.class));
	 * if (cache != null)
	 * 		return cache.get(...);
	 * </code>
	 * ����XXXCache�������Ļ�����
	 */
	DSCache(){

	}

	/**
	 * ����ˢ��
	 * @throws E5Exception
	 */
	public void refresh() throws E5Exception{
		DSManager manager = DSManagerImpl.getInstance();
		dsArr = manager.getAll();
	}

	/**
	 * ��������
	 */
	public void reset(){
		return;
	}
	/**
	 * ���ڲ������л��һ��E5����Դ����
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