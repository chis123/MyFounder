package com.founder.e5.flow;
import com.founder.e5.context.Cache;
import com.founder.e5.context.E5Exception;

/**
 * @deprecated 2006-5-17
 * �Ѿ����ϲ���FlowCache��
 * �����������
 * @created 04-8-2005 15:32:22
 * @author Gong Lijie
 * @version 1.0
 */
public class ProcOrderCache implements Cache {
	private ProcOrder[] orders = null;
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
	public ProcOrderCache()
	{
	}
	/**
	 * ����ˢ��
	 */
	public void refresh() throws E5Exception
	{
		orders = FlowHelper.getProcOrderManager().getAll();
	}

	/**
	 * ��������
	 */
	public void reset(){

	}
	ProcOrder[] getOrders()
	{
		return orders;
	}

}